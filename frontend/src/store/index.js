/* Vuex 状态管理：维护登录态、用户信息和主题配置 */
import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    /* 登录凭证：优先从 localStorage 恢复，避免刷新后丢失 */
    token: localStorage.getItem('token') || '',
    username: localStorage.getItem('username') || '',
    role: localStorage.getItem('role') || '',
    /* 主题偏好：默认科技风 */
    theme: localStorage.getItem('theme') || 'tech'
  },
  mutations: {
    /* 登录成功后保存 token 和用户信息 */
    SET_TOKEN(state, { token, username, role }) {
      state.token = token; state.username = username; state.role = role
      localStorage.setItem('token', token)
      localStorage.setItem('username', username)
      localStorage.setItem('role', role)
    },
    /* 退出登录：清除所有登录信息 */
    LOGOUT(state) {
      state.token = ''; state.username = ''; state.role = ''
      localStorage.removeItem('token'); localStorage.removeItem('username'); localStorage.removeItem('role')
    },
    /* 切换主题风格：同步更新 Vuex 状态和 localStorage */
    SET_THEME(state, theme) { state.theme = theme; localStorage.setItem('theme', theme) }
  }
})
