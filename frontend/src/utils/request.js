import axios from 'axios'
import store from '../store'
import router from '../router'

const service = axios.create({ baseURL: '/api/v1', timeout: 15000 })

service.interceptors.request.use(config => {
  const token = store.state.token
  if (token) { config.headers['Authorization'] = 'Bearer ' + token }
  return config
})

service.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response && error.response.status === 401) {
      store.commit('LOGOUT'); router.push('/login')
    }
    return Promise.reject(error)
  }
)

export default service
