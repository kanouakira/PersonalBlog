<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading"
      :data="users"
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
      <el-table-column label="用户名" width="110">
        <template slot-scope="scope">
          {{ scope.row.username }}
        </template>
      </el-table-column>
      <el-table-column label="昵称" width="110">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="邮箱">
        <template slot-scope="scope">
          {{ scope.row.email }}
        </template>
      </el-table-column>
      <el-table-column label="权限" width="110" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ scope.row.roleName}}</el-tag>
          <!-- <span>{{ scope.row.status }}</span> -->
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="220">
        <template slot-scope="scope">
          <div v-if="scope.row.status < status" >
            <el-button size="mini" type="warning" @click="handleEdit(scope.row)">修改权限</el-button>
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </div>
          <div v-else>
            无操作权限
          </div>
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

    <!-- 修改框 -->
    <el-dialog title="修改用户权限" :visible.sync="editVisible" width="300px" center>
      <div class="del-dialog-cnt">
        <el-form ref="editUserForm" :model="editUserForm" label-width="80px">
          <el-form-item label="用户权限">
            <el-select v-model="editUserForm.status" placeholder="请选择权限" style="width: 100%;">
              <el-option v-for="(role, index) in roles" v-if="role.id <= status" :key="index" :label="role.roleName" :value="role.id" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="mini" @click="editVisible = false">取 消</el-button>
        <el-button size="mini" type="primary" @click="editUser">确 定</el-button>
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
import { getUsers, updateUser, deleteUser } from '@/api/user'
import { getRoles } from '@/api/role'
import { Message } from 'element-ui'
import { mapGetters } from 'vuex'

export default {
  computed: {
    ...mapGetters([
      'name',
      'token',
      'avatar',
      'role',
      'status'
    ])
  },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'info',
        2: 'primary',
        3: 'success',
        4: 'warning',
        5: 'danger',
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      users: null,
      listLoading: true,
      editVisible: false,
      edit_id: null,
      delVisible: false,
      delete_id: null,
      currentPage: 1,
      pageSize: 10,
      total: 100,
      editUserForm:{
        status: null
      },
      roles: []
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
      getUsers(payload).then(response => {
        this.total = response.data.total
        this.pageSize = response.data.per_page
        this.users = response.data.data
        this.listLoading = false
      })
      getRoles().then(response => {
        this.roles = response.data
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
    handleEdit(data){
      this.editVisible = true
      this.edit_id = data.id
    },
    handleDelete(data) {
      this.delVisible = true
      this.delete_id = data.id
    },
    editUser() {
      updateUser(this.edit_id, this.editUserForm).then(response => {
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
      deleteUser(this.delete_id).then(response => {
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
