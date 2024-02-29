<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="article-title-container">
      <el-input v-model="note.noteTitle" size="medium" placeholder="输入笔记标题" />
      <el-button
        type="danger"
        size="medium"
        class="save-btn"
        @click="saveNoteDraft"
        v-if="note.id == null || note.status == 3">
        保存草稿
      </el-button>
      <el-button type="danger" size="medium" @click="openModel" style="margin-left: 10px"> 发布笔记</el-button>
    </div>
    <mavon-editor ref="md" v-model="note.noteContent" @imgAdd="uploadImg" style="height: calc(100vh - 260px)" />
    <el-dialog :visible.sync="addOrEdit" width="40%" top="3vh">
      <div class="dialog-title-container" slot="title">发布笔记</div>
      <el-form label-width="80px" size="medium" :model="note">
        <el-form-item label="笔记合集">
          <el-tag
            type="success"
            v-show="note.collectionName"
            style="margin: 0 1rem 0 0"
            :closable="true"
            @close="removeCollection">
            {{ note.collectionName }}
          </el-tag>
          <el-popover placement="bottom-start" width="460" trigger="click" v-if="!note.collectionName">
            <div class="popover-title">合集</div>
            <el-autocomplete
              style="width: 100%"
              v-model="collectionName"
              :fetch-suggestions="searchCollection"
              placeholder="请输入合集名搜索，enter可添加自定义分类"
              :trigger-on-focus="false"
              @keyup.enter.native="saveCollection"
              @select="handleSelectCollections">
              <template slot-scope="{ item }">
                <div>{{ item.collectionName }}</div>
              </template>
            </el-autocomplete>
            <div class="popover-container">
              <div v-for="item of collections" :key="item.id" class="category-item" @click="addCollection(item)">
                {{ item.collectionName }}
              </div>
            </div>
            <el-button type="success" plain slot="reference" size="small"> 添加合集</el-button>
          </el-popover>
        </el-form-item>
        <el-form-item label="笔记标签">
          <el-tag
            v-for="(item, index) of note.tagNames"
            :key="index"
            style="margin: 0 1rem 0 0"
            :closable="true"
            @close="removeTag(item)">
            {{ item }}
          </el-tag>
          <el-popover placement="bottom-start" width="460" trigger="click" v-if="note.tagNames.length < 3">
            <div class="popover-title">标签</div>
            <el-autocomplete
              style="width: 100%"
              v-model="tagName"
              :fetch-suggestions="searchTags"
              placeholder="请输入标签名搜索，enter可添加自定义标签"
              :trigger-on-focus="false"
              @keyup.enter.native="saveTag"
              @select="handleSelectTag">
              <template slot-scope="{ item }">
                <div>{{ item.tagName }}</div>
              </template>
            </el-autocomplete>
            <div class="popover-container">
              <div style="margin-bottom: 1rem">添加标签</div>
              <el-tag v-for="(item, index) of tagList" :key="index" :class="tagClass(item)" @click="addTag(item)">
                {{ item.tagName }}
              </el-tag>
            </div>
            <el-button type="primary" plain slot="reference" size="small"> 添加标签</el-button>
          </el-popover>
        </el-form-item>
        <el-form-item label="笔记类型">
          <el-select v-model="note.type" placeholder="请选择类型">
            <el-option v-for="item in typeList" :key="item.type" :label="item.desc" :value="item.type" />
          </el-select>
        </el-form-item>
        <el-form-item label="原文地址" v-if="note.type != 1">
          <el-input v-model="note.originalUrl" placeholder="请填写原文链接" />
        </el-form-item>
        <el-form-item label="上传封面">
          <el-upload
            class="upload-cover"
            drag
            action="/api/admin/notes/images"
            multiple
            :headers="headers"
            :before-upload="beforeUpload"
            :on-success="uploadCover"
            :data="{ 'title': note.noteTitle }">
            <i class="el-icon-upload" v-if="note.noteCover == ''" />
            <div class="el-upload__text" v-if="note.noteCover == ''">将文件拖到此处，或<em>点击上传</em></div>
            <img v-else :src="note.noteCover" width="360px" height="180px" />
          </el-upload>
        </el-form-item>
        <el-form-item label="推荐">
          <el-switch
            v-model="note.isFeatured"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :active-value="1"
            :inactive-value="0" />
        </el-form-item>
        <el-form-item label="发布形式">
          <el-radio-group v-model="note.status">
            <el-radio :label="1">公开</el-radio>
            <el-radio :label="2">密码</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="访问密码" v-if="note.status == 2">
          <el-input v-model="note.password" placeholder="请填写笔记访问密码" />
        </el-form-item>
        <el-form-item label="说写什么">
          <el-input v-model="note.noteQuotes" :rows="2" type="textarea" placeholder="写上你此刻想说的话"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEdit = false">取 消</el-button>
        <el-button type="danger" @click="saveOrUpdateNote"> 发 表</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
import * as imageConversion from 'image-conversion'

export default {
  created() {
    const path = this.$route.path
    const arr = path.split('/')
    const noteId = arr[2]
    if (noteId) {
      this.axios.get('/api/admin/notes/' + noteId).then(({ data }) => {
        this.note = data.data
      })
    } else {
      const note = sessionStorage.getItem('note')
      if (note) {
        this.note = JSON.parse(note)
      }
    }
  },
  destroyed() {
    this.autoSaveNote()
  },
  data: function() {
    return {
      addOrEdit: false,
      autoSave: true,
      collectionName: '',
      tagName: '',
      collections: [],
      tagList: [],
      typeList: [
        {
          type: 1,
          desc: '原创'
        },
        {
          type: 2,
          desc: '转载'
        },
        {
          type: 3,
          desc: '翻译'
        }
      ],
      note: {
        id: null,
        noteTitle: this.$moment(new Date()).format('YYYY-MM-DD'),
        noteContent: '',
        noteCover: '',
        collectionName: null,
        tagNames: [],
        type: 1,
        status: 1,
        noteQuotes: ''
      },
      headers: { Authorization: 'Bearer ' + sessionStorage.getItem('token') }
    }
  },
  methods: {
    listCollections() {
      this.axios.get('/api/admin/collections/search').then(({ data }) => {
        this.collections = data.data
      })
    },
    listTags() {
      this.axios.get('/api/admin/tags/search').then(({ data }) => {
        this.tagList = data.data
      })
    },
    openModel() {
      if (this.note.noteTitle.trim() == '') {
        this.$message.error('笔记标题不能为空')
        return false
      }
      if (this.note.noteContent.trim() == '') {
        this.$message.error('笔记内容不能为空')
        return false
      }
      this.listCollections()
      this.listTags()
      this.addOrEdit = true
    },
    uploadCover(response) {
      this.note.noteCover = response.data
    },
    beforeUpload(file) {
      return new Promise((resolve) => {
        if (file.size / 1024 < this.config.UPLOAD_SIZE) {
          resolve(file)
        }
        imageConversion.compressAccurately(file, this.config.UPLOAD_SIZE).then((res) => {
          resolve(res)
        })
      })
    },
    uploadImg(pos, file) {
      var formdata = new FormData()
      if (file.size / 1024 < this.config.UPLOAD_SIZE) {
        formdata.append('file', file)
        formdata.append('title',this.note.noteTitle)
        console.log(formdata)
        this.axios.post('/api/admin/notes/images', formdata).then(({ data }) => {
          this.$refs.md.$img2Url(pos, data.data)
        })
      } else {
        // 压缩图片
        imageConversion.compressAccurately(file, this.config.UPLOAD_SIZE).then((res) => {
          formdata.append('file', new window.File([res], file.name, { type: file.type }))
          formdata.append('title',this.note.noteTitle)
          console.log(formdata)
          this.axios.post('/api/admin/notes/images', formdata).then(({ data }) => {
            this.$refs.md.$img2Url(pos, data.data)
          })
        })
      }
    },
    saveNoteDraft() {
      if (this.note.noteTitle.trim() == '') {
        this.$message.error('笔记标题不能为空')
        return false
      }
      if (this.note.noteContent.trim() == '') {
        this.$message.error('笔记内容不能为空')
        return false
      }
      this.note.status = 3
      this.axios.post('/api/admin/notes', this.note).then(({ data }) => {
        if (data.flag) {
          if (this.note.id === null) {
            this.$store.commit('removeTab', '发布笔记')
          } else {
            this.$store.commit('removeTab', '修改笔记')
          }
          sessionStorage.removeItem('note')
          this.$router.push({ path: '/note-list' })
          this.$notify.success({
            title: '成功',
            message: '保存草稿成功'
          })
        } else {
          this.$notify.error({
            title: '失败',
            message: '保存草稿失败'
          })
        }
      })
      this.autoSave = false
    },
    saveOrUpdateNote() {
      if (this.note.noteTitle.trim() == '') {
        this.$message.error('笔记标题不能为空')
        return false
      }
      if (this.note.noteContent.trim() == '') {
        this.$message.error('笔记内容不能为空')
        return false
      }
      if (this.note.collectionName == null) {
        this.$message.error('笔记分类不能为空')
        return false
      }
      if (this.note.tagNames.length == 0) {
        this.$message.error('笔记标签不能为空')
        return false
      }
      if (this.note.noteCover.trim() == '') {
        this.$message.error('笔记封面不能为空')
        return false
      }
      this.axios.post('/api/admin/notes', this.note).then(({ data }) => {
        if (data.flag) {
          if (this.note.id === null) {
            this.$store.commit('removeTab', '发布笔记')
          } else {
            this.$store.commit('removeTab', '修改笔记')
          }
          sessionStorage.removeItem('note')
          this.$router.push({ path: '/note-list' })
          this.$notify.success({
            title: '成功',
            message: data.message
          })
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.addOrEdit = false
      })
      this.autoSave = false
    },
    autoSaveNote() {
      if (
        this.autoSave &&
        this.note.noteTitle.trim() != '' &&
        this.note.noteContent.trim() != '' &&
        this.note.id != null
      ) {
        this.axios.post('/api/admin/notes', this.note).then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: '成功',
              message: '自动保存成功'
            })
          } else {
            this.$notify.error({
              title: '失败',
              message: '自动保存失败'
            })
          }
        })
      }
      if (this.autoSave && this.note.id == null) {
        sessionStorage.setItem('note', JSON.stringify(this.note))
      }
    },
    searchCollection(keywords, cb) {
      this.axios
        .get('/api/admin/collections/search', {
          params: {
            keywords: keywords
          }
        })
        .then(({ data }) => {
          cb(data.data)
        })
    },
    handleSelectCollections(item) {
      this.addCollection({
        collectionName: item.collectionName
      })
    },
    saveCollection() {
      if (this.collectionName.trim() != '') {
        this.addCollection({
          collectionName: this.collectionName
        })
        this.collectionName = ''
      }
    },
    addCollection(item) {
      this.note.collectionName = item.collectionName
    },
    removeCollection() {
      this.note.collectionName = null
    },
    searchTags(keywords, cb) {
      this.axios
        .get('/api/admin/tags/search', {
          params: {
            keywords: keywords
          }
        })
        .then(({ data }) => {
          cb(data.data)
        })
    },
    handleSelectTag(item) {
      this.addTag({
        tagName: item.tagName
      })
    },
    saveTag() {
      if (this.tagName.trim() != '') {
        this.addTag({
          tagName: this.tagName
        })
        this.tagName = ''
      }
    },
    addTag(item) {
      if (this.note.tagNames.indexOf(item.tagName) == -1) {
        this.note.tagNames.push(item.tagName)
      }
    },
    removeTag(item) {
      const index = this.note.tagNames.indexOf(item)
      this.note.tagNames.splice(index, 1)
    }
  },
  computed: {
    tagClass() {
      return function(item) {
        const index = this.note.tagNames.indexOf(item.tagName)
        return index != -1 ? 'tag-item-select' : 'tag-item'
      }
    }
  }
}
</script>

<style scoped>
.article-title-container {
  display: flex;
  align-items: center;
  margin-bottom: 1.25rem;
  margin-top: 2.25rem;
}

.save-btn {
  margin-left: 0.75rem;
  background: #fff;
  color: #f56c6c;
}

.tag-item {
  margin-right: 1rem;
  margin-bottom: 1rem;
  cursor: pointer;
}

.tag-item-select {
  margin-right: 1rem;
  margin-bottom: 1rem;
  cursor: not-allowed;
  color: #ccccd8 !important;
}

.category-item {
  cursor: pointer;
  padding: 0.6rem 0.5rem;
}

.category-item:hover {
  background-color: #f0f9eb;
  color: #67c23a;
}

.popover-title {
  margin-bottom: 1rem;
  text-align: center;
}

.popover-container {
  margin-top: 1rem;
  height: 260px;
  overflow-y: auto;
}
</style>
