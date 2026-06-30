<template>
  <div class="min-h-screen">
    <!-- 导航栏 -->
    <nav class="sticky top-0 z-50 backdrop-blur-md bg-surface/80 border-b border-border">
      <div class="max-w-7xl mx-auto px-6 py-4">
        <div class="flex items-center justify-between">
          <router-link to="/" class="text-2xl font-serif font-bold text-ink">
            Life <span class="italic text-terracotta">Hub</span>
          </router-link>

          <div class="flex gap-8 items-center">
            <router-link to="/growth" class="text-muted hover:text-ink transition">成长记录</router-link>
            <router-link to="/couple" class="text-muted hover:text-ink transition">情侣空间</router-link>
            <router-link to="/characters" class="text-muted hover:text-ink transition">角色卡片</router-link>
          </div>
        </div>
      </div>
    </nav>

    <!-- 主页内容 -->
    <main class="max-w-6xl mx-auto px-6 py-20">
      <!-- Hero Section -->
      <div class="text-center mb-20">
        <div class="inline-block px-4 py-1.5 bg-terracotta-tint rounded-full text-sm text-terracotta mb-6">
          三合一生活中心
        </div>

        <h1 class="text-6xl font-serif font-bold mb-6 tracking-tight">
          记录你的 <span class="italic text-terracotta">成长</span>，<br/>
          珍藏我们的 <span class="italic text-terracotta">时光</span>
        </h1>

        <p class="text-xl text-muted max-w-2xl mx-auto mb-12 font-light">
          整合个人成长记录、情侣互动空间、角色卡片系统的全能生活管理平台
        </p>

        <div class="flex gap-4 justify-center">
          <router-link to="/growth"
            class="px-8 py-3 bg-terracotta text-white rounded-full hover:bg-terracotta-hover transition transform hover:-translate-y-0.5">
            开始记录
          </router-link>
          <a href="#features"
            class="px-8 py-3 border border-border rounded-full hover:border-terracotta transition">
            了解更多
          </a>
        </div>
      </div>

      <!-- 特色功能卡片 -->
      <div id="features" class="grid md:grid-cols-3 gap-8 mb-20">
        <div class="bg-white rounded-2xl p-8 border border-border hover:shadow-lg transition">
          <div class="w-12 h-12 bg-terracotta-tint rounded-full flex items-center justify-center mb-6">
            <span class="text-2xl">📚</span>
          </div>
          <h3 class="text-2xl font-serif font-bold mb-3">个人成长</h3>
          <p class="text-muted leading-relaxed">
            每日学习记录、标签分类、时长统计，让成长轨迹清晰可见
          </p>
        </div>

        <div class="bg-white rounded-2xl p-8 border border-border hover:shadow-lg transition">
          <div class="w-12 h-12 bg-terracotta-tint rounded-full flex items-center justify-center mb-6">
            <span class="text-2xl">💕</span>
          </div>
          <h3 class="text-2xl font-serif font-bold mb-3">情侣空间</h3>
          <p class="text-muted leading-relaxed">
            相册墙、心情日记、留言板、纪念日提醒，记录每个甜蜜瞬间
          </p>
        </div>

        <div class="bg-white rounded-2xl p-8 border border-border hover:shadow-lg transition">
          <div class="w-12 h-12 bg-terracotta-tint rounded-full flex items-center justify-center mb-6">
            <span class="text-2xl">🎮</span>
          </div>
          <h3 class="text-2xl font-serif font-bold mb-3">角色卡片</h3>
          <p class="text-muted leading-relaxed">
            将朋友变成斗罗大陆角色，技能、属性、故事一应俱全
          </p>
        </div>
      </div>

      <!-- 后端状态检测 -->
      <div class="text-center">
        <div v-if="backendStatus" class="inline-flex items-center gap-2 px-4 py-2 bg-green-50 border border-green-200 rounded-full text-green-700">
          <span class="w-2 h-2 bg-green-500 rounded-full animate-pulse"></span>
          后端服务运行正常
        </div>
        <div v-else class="inline-flex items-center gap-2 px-4 py-2 bg-red-50 border border-red-200 rounded-full text-red-700">
          <span class="w-2 h-2 bg-red-500 rounded-full"></span>
          后端服务未连接
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'

const backendStatus = ref(false)

const checkBackend = async () => {
  try {
    const res = await request.get('/health')
    if (res.code === 200) {
      backendStatus.value = true
    }
  } catch (error) {
    backendStatus.value = false
  }
}

onMounted(() => {
  checkBackend()
})
</script>
