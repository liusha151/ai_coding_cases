/* Axios 请求封装：统一添加 JWT Token 鉴权头和后端 API 前缀 */
import axios from 'axios'
import { Message } from 'element-ui'

const request = axios.create({ baseURL: 'http://localhost:8015/api/v1' })

/* 请求拦截器：自动附加 Authorization Bearer Token */
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = 'Bearer ' + token
  return config
})

/* 响应拦截器：统一错误提示，401 时重定向至登录页 */
request.interceptors.response.use(
  res => res.data,
  err => {
    if (err.response && err.response.status === 401) {
      localStorage.removeItem('token')
      window.location.href = '/login'
    }
    Message.error(err.response?.data?.message || '请求失败')
    return Promise.reject(err)
  }
)

export default request
