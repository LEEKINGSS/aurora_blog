<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="article-status-menu">
      <span>状态</span>
      <span @click="changeStatus('all')" :class="isActive('all')">全部</span>
      <span @click="changeStatus('public')" :class="isActive('public')"> 公开 </span>
      <span @click="changeStatus('private')" :class="isActive('private')"> 私密 </span>
      <span @click="changeStatus('draft')" :class="isActive('draft')"> 草稿箱 </span>
      <span @click="changeStatus('delete')" :class="isActive('delete')"> 回收站 </span>
    </div>
    <div class="operation-container">
      <el-button
        v-if="isDelete == 0"
        type="danger"
        size="small"
        icon="el-icon-delete"
        :disabled="noteIds.length == 0"
        @click="updateIsDelete = true">
        批量删除
      </el-button>
      <el-button
        v-else
        type="danger"
        size="small"
        icon="el-icon-delete"
        :disabled="noteIds.length == 0"
        @click="remove = true">
        批量删除
      </el-button>
      <el-button
        type="success"
        size="small"
        icon="el-icon-download"
        :disabled="noteIds.length == 0"
        style="margin-right: 1rem"
        @click="isExport = true">
        批量导出
      </el-button>
      <el-upload
        action="/api/admin/notes/import"
        multiple
        :limit="9"
        :show-file-list="false"
        :headers="uploadHeaders"
        :on-success="uploadNote"
        :data="{'type': note}">
        <el-button type="primary" size="small" icon="el-icon-upload"> 批量导入</el-button>
      </el-upload>
      <div style="margin-left: auto">
        <el-select
          clearable
          v-model="type"
          placeholder="请选择笔记类型"
          size="small"
          style="margin-right: 1rem; width: 180px">
          <el-option label="全部" value="" />
          <el-option v-for="item in types" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select
          clearable
          size="small"
          v-model="collectionId"
          filterable
          placeholder="请选择合集"
          style="margin-right: 1rem; width: 180px">
          <el-option label="全部" value="" />
          <el-option v-for="item in collections" :key="item.id" :label="item.collectionName" :value="item.id" />
        </el-select>
        <el-select
          clearable
          size="small"
          v-model="tagId"
          filterable
          placeholder="请选择标签"
          style="margin-right: 1rem; width: 180px">
          <el-option label="全部" value="" />
          <el-option v-for="item in tags" :key="item.id" :label="item.tagName" :value="item.id" />
        </el-select>
        <el-input
          clearable
          v-model="keywords"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入笔记名"
          style="width: 200px"
          @keyup.enter.native="searchNotes" />
        <el-button type="primary" size="small" icon="el-icon-search" style="margin-left: 1rem" @click="searchNotes">
          搜索
        </el-button>
      </div>
    </div>
    <el-table border :data="notes" @selection-change="selectionChange" v-loading="loading">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="noteCover" label="笔记封面" width="180" align="center">
        <template slot-scope="scope">
          <el-image
            class="article-cover"
            :src="
              scope.row.noteCover
                ? scope.row.noteCover
                : 'https://liking.oss-cn-wulanchabu.aliyuncs.com/aurora/404/4c480d324fedddd4e55ae2438251871.jpg'
            " />
          <i v-if="scope.row.status == 1" class="iconfont el-icon-mygongkai article-status-icon" />
          <i v-if="scope.row.status == 2" class="iconfont el-icon-mymima article-status-icon" />
          <i v-if="scope.row.status == 3" class="iconfont el-icon-mycaogaoxiang article-status-icon" />
        </template>
      </el-table-column>
      <el-table-column prop="noteTitle" label="标题" align="center" />
      <el-table-column prop="collectionName" label="合集" width="110" align="center" />
      <el-table-column prop="tagDTOs" label="标签" width="170" align="center">
        <template slot-scope="scope">
          <el-tag v-for="item of scope.row.tagDTOs" :key="item.tagId" style="margin-right: 0.2rem; margin-top: 0.2rem">
            {{ item.tagName }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="viewsCount" label="浏览量" width="70" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.viewsCount">
            {{ scope.row.viewsCount }}
          </span>
          <span v-else>0</span>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="类型" width="80" align="center">
        <template slot-scope="scope">
          <el-tag :type="noteType(scope.row.type).tagType">
            {{ noteType(scope.row.type).name }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="发表时间" width="130" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column prop="isFeatured" label="推荐" width="80" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isFeatured"
            active-color="#13ce66"
            inactive-color="#F4F4F5"
            :disabled="scope.row.isDelete == 1"
            :active-value="1"
            :inactive-value="0"
            @change="changeFeatured(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="editNote(scope.row.id)" v-if="scope.row.isDelete == 0">
            编辑
          </el-button>
          <el-popconfirm
            title="确定删除吗？"
            style="margin-left: 10px"
            @confirm="updateNoteDelete(scope.row.id)"
            v-if="scope.row.isDelete == 0">
            <el-button size="mini" type="danger" slot="reference"> 删除</el-button>
          </el-popconfirm>
          <el-popconfirm
            title="确定恢复吗？"
            v-if="scope.row.isDelete == 1"
            @confirm="updateNoteDelete(scope.row.id)">
            <el-button size="mini" type="success" slot="reference"> 恢复</el-button>
          </el-popconfirm>
          <el-popconfirm
            style="margin-left: 10px"
            v-if="scope.row.isDelete == 1"
            title="确定彻底删除吗？"
            @confirm="deleteNotes(scope.row.id)">
            <el-button size="mini" type="danger" slot="reference"> 删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="pagination-container"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
      :current-page="current"
      :page-size="size"
      :total="count"
      :page-sizes="[10, 20]"
      layout="total, sizes, prev, pager, next, jumper" />
    <el-dialog :visible.sync="updateIsDelete" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900" />提示</div>
      <div style="font-size: 1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="updateIsDelete = false">取 消</el-button>
        <el-button type="primary" @click="updateNoteDelete(null)"> 确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="remove" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900" />提示</div>
      <div style="font-size: 1rem">是否彻底删除选中项？</div>
      <div slot="footer">
        <el-button @click="remove = false">取 消</el-button>
        <el-button type="primary" @click="deleteNotes(null)"> 确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="isExport" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900" />提示</div>
      <div style="font-size: 1rem">是否导出选中笔记？</div>
      <div slot="footer">
        <el-button @click="isExport = false">取 消</el-button>
        <el-button type="primary" @click="exportNotes(null)"> 确 定</el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.current = this.$store.state.pageState.noteList
    this.listNotes()
    this.listCollections()
    this.listTags()
  },
  data: function() {
    return {
      uploadHeaders: { Authorization: 'Bearer ' + sessionStorage.getItem('token') },
      loading: true,
      updateIsDelete: false,
      remove: false,
      note: 'note',
      types: [
        {
          value: 1,
          label: '原创'
        },
        {
          value: 2,
          label: '转载'
        },
        {
          value: 3,
          label: '翻译'
        }
      ],
      activeStatus: 'all',
      notes: [],
      noteIds: [],
      collections: [],
      tags: [],
      keywords: null,
      type: null,
      collectionId: null,
      tagId: null,
      isDelete: 0,
      isExport: false,
      status: null,
      current: 1,
      size: 10,
      count: 0
    }
  },
  methods: {
    selectionChange(notes) {
      this.noteIds = []
      notes.forEach((item) => {
        this.noteIds.push(item.id)
      })
    },
    searchNotes() {
      this.current = 1
      this.listNotes()
    },
    editNote(id) {
      this.$router.push({ path: '/notes/' + id })
    },
    updateNoteDelete(id) {
      let param = {}
      if (id != null) {
        param.ids = [id]
      } else {
        param.ids = this.noteIds
      }
      param.isDelete = this.isDelete == 0 ? 1 : 0
      this.axios.put('/api/admin/notes', param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listNotes()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.updateIsDelete = false
      })
    },
    deleteNotes(id) {
      let param = {}
      if (id == null) {
        param = { data: this.noteIds }
      } else {
        param = { data: [id] }
      }
      this.axios.delete('/api/admin/notes/delete', param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listNotes()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.remove = false
      })
    },
    exportNotes(id) {
      var param = {}
      if (id == null) {
        param = this.noteIds
      } else {
        param = [id]
      }
      this.axios.post('/api/admin/notes/export', param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          data.data.forEach((item) => {
            this.downloadFile(item)
          })
          this.listNotes()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.isExport = false
      })
    },
    downloadFile(url) {
      const iframe = document.createElement('iframe')
      iframe.style.display = 'none'
      iframe.style.height = 0
      iframe.src = url
      document.body.appendChild(iframe)
      setTimeout(() => {
        iframe.remove()
      }, 5 * 60 * 1000)
    },
    uploadNote(data) {
      if (data.flag) {
        this.$notify.success({
          title: '成功',
          message: '导入成功'
        })
        this.listNotes()
      } else {
        this.$notify.error({
          title: '失败',
          message: data.message
        })
      }
    },
    sizeChange(size) {
      this.size = size
      this.listNotes()
    },
    currentChange(current) {
      this.current = current
      this.$store.commit('updateArticleListPageState', current)
      this.listNotes()
    },
    changeStatus(status) {
      switch (status) {
        case 'all':
          this.isDelete = 0
          this.status = null
          break
        case 'public':
          this.isDelete = 0
          this.status = 1
          break
        case 'private':
          this.isDelete = 0
          this.status = 2
          break
        case 'draft':
          this.isDelete = 0
          this.status = 3
          break
        case 'delete':
          this.isDelete = 1
          this.status = null
          break
      }
      this.current = 1
      this.activeStatus = status
    },
    changeFeatured(note) {
      this.axios
        .put('/api/admin/notes/featured', {
          id: note.id,
          isFeatured: note.isFeatured
        })
        .then(({ data }) => {
          if (data.flag) {
            this.$notify.success({
              title: '成功',
              message: '修改成功'
            })
          } else {
            this.$notify.error({
              title: '失败',
              message: data.message
            })
          }
          this.remove = false
        })
    },
    listNotes() {
      this.axios
        .get('/api/admin/notes', {
          params: {
            current: this.current,
            size: this.size,
            keywords: this.keywords,
            collectionId: this.collectionId,
            status: this.status,
            tagId: this.tagId,
            type: this.type,
            isDelete: this.isDelete
          }
        })
        .then(({ data }) => {
          this.notes = data.data.records
          this.count = data.data.count
          this.loading = false
        })
    },
    listCollections() {
      this.axios.get('/api/admin/collections/search').then(({ data }) => {
        this.collections = data.data
      })
    },
    listTags() {
      this.axios.get('/api/admin/tags/search').then(({ data }) => {
        this.tags = data.data
      })
    }
  },
  watch: {
    type() {
      this.current = 1
      this.listNotes()
    },
    categoryId() {
      this.current = 1
      this.listNotes()
    },
    tagId() {
      this.current = 1
      this.listNotes()
    },
    status() {
      this.current = 1
      this.listNotes()
    },
    isDelete() {
      this.current = 1
      this.listNotes()
    }
  },
  computed: {
    noteType() {
      return function(type) {
        var tagType = ''
        var name = ''
        switch (type) {
          case 1:
            tagType = 'danger'
            name = '原创'
            break
          case 2:
            tagType = 'success'
            name = '转载'
            break
          case 3:
            tagType = 'primary'
            name = '翻译'
            break
        }
        return {
          tagType: tagType,
          name: name
        }
      }
    },
    isActive() {
      return function(status) {
        return this.activeStatus == status ? 'active-status' : 'status'
      }
    }
  }
}
</script>

<style scoped>
.operation-container {
  margin-top: 1.5rem;
}

.article-status-menu {
  font-size: 14px;
  margin-top: 40px;
  color: #999;
}

.article-status-menu span {
  margin-right: 24px;
}

.status {
  cursor: pointer;
}

.active-status {
  cursor: pointer;
  color: #333;
  font-weight: bold;
}

.article-cover {
  position: relative;
  width: 100%;
  height: 90px;
  border-radius: 4px;
}

.article-cover::after {
  content: '';
  background: rgba(0, 0, 0, 0.3);
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
}

.article-status-icon {
  color: #fff;
  font-size: 1.5rem;
  position: absolute;
  right: 1rem;
  bottom: 1.4rem;
}
</style>
