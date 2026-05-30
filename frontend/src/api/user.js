/* 用户管理 API：系统用户的 CRUD 操作 */
import request from '../utils/request'
export const getUsers = () => request.get('/users')
export const getUserById = id => request.get(`/users/${id}`)
export const createUser = data => request.post('/users', data)
export const updateUser = (id, data) => request.put(`/users/${id}`, data)
export const deleteUser = id => request.delete(`/users/${id}`)