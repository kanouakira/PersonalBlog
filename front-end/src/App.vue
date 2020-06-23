<template>
  <div id="app">
    <el-header>
      <headernav></headernav>
    </el-header>
    <el-container>
      <el-main id="searchBar" @scroll.native="handleScroll($event)">
        <el-row :gutter="20">
          <el-col :sm="7" :md="6" :lg="5" class="hidden-xs-only">
            <div v-resize="redraw" ref="navbar" v-sticky="{ zIndex: 100, stickyTop: 0, disabled: false}">
              <navbar style="min-width: 200px;"></navbar>
            </div>
            <div ref="empty"></div>
          </el-col>
          <el-col v-resize="redraw" :sm="17" :md="18" :lg="19" ref="content">
            <router-view/>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import VueSticky from 'vue-sticky'
import Navbar from './components/Navbar'
import HeaderNav from './components/Header'
import NProgress from "nprogress";

export default {
  name: 'App',
  components:{
    navbar: Navbar,
    headernav: HeaderNav
  },
  directives:{
    'sticky': VueSticky,
  },
  methods: {
    // 计算el-main屏幕滚动的距离将滚动条显示在顶部
    handleScroll() {
      // 屏幕剩余的高度
      let surplus =
        document.getElementById("searchBar").scrollHeight -
        document.getElementById("searchBar").clientHeight;
      // 当前滑动高度
      let scrollY = document.getElementById("searchBar").scrollTop;
      // 当前位置百分比小数
      let coorY = scrollY / surplus - 0.0001;
      // 设置导航栏，这里使用NProgress.set() 动态更改进度条
      NProgress.set(coorY);
    },
    // 动态计算固定侧栏
    redraw(){
      var contentHeight = this.$refs.content.$el.offsetHeight
      var navbarHeight = this.$refs.navbar.offsetHeight
      if(navbarHeight){
        this.$refs.empty.style.height= contentHeight-navbarHeight+"px";
      }else{
        this.$refs.empty.style.height= "0px";
      }
    }
  },
  mounted() {
    window.addEventListener('scroll', this.handleScroll); // 监听滚动条事件
  }
}
</script>

<style>
  #app{
    font-family: "PingFang SC";
  }
  a{
    text-decoration:none;
    color:#333;
  }
 .el-header {
    position: relative;
    width: 100%;
    line-height: 70px;
  }
  .el-main {
    left: 10px;
    right: 10px;
    top: 70px;
    bottom: 0;
    position: absolute;
    /* overflow: hidden; */
    /* overflow-y: scroll; */
  }
  .el-main::-webkit-scrollbar {display:none}
  #nprogress .bar{
    background: lightblue !important;
  }
  #nprogress .peg {
    box-shadow: 0 0 10px #00000000, 0 0 5px #00000000 !important;
  }
</style>
