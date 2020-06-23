<template>
  <el-row>
    <el-col :span="18" :offset="3">
      <el-form ref="loginForm" :model="loginForm" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input id="username" v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password"></el-input>
        </el-form-item>
        <el-form-item size="mini">
          <el-button type="primary" @click="onSubmitLogin('loginForm')">登录</el-button>
          <el-button @click="onResetLogin('loginForm')">清空</el-button>
        </el-form-item>
        <el-form-item>
          <p>新用户? <router-link to="/register" style="color: lightgreen">点击注册!</router-link></p>
          <p>
              忘记密码?
              <a href="#" style="color: lightgreen">点击重置</a>
          </p>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<script>
import store from '../store.js'
export default {
  name: 'Login',
  data () {
    return {
      sharedState: store.state,
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
        ]
      }
    }
  },
  methods: {
    // 登录
    onSubmitLogin(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let payload = {
            'username': this.loginForm.username,
            'password': this.loginForm.password
          }
          const path = `/tokens`
          this.$axios.post(path,payload)
            .then((res) => {
              if(res.data.status == 200){
                this.$message({
                  message: '登录成功',
                  type: 'success'
                });
                console.log(res.data.data)
                window.localStorage.setItem('vueblog-token', res.data.data.token)
                window.localStorage.setItem('vueblog-user', JSON.stringify(res.data.data.user))
                store.loginAction()
                if (typeof this.$route.query.redirect == 'undefined') {
                  this.$router.push('/')
                } else {
                  this.$router.push(this.$route.query.redirect)
                }
              }else{
                this.$message.error(res.data.message);
              }
            })
            .catch((error) => {
              console.log(error)
            })
        } else {
          return false;
        }
      });
    },
    onResetLogin(formName){
      this.$refs[formName].resetFields();
    }
  },
  created() {
    $(document).ready(function() {
      $('#username').focus()
    })
  }
}
</script>
