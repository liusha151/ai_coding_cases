/* 统计 API：加载按著作类型/状态/排名/年份的统计数据 */
import request from '../utils/request'
export const getStatistics = params => request.get('/statistics', { params })