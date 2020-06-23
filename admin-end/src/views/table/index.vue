<template>
  <div class="app-container">
    <el-table
      v-loading="listLoading"
      :data="posts"
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
      <el-table-column label="标题">
        <template slot-scope="scope">
          {{ scope.row.title }}
        </template>
      </el-table-column>
      <el-table-column label="作者" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.user.name ? scope.row.user.name : scope.row.user.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="摘要" align="center">
        <template slot-scope="scope">
          {{ scope.row.summary }}
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="标签" width="180" align="center">
        <template slot-scope="scope">
          <el-tag v-for="(tag, index) in scope.row.tags" :key="index" style="margin: 2px;">
            {{ tag.name }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" width="200">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ $moment(scope.row.created).format('lll') }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="300">
        <template slot-scope="scope">
          <el-button size="mini" type="success" icon="el-icon-view" @click="handleRead(scope.row)">查看</el-button>
          <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
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

    <!-- 文章阅读框 -->
    <el-dialog title="" :visible.sync="readVisible" center>
      <span slot="title">
        <h2>文章内容</h2>
        <el-switch
          v-model="editBody"
          active-text="编辑"
          inactive-text="阅读">
        </el-switch>
      </span>
      <MarkdownPreview v-if="!editBody" :initialValue="readPost.body"/>
      <div v-else>
        <el-form ref="readPost" :model="readPost" label-width="120px">
          <el-form-item label="文章标题">
            <el-input v-model="readPost.title" />
          </el-form-item>
          <el-form-item label="文章标签">
            <el-select v-model="selected_tags" multiple placeholder="请选择文章分类标签" style="width: 100%;">
              <el-option v-for="(tag, index) in tags" :key="index" :label="tag.label" :value="tag.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="文章摘要">
            <el-input v-model="readPost.summary" />
          </el-form-item>
          <el-form-item label="文章内容">
            <Markdown v-model="readPost.body"/>
          </el-form-item>
          <el-form-item>
            <el-button size="mini" type="primary" @click="editPost">确认修改</el-button>
          </el-form-item>
        </el-form>
      </div>
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
import { getPosts, deletePost, updatePost, getPostTags, updateTagToPost } from '@/api/post'
import { getTags } from '@/api/tag'
import { Message } from 'element-ui'
import Markdown from 'vue-meditor'
import { MarkdownPreview } from 'vue-meditor'

export default {
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      posts: null,
      listLoading: true,
      delVisible: false,
      readVisible: false,
      editBody: false,
      delete_id: null,
      currentPage: 1,
      pageSize: 10,
      total: 100,
      readPost: {
        title: '',
        summary: '',
        tags: '',
        body: ''
      },
      tags: [],
      selected_tags: []
    }
  },
  components:{
    Markdown,
    MarkdownPreview,
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
      getPosts(payload).then(response => {
        this.total = response.data.total
        this.pageSize = response.data.per_page
        this.posts = response.data.data
        this.listLoading = false
      })

      getTags().then(response => {
        this.tags = []
        for(var i=0; i<response.data.length; i++){
          this.tags.push({
            value: response.data[i].id,
            label: response.data[i].name
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
    handleDelete(rowId, data) {
      this.delVisible = true
      this.delete_id = data.id
    },
    handleRead(post) {
      this.readVisible = true
      this.readPost = JSON.parse(JSON.stringify(post))
      getPostTags(post.id).then(response => {
        // console.log(response)
        this.selected_tags = []
        for(var i=0;i<response.data.length;i++){
          this.selected_tags.push(response.data[i])
        }
      })
    },
    editPost() {
      const data = {
        title: this.readPost.title,
        summary: this.readPost.summary,
        body: this.readPost.body
      }
      updatePost(this.readPost.id, data).then(response => {
        updateTagToPost(this.readPost.id, this.selected_tags).then(response => {
          Message({
            message: response.message,
            type: 'success',
            duration: 5 * 1000
          })
          this.readVisible = false
          this.editBody = false
          this.fetchData()
        })
      })
    },
    deleteRow() {
      deletePost(this.delete_id).then(response => {
        Message({
          message: response.message,
          type: 'success',
          duration: 5 * 1000
        })
        this.delVisible = false
        this.fetchData()
      })
    }
  }
}
</script>
