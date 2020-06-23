<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="文章标题">
        <el-input v-model="form.title" />
      </el-form-item>
      <el-form-item label="文章标签">
        <el-select v-model="form.tags" multiple placeholder="请选择文章分类标签" style="width: 100%;">
          <el-option v-for="(tag, index) in tags" :key="index" :label="tag.label" :value="tag.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="文章摘要">
        <el-input v-model="form.summary" />
      </el-form-item>
      <el-form-item label="文章内容">
        <Markdown v-model="form.body"></Markdown>
      </el-form-item>
      <el-form-item>
        <el-button size="mini" type="primary" @click="onSubmit">添加</el-button>
        <el-button size="mini" type="warning" @click="onCancel">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getTags } from '@/api/tag'
import { createPost, addTagToPost } from '@/api/post'
import Markdown from 'vue-meditor'
export default {
  data() {
    return {
      form: {
        title: '',
        tags: '',
        summary: '',
        body: ''
      },
      tags:[]
    }
  },
  components:{
    Markdown
  },
  methods: {
    fetchData() {
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
    onSubmit() {
      var data = {
        title: this.form.title,
        summary: this.form.summary,
        body: this.form.body
      }
      createPost(data).then(response => {
        addTagToPost(response.data, this.form.tags).then(response => {
          this.$message({
            message: response.data,
            type: 'success'
          })
        })
      })
    },
    onCancel() {
      this.$message({
        message: '重置!',
        type: 'warning'
      })
    }
  },
  created() {
    this.fetchData()
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}
</style>
