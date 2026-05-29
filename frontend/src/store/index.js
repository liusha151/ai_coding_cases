import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') || '',
    username: localStorage.getItem('username') || '',
    role: localStorage.getItem('role') || '',
    theme: localStorage.getItem('theme') || 'tech'
  },
  mutations: {
    SET_TOKEN(state, { token, username, role }) {
      state.token = token; state.username = username; state.role = role
      localStorage.setItem('token', token)
      localStorage.setItem('username', username)
      localStorage.setItem('role', role)
    },
    LOGOUT(state) {
      state.token = ''; state.username = ''; state.role = ''
      localStorage.removeItem('token'); localStorage.removeItem('username'); localStorage.removeItem('role')
    },
    SET_THEME(state, theme) { state.theme = theme; localStorage.setItem('theme', theme) }
  }
})
