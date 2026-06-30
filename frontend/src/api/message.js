import request from '@/utils/request'

export const messageApi = {
  // 获取消息列表
  getMessages(user1Id, user2Id = null) {
    return request({
      url: '/api/messages',
      method: 'get',
      params: { user1Id, user2Id }
    })
  },

  // 发送消息
  sendMessage(data) {
    return request({
      url: '/api/messages',
      method: 'post',
      data
    })
  },

  // 标记消息为已读
  markAsRead(id, userId) {
    return request({
      url: `/api/messages/${id}/read`,
      method: 'put',
      params: { userId }
    })
  }
}
