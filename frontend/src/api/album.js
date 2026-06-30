import request from '@/utils/request'

export const albumApi = {
  // 获取相册列表
  getAlbums(userId) {
    return request({
      url: '/api/albums',
      method: 'get',
      params: { userId }
    })
  },

  // 获取相册详情
  getAlbumById(id) {
    return request({
      url: `/api/albums/${id}`,
      method: 'get'
    })
  },

  // 创建相册
  createAlbum(data) {
    return request({
      url: '/api/albums',
      method: 'post',
      data
    })
  },

  // 删除相册
  deleteAlbum(id, userId) {
    return request({
      url: `/api/albums/${id}`,
      method: 'delete',
      params: { userId }
    })
  }
}

export const photoApi = {
  // 获取相册照片
  getPhotos(albumId) {
    return request({
      url: '/api/photos',
      method: 'get',
      params: { albumId }
    })
  },

  // 上传照片
  uploadPhoto(data) {
    return request({
      url: '/api/photos',
      method: 'post',
      data
    })
  },

  // 删除照片
  deletePhoto(id, userId) {
    return request({
      url: `/api/photos/${id}`,
      method: 'delete',
      params: { userId }
    })
  }
}
