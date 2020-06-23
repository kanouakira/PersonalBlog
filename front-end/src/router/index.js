import Vue from 'vue'
import Router from 'vue-router'

import Home from '@/components/Home'
import Post from '@/components/Post'
import Login from '@/components/Login'
import Register from '@/components/Register'
import Profile from '@/components/Profile'
import EditProfile from '@/components/EditProfile'
import AboutMe from '@/components/AboutMe'

// 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/aboutme',
      name: 'AboutMe',
      component: AboutMe
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
    {
      path: '/user/:id',
      name: 'Profile',
      component: Profile,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/user/:id/edit-profile',
      name: 'EditProfile',
      component: EditProfile,
      meta: {
        requiresAuth: true
      }
    },
    {
      path: '/post/:id',
      name: 'Post',
      component: Post
    }
  ],
})
router.beforeEach((to, from, next) => {
  const token = window.localStorage.getItem('vueblog-token')
  if (to.matched.some(record => record.meta.requiresAuth) && (!token || token === null)) {
    next({
      path: '/login',
      query: { redirect: to.fullPath }
    })
  } else if (token && to.name == 'Login') {
    // 用户已登录，但又去访问登录页面时不让他过去
    next({
      path: from.fullPath
    })
  } else {
    next()
  }
})
export default router
