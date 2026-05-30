/* 认证相关 API：登录/登出 */
import request from '../utils/request'
export const login = data => request.post('/auth/login', data)
export const logout = () => request.post('/auth/logout')