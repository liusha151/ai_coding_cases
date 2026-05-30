/* 著作管理 API：分页查询、新增、修改、删除、获取作者列表 */
import request from '../utils/request'
export const getWorksList = params => request.get('/works', { params })
export const getWorksById = id => request.get(`/works/${id}`)
export const createWorks = data => request.post('/works', data)
export const updateWorks = (id, data) => request.put(`/works/${id}`, data)
export const deleteWorks = id => request.delete(`/works/${id}`)
export const getAuthorNames = () => request.get('/works/author-names')