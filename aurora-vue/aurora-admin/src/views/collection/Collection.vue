<template>
  <el-card class="main-card">
    <div class="title">{{ this.$route.name }}</div>
    <div class="operation-container">
      <el-button type="primary" size="small" icon="el-icon-plus" @click="openModel(null)"> 新增 </el-button>
      <el-button
        type="danger"
        size="small"
        icon="el-icon-delete"
        :disabled="this.collectionIds.length == 0"
        @click="isDelete = true">
        批量删除
      </el-button>
      <div style="margin-left: auto">
        <el-input
          v-model="keywords"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入合集名"
          style="width: 200px"
          @keyup.enter.native="searchCollections" />
        <el-button
          type="primary"
          size="small"
          icon="el-icon-search"
          style="margin-left: 1rem"
          @click="searchCollections">
          搜索
        </el-button>
      </div>
    </div>
    <el-table border :data="collections" @selection-change="selectionChange" v-loading="loading">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="collectionName" label="合集名" align="center" />
      <el-table-column prop="noteCount" label="笔记量" align="center" />
      <el-table-column prop="createTime" label="创建时间" align="center">
        <template slot-scope="scope">
          <i class="el-icon-time" style="margin-right: 5px" />
          {{ scope.row.createTime | date }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openModel(scope.row)"> 编辑 </el-button>
          <el-popconfirm title="确定删除吗？" style="margin-left: 1rem" @confirm="deleteCategory(scope.row.id)">
            <el-button size="mini" type="danger" slot="reference"> 删除 </el-button>
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
    <el-dialog :visible.sync="isDelete" width="30%">
      <div class="dialog-title-container" slot="title"><i class="el-icon-warning" style="color: #ff9900" />提示</div>
      <div style="font-size: 1rem">是否删除选中项？</div>
      <div slot="footer">
        <el-button @click="isDelete = false">取 消</el-button>
        <el-button type="primary" @click="deleteCategory(null)"> 确 定 </el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="addOrEdit" width="30%">
      <div class="dialog-title-container" slot="title" ref="categoryTitle" />
      <el-form label-width="80px" size="medium" :model="collectionForm">
        <el-form-item label="合集名">
          <el-input v-model="collectionForm.collectionName" style="width: 220px" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="addOrEdit = false">取 消</el-button>
        <el-button type="primary" @click="addOrEditCategory"> 确 定 </el-button>
      </div>
    </el-dialog>
  </el-card>
</template>

<script>
export default {
  created() {
    this.current = this.$store.state.pageState.category
    this.listCollections()
  },
  data: function () {
    return {
      isDelete: false,
      loading: true,
      addOrEdit: false,
      keywords: null,
      collectionIds: [],
      collections: [],
      collectionForm: {
        id: null,
        collectionName: ''
      },
      current: 1,
      size: 10,
      count: 0
    }
  },
  methods: {
    selectionChange(categories) {
      this.collectionIds = []
      categories.forEach((item) => {
        this.collectionIds.push(item.id)
      })
    },
    searchCollections() {
      this.current = 1
      this.listCollections()
    },
    sizeChange(size) {
      this.size = size
      this.listCollections()
    },
    currentChange(current) {
      this.current = current
      this.$store.commit('updateCategoryPageState', current)
      this.listCollections()
    },
    deleteCategory(id) {
      let param = {}
      if (id == null) {
        param = { data: this.collectionIds }
      } else {
        param = { data: [id] }
      }
      this.axios.delete('/api/admin/collections', param).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listCollections()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.isDelete = false
      })
    },
    listCollections() {
      this.axios
        .get('/api/admin/collections', {
          params: {
            current: this.current,
            size: this.size,
            keywords: this.keywords
          }
        })
        .then(({ data }) => {
          this.collections = data.data.records
          this.count = data.data.count
          this.loading = false
        })
    },
    openModel(category) {
      if (category != null) {
        this.collectionForm = JSON.parse(JSON.stringify(category))
        this.$refs.categoryTitle.innerHTML = '修改合集'
      } else {
        this.collectionForm.id = null
        this.collectionForm.collectionName = ''
        this.$refs.categoryTitle.innerHTML = '添加合集'
      }
      this.addOrEdit = true
    },
    addOrEditCategory() {
      if (this.collectionForm.collectionName.trim() == '') {
        this.$message.error('合集名不能为空')
        return false
      }
      this.axios.post('/api/admin/collections', this.collectionForm).then(({ data }) => {
        if (data.flag) {
          this.$notify.success({
            title: '成功',
            message: data.message
          })
          this.listCollections()
        } else {
          this.$notify.error({
            title: '失败',
            message: data.message
          })
        }
        this.addOrEdit = false
      })
    }
  }
}
</script>
