<template>
  <div class="min-h-screen bg-[#F7F4EF] flex items-center justify-center p-4">
    <div class="w-full max-w-md">
      <div class="bg-white rounded-2xl shadow-lg p-8">
        <div class="text-center mb-8">
          <h1 class="text-3xl font-serif font-bold text-[#1F2421] mb-2">Life Hub</h1>
          <p class="text-[#5C635D]">{{ isLogin ? '欢迎回来' : '创建账号' }}</p>
        </div>

        <form @submit.prevent="handleSubmit" class="space-y-6">
          <div>
            <label class="block text-sm font-medium text-[#1F2421] mb-2">用户名</label>
            <input
              v-model="form.username"
              type="text"
              required
              class="w-full px-4 py-3 border border-[#E7E1D7] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#C4612F]"
              placeholder="请输入用户名"
            />
          </div>

          <div>
            <label class="block text-sm font-medium text-[#1F2421] mb-2">密码</label>
            <input
              v-model="form.password"
              type="password"
              required
              class="w-full px-4 py-3 border border-[#E7E1D7] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#C4612F]"
              placeholder="请输入密码"
            />
          </div>

          <div v-if="!isLogin">
            <label class="block text-sm font-medium text-[#1F2421] mb-2">昵称（可选）</label>
            <input
              v-model="form.nickname"
              type="text"
              class="w-full px-4 py-3 border border-[#E7E1D7] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#C4612F]"
              placeholder="请输入昵称"
            />
          </div>

          <button
            type="submit"
            :disabled="loading"
            class="w-full py-3 bg-[#C4612F] text-white rounded-full hover:bg-[#A94E22] transition-colors disabled:opacity-50"
          >
            {{ loading ? '处理中...' : (isLogin ? '登录' : '注册') }}
          </button>
        </form>

        <div class="mt-6 text-center">
          <button
            @click="toggleMode"
            class="text-[#C4612F] hover:underline"
          >
            {{ isLogin ? '没有账号？立即注册' : '已有账号？立即登录' }}
          </button>
        </div>

        <div v-if="error" class="mt-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">
          {{ error }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const isLogin = ref(true)
const loading = ref(false)
const error = ref('')

const form = ref({
  username: '',
  password: '',
  nickname: ''
})

const toggleMode = () => {
  isLogin.value = !isLogin.value
  error.value = ''
  form.value = { username: '', password: '', nickname: '' }
}

const handleSubmit = async () => {
  loading.value = true
  error.value = ''

  try {
    if (isLogin.value) {
      await userStore.login(form.value.username, form.value.password)
      router.push('/')
    } else {
      await userStore.register(form.value.username, form.value.password, form.value.nickname)
      alert('注册成功！请登录')
      isLogin.value = true
      form.value.password = ''
      form.value.nickname = ''
    }
  } catch (err) {
    error.value = err.response?.data?.message || err.message || '操作失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>
