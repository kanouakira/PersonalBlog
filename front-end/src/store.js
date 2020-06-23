export default {
  debug: false,
  state: {
    is_reading:false,
    is_authenticated: window.localStorage.getItem('vueblog-token') ? true : false,
    // // 用户登录后，就算刷新页面也能再次解析出 user
    user: JSON.parse(window.localStorage.getItem('vueblog-user')),
  },
  loginAction () {
    if (this.debug) { console.log('loginAction triggered') }
    this.state.is_authenticated = true
    this.state.user = JSON.parse(window.localStorage.getItem('vueblog-user'))
  },
  logoutAction () {
    if (this.debug) console.log('logoutAction triggered')
    window.localStorage.removeItem('vueblog-token')
    this.state.is_authenticated = false
    this.state.user = ''
  }
}
