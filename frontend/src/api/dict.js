import request from '../utils/request'
export function getDictTypes() { return request({ url: '/dict/types', method: 'get' }) }
export function getDictItems(typeCode) { return request({ url: `/dict/items/${typeCode}`, method: 'get' }) }
export function createDictItem(data) { return request({ url: '/dict/items', method: 'post', data }) }
export function updateDictItem(id, data) { return request({ url: `/dict/items/${id}`, method: 'put', data }) }
export function deleteDictItem(id) { return request({ url: `/dict/items/${id}`, method: 'delete' }) }
