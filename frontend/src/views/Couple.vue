<template>
  <div class="min-h-screen bg-background">
    <div class="max-w-6xl mx-auto px-6 py-12">
      <!-- 头部 -->
      <h1 class="text-4xl font-serif font-bold mb-8">我们的<span class="italic text-terracotta">时光</span></h1>

      <!-- 功能选项卡 -->
      <div class="flex gap-4 mb-8 border-b border-border">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          @click="activeTab = tab.key"
          :class="[
            'px-6 py-3 font-medium transition relative',
            activeTab === tab.key
              ? 'text-terracotta'
              : 'text-muted hover:text-ink'
          ]">
          {{ tab.label }}
          <div
            v-if="activeTab === tab.key"
            class="absolute bottom-0 left-0 right-0 h-0.5 bg-terracotta"></div>
        </button>
      </div>

      <!-- 心情日记 -->
      <div v-if="activeTab === 'mood'">
        <div class="grid md:grid-cols-2 gap-6 mb-8">
          <!-- 我的心情 -->
          <div class="bg-white rounded-2xl p-6 border border-border">
            <h3 class="text-xl font-bold mb-4">我的心情</h3>
            <div v-if="todayMood" class="text-center">
              <span class="text-6xl">{{ todayMood.moodEmoji }}</span>
              <p class="text-muted mt-3">{{ todayMood.content }}</p>
            </div>
            <div v-else class="text-center py-8">
              <p class="text-muted mb-4">今天还没有记录心情</p>
              <button
                @click="showMoodModal = true"
                class="px-6 py-2 bg-terracotta text-white rounded-full hover:bg-terracotta-hover transition">
                记录心情
              </button>
            </div>
          </div>

          <!-- 伴侣心情 -->
          <div class="bg-white rounded-2xl p-6 border border-border">
            <h3 class="text-xl font-bold mb-4">TA 的心情</h3>
            <div v-if="partnerMood" class="text-center">
              <span class="text-6xl">{{ partnerMood.moodEmoji }}</span>
              <p class="text-muted mt-3">{{ partnerMood.content }}</p>
            </div>
            <div v-else class="text-center py-8">
              <p class="text-muted">TA 今天还没有记录心情</p>
            </div>
          </div>
        </div>

        <!-- 心情历史 -->
        <div class="bg-white rounded-2xl p-6 border border-border">
          <h3 class="text-xl font-bold mb-4">心情历史</h3>
          <div v-if="moodHistory.length === 0" class="text-center py-8 text-muted">
            暂无历史记录
          </div>
          <div v-else class="space-y-3">
            <div
              v-for="mood in moodHistory"
              :key="mood.id"
              class="flex items-center gap-4 p-4 border border-border rounded-lg">
              <span class="text-4xl">{{ mood.moodEmoji }}</span>
              <div class="flex-1">
                <p class="text-muted text-sm">{{ mood.diaryDate }}</p>
                <p>{{ mood.content }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 相册墙 -->
      <div v-if="activeTab === 'album'">
        <div class="flex justify-between items-center mb-6">
          <h3 class="text-xl font-bold">我们的相册</h3>
          <button
            @click="showAlbumModal = true"
            class="px-6 py-2 bg-terracotta text-white rounded-full hover:bg-terracotta-hover transition">
            + 创建相册
          </button>
        </div>

        <div v-if="albums.length === 0" class="bg-white rounded-2xl p-8 border border-border text-center">
          <p class="text-muted">还没有相册，创建第一个相册吧！</p>
        </div>

        <div v-else class="grid md:grid-cols-3 gap-6">
          <div
            v-for="album in albums"
            :key="album.id"
            class="bg-white rounded-2xl overflow-hidden border border-border hover:shadow-lg transition cursor-pointer"
            @click="viewAlbum(album.id)">
            <div class="aspect-video bg-gradient-to-br from-terracotta-tint to-surface flex items-center justify-center">
              <span class="text-6xl">{{ album.coverImage || '📷' }}</span>
            </div>
            <div class="p-4">
              <h4 class="font-bold text-lg mb-1">{{ album.title }}</h4>
              <p class="text-sm text-muted">{{ album.description || '暂无描述' }}</p>
            </div>
          </div>
        </div>
      </div>

      <!-- 留言板 -->
      <div v-if="activeTab === 'message'">
        <div class="bg-white rounded-2xl p-6 border border-border mb-6">
          <h3 class="text-xl font-bold mb-4">给 TA 留言</h3>
          <form @submit.prevent="sendMessage" class="space-y-4">
            <textarea
              v-model="messageContent"
              rows="4"
              required
              class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
              placeholder="写下你想说的话..."></textarea>
            <button
              type="submit"
              class="px-6 py-2 bg-terracotta text-white rounded-full hover:bg-terracotta-hover transition">
              发送
            </button>
          </form>
        </div>

        <!-- 留言列表 -->
        <div class="bg-white rounded-2xl p-6 border border-border">
          <h3 class="text-xl font-bold mb-4">留言记录</h3>
          <div v-if="messages.length === 0" class="text-center py-8 text-muted">
            还没有留言记录
          </div>
          <div v-else class="space-y-4">
            <div
              v-for="msg in messages"
              :key="msg.id"
              :class="[
                'p-4 rounded-lg',
                msg.fromUserId === currentUserId
                  ? 'bg-terracotta-tint ml-12'
                  : 'bg-surface mr-12'
              ]">
              <p class="mb-2">{{ msg.content }}</p>
              <p class="text-xs text-muted">{{ formatDate(msg.createdAt) }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 记录心情弹窗 -->
    <div
      v-if="showMoodModal"
      class="fixed inset-0 bg-black/50 flex items-center justify-center z-50"
      @click.self="showMoodModal = false">
      <div class="bg-white rounded-2xl p-8 max-w-md w-full mx-4">
        <h2 class="text-2xl font-serif font-bold mb-6">记录今天的心情</h2>

        <form @submit.prevent="saveMood" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-ink mb-2">选择心情</label>
            <div class="grid grid-cols-5 gap-3">
              <button
                v-for="emoji in moodEmojis"
                :key="emoji"
                type="button"
                @click="moodForm.moodEmoji = emoji"
                :class="[
                  'text-4xl p-3 rounded-lg border-2 transition',
                  moodForm.moodEmoji === emoji
                    ? 'border-terracotta bg-terracotta-tint'
                    : 'border-border hover:border-terracotta'
                ]">
                {{ emoji }}
              </button>
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-ink mb-2">今天发生了什么？</label>
            <textarea
              v-model="moodForm.content"
              rows="4"
              class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
              placeholder="记录今天的心情..."></textarea>
          </div>

          <div class="flex gap-3 pt-4">
            <button
              type="submit"
              class="flex-1 py-3 bg-terracotta text-white rounded-full hover:bg-terracotta-hover transition">
              保存
            </button>
            <button
              type="button"
              @click="showMoodModal = false"
              class="px-6 py-3 border border-border rounded-full hover:border-terracotta transition">
              取消
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- 创建相册弹窗 -->
    <div
      v-if="showAlbumModal"
      class="fixed inset-0 bg-black/50 flex items-center justify-center z-50"
      @click.self="showAlbumModal = false">
      <div class="bg-white rounded-2xl p-8 max-w-md w-full mx-4">
        <h2 class="text-2xl font-serif font-bold mb-6">创建相册</h2>

        <form @submit.prevent="createAlbum" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-ink mb-2">相册标题 *</label>
            <input
              v-model="albumForm.title"
              type="text"
              required
              class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
              placeholder="例：2024春游">
          </div>

          <div>
            <label class="block text-sm font-medium text-ink mb-2">描述</label>
            <textarea
              v-model="albumForm.description"
              rows="3"
              class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
              placeholder="记录这次旅行的美好回忆..."></textarea>
          </div>

          <div>
            <label class="block text-sm font-medium text-ink mb-2">封面图标</label>
            <input
              v-model="albumForm.coverImage"
              type="text"
              class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
              placeholder="例：📷 🌸 🎉">
          </div>

          <div class="flex gap-3 pt-4">
            <button
              type="submit"
              class="flex-1 py-3 bg-terracotta text-white rounded-full hover:bg-terracotta-hover transition">
              创建
            </button>
            <button
              type="button"
              @click="showAlbumModal = false"
              class="px-6 py-3 border border-border rounded-full hover:border-terracotta transition">
              取消
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { moodApi } from '../api/mood'
import { albumApi } from '../api/album'
import { messageApi } from '../api/message'

const activeTab = ref('mood')
const tabs = [
  { key: 'mood', label: '心情日记' },
  { key: 'album', label: '相册墙' },
  { key: 'message', label: '留言板' }
]

// 心情相关
const todayMood = ref(null)
const partnerMood = ref(null)
const moodHistory = ref([])
const showMoodModal = ref(false)
const moodEmojis = ['😊', '😢', '😡', '😴', '🥳', '😍', '🤔', '😭', '😎', '🤗']
const moodForm = ref({
  moodEmoji: '😊',
  content: '',
  moodLevel: 3,
  isVisibleToPartner: true
})

// 相册相关
const albums = ref([])
const showAlbumModal = ref(false)
const albumForm = ref({
  title: '',
  description: '',
  coverImage: '📷',
  isCoupleShared: true
})

// 留言相关
const messages = ref([])
const messageContent = ref('')
const currentUserId = ref(1) // 使用测试用户 ID

// 获取今日心情
const fetchTodayMood = async () => {
  try {
    const userId = currentUserId.value
    const res = await moodApi.getMoods(userId, 1, 1)
    const today = new Date().toISOString().split('T')[0]
    const records = res.data.records || []
    todayMood.value = records.find(r => r.diaryDate === today) || null
  } catch (error) {
    console.error('获取今日心情失败:', error)
  }
}

// 获取伴侣心情
const fetchPartnerMood = async () => {
  try {
    const userId = currentUserId.value
    const res = await moodApi.getPartnerMood(userId)
    partnerMood.value = res.data
  } catch (error) {
    console.error('获取伴侣心情失败:', error)
  }
}

// 获取心情历史
const fetchMoodHistory = async () => {
  try {
    const userId = currentUserId.value
    const res = await moodApi.getMoods(userId, 1, 10)
    moodHistory.value = res.data.records || []
  } catch (error) {
    console.error('获取心情历史失败:', error)
  }
}

// 保存心情
const saveMood = async () => {
  try {
    const userId = currentUserId.value
    await moodApi.createMood({
      ...moodForm.value,
      userId,
      diaryDate: new Date().toISOString().split('T')[0]
    })
    alert('记录成功！')
    showMoodModal.value = false
    fetchTodayMood()
    fetchMoodHistory()
  } catch (error) {
    console.error('保存失败:', error)
    alert('保存失败：' + (error.response?.data?.message || error.message))
  }
}

// 获取相册列表
const fetchAlbums = async () => {
  try {
    const userId = currentUserId.value
    const res = await albumApi.getAlbums(userId)
    albums.value = res.data || []
  } catch (error) {
    console.error('获取相册失败:', error)
  }
}

// 创建相册
const createAlbum = async () => {
  try {
    const userId = currentUserId.value
    await albumApi.createAlbum({
      ...albumForm.value,
      createdBy: userId
    })
    alert('创建成功！')
    showAlbumModal.value = false
    fetchAlbums()
    albumForm.value = {
      title: '',
      description: '',
      coverImage: '📷',
      isCoupleShared: true
    }
  } catch (error) {
    console.error('创建失败:', error)
    alert('创建失败：' + (error.response?.data?.message || error.message))
  }
}

// 查看相册
const viewAlbum = (id) => {
  alert(`相册详情功能开发中...\n相册ID: ${id}`)
}

// 获取留言
const fetchMessages = async () => {
  try {
    const userId = currentUserId.value
    const partnerId = userId === 1 ? 2 : 1 // 简化处理
    const res = await messageApi.getMessages(userId, partnerId)
    messages.value = res.data || []
  } catch (error) {
    console.error('获取留言失败:', error)
  }
}

// 发送留言
const sendMessage = async () => {
  try {
    const userId = currentUserId.value
    const partnerId = userId === 1 ? 2 : 1 // 简化处理
    await messageApi.sendMessage({
      fromUserId: userId,
      toUserId: partnerId,
      content: messageContent.value
    })
    messageContent.value = ''
    alert('发送成功！')
    fetchMessages()
  } catch (error) {
    console.error('发送失败:', error)
    alert('发送失败：' + (error.response?.data?.message || error.message))
  }
}

// 格式化日期
const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN')
}

onMounted(() => {
  fetchTodayMood()
  fetchPartnerMood()
  fetchMoodHistory()
  fetchAlbums()
  fetchMessages()
})
</script>
