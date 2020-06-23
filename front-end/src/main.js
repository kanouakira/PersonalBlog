// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
// 引入element ui
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'element-ui/lib/theme-chalk/display.css'
import NProgress from 'nprogress' //引入 nprogress 插件
import 'nprogress/nprogress.css' // 引入基础样式
// 导入配置了全局拦截器后的 axios
import axios from './http'
import moment from 'moment'
// 自定义 css 文件
import './assets/custom.css'
import Markdown from 'vue-meditor'

Vue.config.productionTip = false
Vue.use(ElementUI)

// 将 $axios 挂载到 prototype 上，在组件中可以直接使用 this.$axios 访问
Vue.prototype.$axios = axios
// moment.js设置为中文
moment.lang('zh-cn')
Vue.prototype.$moment = moment

// 使用指令 v-resize 当元素高度发生变化会调用回调函数
Vue.directive('resize', {
  bind(el, binding) {      let width = '', height = '';      function get() {        const style = document.defaultView.getComputedStyle(el);        if (width !== style.width || height !== style.height) {
        binding.value({width, height});
      }
      width = style.width;
      height = style.height;
    }
    el.__vueReize__ = setInterval(get, 200);
  },
  unbind(el) {
    clearInterval(el.__vueReize__);
  },
});

NProgress.configure({showSpinner: false});
NProgress.configure({ease:'ease-in',speed:400});
// NProgress.configure({minimum:0.0});
// NProgress.set(0.4);

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
})

//当路由进入前
router.beforeEach((to, from, next) => {
  // 每次切换页面时，调用进度条
  NProgress.start();
  // 这个一定要加，没有next()页面不会跳转的。这部分还不清楚的去翻一下官网就明白了
  next();
});
//当路由进入后：关闭进度条
router.afterEach(() => {
  // 在即将进入新的页面组件前，关闭掉进度条
  NProgress.done()
})
