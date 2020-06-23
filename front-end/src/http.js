import axios from 'axios'
import store from './store.js'
import router from './router/index.js'
import Vue from 'vue'

// 基础配置
// axios.defaults.timeout = 5000
// 118.178.91.64
axios.defaults.baseURL = 'http://49.234.221.187:5000/vueblog'
// axios.defaults.baseURL = 'http://localhost:5000'

//add a request interceptor
axios.interceptors.request.use(function (config){
  //请求发送前添加 Token 到请求头 Authorization 中
  const token = window.localStorage.getItem('vueblog-token')
  if (token && config.url.indexOf("https") == -1){
    // console.log('在请求头添加token')
    config.headers.token = token
  }
  if (config.url.indexOf("https") != -1){
    config.baseURL = ''
  }
  return config
}, function(error){
  return Promise.reject(error)
})


// add a response interceptor
// axios.interceptors.response.use(function (response){
//   return response
// }, function (error){
//   // do something with response error
//   console.log('error:', error)
//   switch (error.response.status){
//     case 401:
//       // 清除token 以及 已认证状态
//       store.logoutAction()
//       // Vue.toasted.error('401: 认证已失效, 请先登录', {icon:'fingerprint'})
//       // 跳转至登录页
//       if (router.currentRoute.path !== '/login'){
//         Vue.toasted.error('401: 认证已失效, 请先登录', {icon:'fingerprint'})
//         router.replace({
//           path:'/login',
//           query:{redirect:router.currentRoute.path},
//         })
//       }
//       break
//     case 404:
//       Vue.toasted.error('404: NOT FOUND', { icon: 'fingerprint' })
//       router.back()
//       break
//   }
//   return Promise.reject(error)
// })
export default axios
