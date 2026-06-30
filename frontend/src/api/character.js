import request from '@/utils/request'

export const characterApi = {
  // 获取角色列表
  getCharacters(pageNum = 1, pageSize = 10) {
    return request({
      url: '/api/characters',
      method: 'get',
      params: { pageNum, pageSize }
    })
  },

  // 获取角色详情
  getCharacterDetail(id) {
    return request({
      url: `/api/characters/${id}`,
      method: 'get'
    })
  },

  // 创建角色
  createCharacter(data) {
    return request({
      url: '/api/characters',
      method: 'post',
      data
    })
  },

  // 点赞角色
  likeCharacter(id, userId) {
    return request({
      url: `/api/characters/${id}/like`,
      method: 'post',
      params: { userId }
    })
  }
}
