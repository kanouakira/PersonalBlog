<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading"
      :data="comments"
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
      <el-table-column label="评论内容">
        <template slot-scope="scope">
          {{ scope.row.body }}
        </template>
      </el-table-column>
      <el-table-column label="评论状态" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ scope.row.status | statusInfo }}</el-tag>
          <!-- <span>{{ scope.row.status }}</span> -->
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="300">
        <template slot-scope="scope">
          <el-button v-if="scope.row.status == 0" size="mini" type="warning" @click="handleEdit(scope.row, 1)">屏蔽</el-button>
          <el-button v-if="scope.row.status == 1" size="mini" type="success" @click="handleEdit(scope.row, 0)">显示</el-button>
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
import { getComments, updateComment, deleteComment } from '@/api/comment'
import { Message } from 'element-ui'

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        0: 'success',
        1: 'warning'
      }
      return statusMap[status]
    },
    statusInfo(status){
      const statusMap = {
        0: "正常",
        1: '屏蔽'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      comments: null,
      listLoading: true,
      delVisible: false,
      delete_id: null,
      currentPage: 1,
      pageSize: 10,
      total: 100,
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
      getComments(payload).then(response => {
        this.total = response.data.total
        this.pageSize = response.data.per_page
        this.comments = response.data.data
        this.listLoading = false
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
    handleEdit(comment, status){
      const data = {
        status: status
      }
      updateComment(comment.id, data).then(response => {
        Message({
          message: response.message,
          type: 'success',
          duration: 5 * 1000
        })
        this.fetchData()
      })
    },
    handleDelete(data) {
      this.delVisible = true
      this.delete_id = data.id
    },
    deleteRow() {
      deleteComment(this.delete_id).then(response => {
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
