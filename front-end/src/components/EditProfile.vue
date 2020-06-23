<template>
  <el-card>
    <el-row>
      <el-page-header @back="goBack" content="编辑信息" style="margin: 20px 0; font-weight: 800;">
      </el-page-header>
      <el-col :span="10">
       <!-- 部署服务器后需要修改 -->
       <el-upload
          class="avatar-uploader"
          action="http://49.234.221.187:5000/vueblog/api/upload/picture"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :on-error="handleError"
          :before-upload="beforeAvatarUpload">
          <img v-if="userForm.avatar" :src="userForm.avatar" class="avatar_178px">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
        <el-switch
          @change="handleSwitch"
          v-model="updatePassword"
          active-text="修改密码"
          inactive-text="">
        </el-switch>
      </el-col>
      <el-col :span="14">
        <el-form ref="userForm" :model="userForm" :rules="rules" label-width="100px">
          <el-form-item label="昵称" prop="name">
            <el-input v-model="userForm.name"></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="userForm.email"></el-input>
          </el-form-item>
          <el-form-item v-if="this.updatePassword" label="新密码" prop="password">
            <el-input type="password" v-model="userForm.password" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item v-if="this.updatePassword" label="确认新密码" prop="checkPass">
            <el-input type="password" v-model="userForm.checkPass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item size="mini">
            <el-button type="primary" @click="onSubmitEdit('userForm')">修改</el-button>
            <el-button @click="onResetEdit('userForm')">清空</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
  import store from '../store.js'
  export default{
    data(){
      var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入新密码'));
        } else {
          if (this.userForm.checkPass !== '') {
            this.$refs.userForm.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.userForm.password && this.userForm.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return{
        sharedStatus: store.state,
        updatePassword: false,
        userForm:{
          name:'',
          avatar:'',
          email:'',
          password:'',
          checkPass: '',
        },
        user:'',
        rules: {
          password: [
            { validator: validatePass, trigger: 'blur' }
          ],
          checkPass: [
            { validator: validatePass2, trigger: 'blur' }
          ],
          email: [
            { required: true, message: '邮箱不能为空', trigger: 'blur' },
            { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
          ]
        }
      }
    },
    methods:{
      getUser(){
        const path = `/users/${this.$route.params.id}`
        this.$axios.get(path)
          .then((res) => {
            this.user = JSON.parse(JSON.stringify(res.data.data))
            this.userForm = res.data.data
          })
          .catch((error) => {
            console.error(error)
          })
      },
      handleAvatarSuccess(res, file) {
        this.userForm.avatar = res.data;
        this.$message({
          message: "上传成功",
          duration: 2000,
          type: "success"
        });
      },
      handleError(res) {
        this.$message({
          message: "上传失败",
          duration: 2000,
          type: "warning"
        });
      },
      beforeAvatarUpload(file) {
        // const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;
        // if (!isJPG) {
        //   this.$message.error('上传头像图片只能是 JPG 格式!');
        // }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        // return isJPG && isLt2M;
        return isLt2M;
      },
      handleSwitch(val){
        if(val === false){
          this.userForm.password = ''
          this.userForm.checkPass = ''
        }
      },
      // 更新
      onSubmitEdit(formName){
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let payload = {
              'avatar': this.userForm.avatar,
              'name': this.userForm.name,
              'password': this.userForm.password,
              'email': this.userForm.email,
            }
            const path = `/users/${this.userForm.id}`
            this.$axios.put(path,payload)
              .then((res) => {
                // console.log(res)
                if(res.data.status == 200){
                  this.$message({
                    message: '更新成功',
                    type: 'success'
                  });
                  this.$router.push(`/user/${res.data.data.id}`)
                  this.sharedStatus.user = res.data.data
                  window.localStorage.setItem('vueblog-user', JSON.stringify(res.data.data))
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
      onResetEdit(formName){
        this.$refs[formName].resetFields();
        this.userForm = JSON.parse(JSON.stringify(this.user))
      },
      // 返回之前页面
      goBack(){
        this.$router.back()
      },
    },
    created() {
      this.getUser()
    }
  }
</script>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar_178px {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
