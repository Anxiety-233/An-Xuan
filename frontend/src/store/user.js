import { defineStore } from 'pinia'
import { authApi } from '@/api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('user') || 'null')
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    userId: (state) => state.userInfo?.id,
    username: (state) => state.userInfo?.username,
    nickname: (state) => state.userInfo?.nickname
  },

  actions: {
    async login(username, password) {
      try {
        const res = await authApi.login({ username, password })

        this.token = res.data.token
        this.userInfo = res.data.user

        localStorage.setItem('token', res.data.token)
        localStorage.setItem('user', JSON.stringify(res.data.user))

        return res
      } catch (error) {
        throw error
      }
    },

    async register(username, password, nickname) {
      try {
        const res = await authApi.register({ username, password, nickname })
        return res
      } catch (error) {
        throw error
      }
    },

    async fetchUserInfo() {
      try {
        const res = await authApi.getCurrentUser()
        this.userInfo = res.data
        localStorage.setItem('user', JSON.stringify(res.data))
        return res
      } catch (error) {
        this.logout()
        throw error
      }
    },

    logout() {
      this.token = ''
      this.userInfo = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})
