<template>
  <el-menu mode="horizontal" @select="handleSelect">
    <el-menu-item index="1"><i class="el-icon-apple"></i></el-menu-item>
    <!-- <el-menu-item index="1" class="hidden-sm-and-up">首页</el-menu-item> -->
    <el-submenu index="2" class="hidden-sm-and-up">
      <template slot="title">笔记</template>
      <el-submenu v-for="(category, index) in categories" :key="index" :index="category.index">
        <template slot="title">{{category.title}}</template>
        <el-menu-item v-for="(tag, index) in category.tags" :key="index" :index="tag.index">{{tag.index}}</el-menu-item>
      </el-submenu>
<!--      <el-submenu index="2-1">
        <template slot="title">前端</template>
        <el-menu-item index="2-1-1">Vue</el-menu-item>
      </el-submenu>
      <el-submenu index="2-2">
        <template slot="title">后端</template>
        <el-menu-item index="2-2-1">Java</el-menu-item>
      </el-submenu>
      <el-submenu index="2-3">
        <template slot="title">其他</template>
        <el-menu-item index="2-3-1">Linux</el-menu-item>
      </el-submenu> -->
    </el-submenu>
    <el-menu-item index="3" class="hidden-sm-and-up">关于我</el-menu-item>

    <el-menu-item v-if="!sharedState.is_authenticated" index="4" style="float: right;">
      登录
    </el-menu-item>

    <el-submenu v-else index="5" style="float: right;">
      <template slot="title">
        <el-avatar class="avatar" :src="sharedState.user.avatar" @error="return true">
        </el-avatar>
        {{sharedState.user.name ? sharedState.user.name : sharedState.user.username }}
      </template>
      <el-menu-item index="5-1">
        <el-button type='text'>个人信息</el-button>
      </el-menu-item>
      <el-menu-item index="5-2">
        <el-button type='text'>注销</el-button>
      </el-menu-item>
    </el-submenu>
  </el-menu>
</template>

<script>
  import store from '../store.js'
  export default {
    name: 'HeaderNav',
    data() {
      return {
        sharedState: store.state,
        categories: []
      }
    },
    methods: {
      handleSelect(key, keyPath) {
        // console.log(key, keyPath);
        switch(keyPath[0]){
          case "1":
            this.$router.push({path:`/`})
            break;
          case "2":
            this.$router.push({path:`/`, query:{tag:key}})
            break;
          case "3":
            this.$router.push({path:`/aboutme`})
            break;
          case "4":
            this.$router.push({path:`/login`})
            break;
          case "5":
            if(keyPath[1] == "5-1"){
              this.$router.push({path:`/user/${this.sharedState.user.id}`})
            }else if(keyPath[1] == "5-2"){
              this.onLogout()
            }
            break;
        }
      },
      onLogout(){
        store.logoutAction()
        this.$router.push({
          path:this.$route.fullPath,
          query:{
            _time:new Date().getTime()
          }
        })
      },
      getCategories(){
        const path=`/categories`
        this.$axios.get(path)
          .then(res => {
            // console.log(res.data)
            this.categories = []
            for(var i=0;i<res.data.data.length;i++){
              this.category = {
                index: "2-"+res.data.data[i].id,
                title: res.data.data[i].category,
                tags: []
              }
              for(var j=0;j<res.data.data[i].tags.length;j++){
                // var index = res.data.data[i].id+"-"+res.data.data[i].tags[j].id
                var name = res.data.data[i].tags[j].name
                // console.log(index+" >>> "+name)
                this.category.tags.push({
                  index: name,
                })
              }
              this.categories.push(this.category)
            }
          })
          .catch(error => {
            console.error(error)
          })
      }
    },
    created() {
      this.getCategories()
    }
  }
</script>

<style>
  li.el-menu-item{
    padding: 0 5px;
  }
  .avatar{
    border: 1px solid #d9d9d9;
    border-radius: 6px;
  }
</style>
