import request from '../utils/request'
export function getWorksList(params) { return request({ url: '/works', method: 'get', params }) }
export function getWorksDetail(id) { return request({ url: `/works/${id}`, method: 'get' }) }
export function createWorks(data) { return request({ url: '/works', method: 'post', data }) }
export function updateWorks(id, data) { return request({ url: `/works/${id}`, method: 'put', data }) }
export function deleteWorks(id) { return request({ url: `/works/${id}`, method: 'delete' }) }
export function getAuthorNames() { return request({ url: '/works/author-names', method: 'get' }) }
