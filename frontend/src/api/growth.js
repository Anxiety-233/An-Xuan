import request from '@/utils/request'

export const growthApi = {
  // 获取成长记录列表
  getRecords(userId, pageNum = 1, pageSize = 10) {
    return request({
      url: '/api/growth',
      method: 'get',
      params: { userId, pageNum, pageSize }
    })
  },

  // 创建成长记录
  createRecord(data) {
    return request({
      url: '/api/growth',
      method: 'post',
      data
    })
  },

  // 更新成长记录
  updateRecord(id, data) {
    return request({
      url: `/api/growth/${id}`,
      method: 'put',
      data
    })
  },

  // 删除成长记录
  deleteRecord(id, userId) {
    return request({
      url: `/api/growth/${id}`,
      method: 'delete',
      params: { userId }
    })
  }
}
