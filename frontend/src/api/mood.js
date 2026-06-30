import request from '@/utils/request'

export const moodApi = {
  // 获取心情记录
  getMoods(userId, pageNum = 1, pageSize = 10) {
    return request({
      url: '/api/moods',
      method: 'get',
      params: { userId, pageNum, pageSize }
    })
  },

  // 创建心情记录
  createMood(data) {
    return request({
      url: '/api/moods',
      method: 'post',
      data
    })
  },

  // 查看伴侣心情
  getPartnerMood(userId) {
    return request({
      url: '/api/moods/partner',
      method: 'get',
      params: { userId }
    })
  }
}
