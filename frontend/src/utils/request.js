import axios from 'axios'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 10000
})

request.interceptors.response.use(
  response => {
    const res = response.data

    // 根据后端返回的 Result 结构判断
    if (res.code && res.code !== 200) {
      console.error('API Error:', res.message)
      return Promise.reject(new Error(res.message || 'Error'))
    }

    return res
  },
  error => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

export default request
