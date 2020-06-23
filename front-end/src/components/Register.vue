<template>
  <el-row>
    <el-col :span="18" :offset="3">
      <el-form ref="registerForm" :model="registerForm" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input id="username" v-model="registerForm.username"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input type="password" v-model="registerForm.password"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="registerForm.checkPass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email"></el-input>
        </el-form-item>
        <el-form-item size="mini">
          <el-button type="primary" @click="onSubmitRegister('registerForm')">注册</el-button>
          <el-button @click="onResetRegister('registerForm')">清空</el-button>
        </el-form-item>
        <el-form-item>
          <p>已经注册? <router-link to="/login" style="color: lightgreen">点击登录!</router-link></p>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<script>
import axios from 'axios'
import store from '../store.js'

export default {
  name: 'Register', //this is the name of the component
  data () {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.registerForm.checkPass !== '') {
          this.$refs.registerForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      registerForm: {
        username: '',
        password: '',
        checkPass: '',
        email: '',
      },
      rules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
        ],
        password: [
          { required: true, validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { required: true, validator: validatePass2, trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
        ]
      }
    }
  },
  methods: {
    // 注册
    onSubmitRegister(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let payload = {
            'username': this.registerForm.username,
            'password': this.registerForm.password,
            'email': this.registerForm.email,
          }
          const path = `/users`
          this.$axios.post(path,payload)
            .then((res) => {
              console.log(res)
              if(res.data.status == 200){
                this.$message({
                  message: '注册成功',
                  type: 'success'
                });
                this.$router.push('/login')
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
    onResetRegister(formName){
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
