<template>
  <div class="app-container">
    <el-button icon="el-icon-plus" size="mini" type="primary" @click="handleCreate">新增标签</el-button>
    <el-divider></el-divider>
    <el-table
      v-loading="listLoading"
      :data="tags"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">
          {{ scope.$index }}
        </template>
      </el-table-column>
      <el-table-column label="标签名">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="所属分类" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.category }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="300">
        <template slot-scope="scope">
          <el-button size="mini" type="success" icon="el-icon-edit" @click="handleEdit(scope.row)">修改</el-button>
          <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage"
      :page-sizes="[5, 10, 15, 20]"
      :page-size="pageSize"
      layout="total,sizes, prev, pager, next"
      :total="total"
      style="text-align: center;">
    </el-pagination>

    <!-- 新增标签框 -->
    <el-dialog title="新增标签" :visible.sync="createVisible" width="300px" center>
      <div class="del-dialog-cnt">
        <el-form ref="tagForm" :model="tagForm" label-width="80px">
          <el-form-item label="标签名">
            <el-input v-model="tagForm.name" />
          </el-form-item>
          <el-form-item label="所属分类">
            <el-select v-model="tagForm.categoryId" placeholder="请选择标签分类" style="width: 100%;">
              <el-option v-for="(category, index) in categories" :key="index" :label="category.label" :value="category.value" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="createVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="createNewTag">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 修改标签框 -->
    <el-dialog title="修改标签" :visible.sync="editVisible" width="300px" center>
      <div class="del-dialog-cnt">
        <el-form ref="editTagForm" :model="editTagForm" label-width="80px">
          <el-form-item label="所属分类">
            <el-select v-model="editTagForm.categoryId" placeholder="请选择标签分类" style="width: 100%;">
              <el-option v-for="(category, index) in categories" :key="index" :label="category.label" :value="category.value" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="editVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="editTag">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 删除提示框 -->
    <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
      <div class="del-dialog-cnt">删除不可恢复，是否确定删除？</div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="delVisible = false">取 消</el-button>
        <el-button type="primary" @click="deleteRow">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { getTags, deleteTag, createTag, updateTag } from '@/api/tag'
import { getCategories } from '@/api/category'
import { Message } from 'element-ui'

export default {
  data() {
    return {
      tags: null,
      listLoading: true,
      createVisible: false,
      delVisible: false,
      editVisible: false,
      delete_id: null,
      edit_id: null,
      currentPage: 1,
      pageSize: 10,
      total: 100,
      tagForm: {
        name: '',
        categoryId: 1
      },
      editTagForm: {
        categoryId: 1
      },
      categories: []
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      const payload = {
        page: this.currentPage,
        per_page: this.pageSize
      }
      getTags(payload).then(response => {
        this.total = response.data.total
        this.pageSize = response.data.per_page
        this.tags = response.data.data
        this.listLoading = false
      })
      getCategories().then(response => {
        this.categories = []
        for(var i=0;i<response.data.length;i++){
          this.categories.push({
            label: response.data[i].category,
            value: response.data[i].id
          })
        }
      })
    },
    handleSizeChange(val) {
      // console.log(`每页 ${val} 条`);
      this.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      // console.log(`当前页: ${val}`);
      this.currentPage = val
      this.fetchData()
    },
    handleEdit(data) {
      this.editVisible = true
      this.edit_id = data.id
      this.editTagForm.categoryId = data.categoryId
    },
    handleDelete(data) {
      this.delVisible = true
      this.delete_id = data.id
    },
    handleCreate() {
      this.createVisible = true
    },
    createNewTag() {
      createTag(this.tagForm).then(response => {
        Message({
          message: response.message,
          type: 'success',
          duration: 5 * 1000
        })
        this.fetchData()
        this.createVisible = false
      })
    },
    editTag() {
      updateTag(this.edit_id, this.editTagForm).then(response => {
        Message({
          message: response.message,
          type: 'success',
          duration: 5 * 1000
        })
        this.fetchData()
        this.editVisible = false
      })
    },
    deleteRow() {
      deleteTag(this.delete_id).then(response => {
        Message({
          message: response.message,
          type: 'success',
          duration: 5 * 1000
        })
        this.fetchData()
        this.delVisible = false
      })
    }
  }
}
</script>
