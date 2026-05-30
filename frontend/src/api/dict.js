/* 数据字典 API：字典类型和字典项的 CRUD 操作 */
import request from '../utils/request'
export const getDictItems = typeCode => request.get(`/dict/items/${typeCode}`)
export const getDictTypes = () => request.get('/dict/types')
export const createDictItem = data => request.post('/dict/items', data)
export const updateDictItem = (id, data) => request.put(`/dict/items/${id}`, data)
export const deleteDictItem = id => request.delete(`/dict/items/${id}`)