<template>
  <el-card>
    <el-row>
      <el-col :sm="10" style="text-align: center;">
        <el-row>
          <el-col :span="24">
            <el-avatar class="avatar" @error="errorHandler" shape="square" :size="180" :src="user.avatar"/>
          </el-col>
          <el-col :span="24" v-if="sharedStatus.user.id == user.id">
            <el-button type="button" style="width: 180px;" v-on:click="editProfile">编辑信息</el-button>
          </el-col>
        </el-row>
      </el-col>
      <el-col :sm="14" class="primary_text">
        <p>用户名:<span class="small_text">{{user.username}}</span></p>
        <p>昵称:<span class="small_text" v-text="user.name ? user.name : '未设定昵称'"/></p>
        <p>邮箱:<span class="small_text" v-text="user.email ? user.email : '未绑定邮箱'"/></p>
        <p>创建于:<span class="small_text" v-text="$moment(user.created).format('lll')"/></p>
        <p>上次登录:<span class="small_text" v-text="$moment(user.lastLogin).format('lll')"/></p>
      </el-col>
    </el-row>
  </el-card>

</template>

<script>
  import store from '../store.js'
  export default{
    data(){
      return{
        sharedStatus: store.state,
        user:'',
      }
    },
    methods:{
      getUser(){
        const path = `/users/${this.$route.params.id}`
        this.$axios.get(path)
          .then((res) => {
            this.user = res.data.data
          })
          .catch((error) => {
            console.error(error)
          })
      },
      errorHandler(){
        return true
      },
      editProfile(){
        this.$router.push({path:`/user/${this.$route.params.id}/edit-profile`})
      }
    },
    created() {
      this.getUser()
    },
    // 当路由变化后(比如变更查询参数 page 和 per_page)重新加载数据
    beforeRouteUpdate (to, from, next) {
      next()
      this.getUser()
    }
  }
</script>

<style>
  .primary_text{
    font-size: 14px;
    color: #303133;
  }
  .avatar{
    border: 1px solid #d9d9d9;
    border-radius: 6px;
  }
  .small_text{
    font-size: 13px;
    color: #909399;
  }
</style>
