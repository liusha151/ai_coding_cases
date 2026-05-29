import request from '../utils/request'
export function getStatistics(params) { return request({ url: '/statistics', method: 'get', params }) }
