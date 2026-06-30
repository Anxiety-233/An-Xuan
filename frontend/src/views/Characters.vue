<template>
  <div class="min-h-screen bg-background">
    <div class="max-w-6xl mx-auto px-6 py-12">
      <!-- 头部 -->
      <div class="flex items-center justify-between mb-8">
        <h1 class="text-4xl font-serif font-bold"><span class="italic text-terracotta">角色</span>图鉴</h1>
        <button
          @click="showCreateModal = true"
          class="px-6 py-2 bg-terracotta text-white rounded-full hover:bg-terracotta-hover transition">
          + 创建角色
        </button>
      </div>

      <!-- 角色卡片网格 -->
      <div v-if="loading" class="text-center py-12">
        <p class="text-muted">加载中...</p>
      </div>

      <div v-else-if="characters.length === 0" class="bg-white rounded-2xl p-8 border border-border text-center">
        <p class="text-muted">还没有角色卡片，来创建第一个吧！</p>
      </div>

      <div v-else class="grid md:grid-cols-3 gap-6 mb-8">
        <div
          v-for="char in characters"
          :key="char.id"
          class="bg-white rounded-2xl overflow-hidden border border-border hover:shadow-lg transition cursor-pointer"
          @click="viewDetail(char.id)">
          <!-- 角色头像 -->
          <div class="aspect-square bg-gradient-to-br from-terracotta-tint to-surface flex items-center justify-center">
            <span class="text-6xl">{{ char.avatar || '🎭' }}</span>
          </div>

          <!-- 角色信息 -->
          <div class="p-6">
            <div class="flex items-start justify-between mb-2">
              <div>
                <h3 class="text-xl font-bold text-ink">{{ char.name }}</h3>
                <p class="text-sm text-muted">{{ char.title || '未设置称号' }}</p>
              </div>
              <div class="flex items-center gap-1">
                <span class="text-yellow-500">⭐</span>
                <span class="text-sm font-medium">{{ char.starRating }}</span>
              </div>
            </div>

            <div class="flex items-center gap-3 text-sm text-muted mb-3">
              <span>Lv.{{ char.level }}</span>
              <span v-if="char.originalPerson">原型: {{ char.originalPerson }}</span>
            </div>

            <!-- 属性条 -->
            <div class="space-y-2 mb-4">
              <div class="flex items-center gap-2">
                <span class="text-xs text-muted w-12">攻击</span>
                <div class="flex-1 h-2 bg-surface rounded-full overflow-hidden">
                  <div class="h-full bg-red-500" :style="{width: `${Math.min(char.attack/10, 100)}%`}"></div>
                </div>
                <span class="text-xs font-medium">{{ char.attack }}</span>
              </div>
              <div class="flex items-center gap-2">
                <span class="text-xs text-muted w-12">防御</span>
                <div class="flex-1 h-2 bg-surface rounded-full overflow-hidden">
                  <div class="h-full bg-blue-500" :style="{width: `${Math.min(char.defense/10, 100)}%`}"></div>
                </div>
                <span class="text-xs font-medium">{{ char.defense }}</span>
              </div>
              <div class="flex items-center gap-2">
                <span class="text-xs text-muted w-12">速度</span>
                <div class="flex-1 h-2 bg-surface rounded-full overflow-hidden">
                  <div class="h-full bg-green-500" :style="{width: `${Math.min(char.speed/10, 100)}%`}"></div>
                </div>
                <span class="text-xs font-medium">{{ char.speed }}</span>
              </div>
            </div>

            <!-- 底部统计 -->
            <div class="flex items-center justify-between text-sm text-muted pt-3 border-t border-border">
              <span>👁️ {{ char.viewCount || 0 }}</span>
              <button
                @click.stop="likeCharacter(char.id)"
                class="flex items-center gap-1 hover:text-terracotta transition">
                <span>❤️</span>
                <span>{{ char.likeCount || 0 }}</span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="totalPages > 1" class="flex justify-center gap-2">
        <button
          v-for="page in totalPages"
          :key="page"
          @click="currentPage = page; fetchCharacters()"
          :class="[
            'px-4 py-2 rounded-lg transition',
            page === currentPage
              ? 'bg-terracotta text-white'
              : 'border border-border hover:border-terracotta'
          ]">
          {{ page }}
        </button>
      </div>
    </div>

    <!-- 创建角色弹窗 -->
    <div
      v-if="showCreateModal"
      class="fixed inset-0 bg-black/50 flex items-center justify-center z-50"
      @click.self="closeModal">
      <div class="bg-white rounded-2xl p-8 max-w-3xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        <h2 class="text-2xl font-serif font-bold mb-6">创建角色卡片</h2>

        <form @submit.prevent="createCharacter" class="space-y-4">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-ink mb-2">角色名称 *</label>
              <input
                v-model="form.name"
                type="text"
                required
                class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
                placeholder="例: 唐三">
            </div>
            <div>
              <label class="block text-sm font-medium text-ink mb-2">原型人物</label>
              <input
                v-model="form.originalPerson"
                type="text"
                class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
                placeholder="例: 小明">
            </div>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-ink mb-2">称号</label>
              <input
                v-model="form.title"
                type="text"
                class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
                placeholder="例: 千手修罗">
            </div>
            <div>
              <label class="block text-sm font-medium text-ink mb-2">头像（Emoji）</label>
              <input
                v-model="form.avatar"
                type="text"
                class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
                placeholder="例: 🔥">
            </div>
          </div>

          <div class="grid grid-cols-4 gap-4">
            <div>
              <label class="block text-sm font-medium text-ink mb-2">等级</label>
              <input
                v-model.number="form.level"
                type="number"
                min="1"
                class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta">
            </div>
            <div>
              <label class="block text-sm font-medium text-ink mb-2">星级</label>
              <select
                v-model.number="form.starRating"
                class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta">
                <option :value="1">1星</option>
                <option :value="2">2星</option>
                <option :value="3">3星</option>
                <option :value="4">4星</option>
                <option :value="5">5星</option>
              </select>
            </div>
          </div>

          <div class="grid grid-cols-3 gap-4">
            <div>
              <label class="block text-sm font-medium text-ink mb-2">攻击力</label>
              <input
                v-model.number="form.attack"
                type="number"
                min="0"
                class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta">
            </div>
            <div>
              <label class="block text-sm font-medium text-ink mb-2">防御力</label>
              <input
                v-model.number="form.defense"
                type="number"
                min="0"
                class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta">
            </div>
            <div>
              <label class="block text-sm font-medium text-ink mb-2">速度</label>
              <input
                v-model.number="form.speed"
                type="number"
                min="0"
                class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta">
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-ink mb-2">技能名称</label>
            <input
              v-model="form.skillName"
              type="text"
              class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
              placeholder="例: 昊天锤">
          </div>

          <div>
            <label class="block text-sm font-medium text-ink mb-2">技能描述</label>
            <textarea
              v-model="form.skillDescription"
              rows="3"
              class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
              placeholder="描述技能效果..."></textarea>
          </div>

          <div>
            <label class="block text-sm font-medium text-ink mb-2">背景故事</label>
            <textarea
              v-model="form.backgroundStory"
              rows="4"
              class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
              placeholder="角色的背景故事..."></textarea>
          </div>

          <div class="flex gap-3 pt-4">
            <button
              type="submit"
              class="flex-1 py-3 bg-terracotta text-white rounded-full hover:bg-terracotta-hover transition">
              创建角色
            </button>
            <button
              type="button"
              @click="closeModal"
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
import { characterApi } from '../api/character'

const loading = ref(false)
const characters = ref([])
const currentPage = ref(1)
const totalPages = ref(0)
const pageSize = 10

const showCreateModal = ref(false)
const form = ref({
  name: '',
  originalPerson: '',
  avatar: '🎭',
  title: '',
  level: 1,
  starRating: 3,
  attack: 100,
  defense: 100,
  speed: 100,
  skillName: '',
  skillDescription: '',
  backgroundStory: '',
  series: '斗罗大陆',
  isPublic: true
})

// 获取角色列表
const fetchCharacters = async () => {
  loading.value = true
  try {
    const res = await characterApi.getCharacters(currentPage.value, pageSize)
    characters.value = res.data.records
    totalPages.value = res.data.pages
  } catch (error) {
    console.error('获取角色列表失败:', error)
    alert('获取角色列表失败，请检查后端服务')
  } finally {
    loading.value = false
  }
}

// 创建角色
const createCharacter = async () => {
  try {
    const userId = 1 // 使用测试用户 ID
    await characterApi.createCharacter({
      ...form.value,
      createdBy: userId
    })
    alert('创建成功！')
    closeModal()
    fetchCharacters()
  } catch (error) {
    console.error('创建失败:', error)
    alert('创建失败：' + (error.response?.data?.message || error.message))
  }
}

// 点赞角色
const likeCharacter = async (id) => {
  try {
    const userId = 1 // 使用测试用户 ID
    const res = await characterApi.likeCharacter(id, userId)
    if (res.code === 200) {
      alert('点赞成功！')
      fetchCharacters()
    }
  } catch (error) {
    console.error('点赞失败:', error)
    alert('点赞失败：' + (error.response?.data?.message || error.message))
  }
}

// 查看详情
const viewDetail = async (id) => {
  try {
    const res = await characterApi.getCharacterDetail(id)
    const char = res.data
    alert(`角色详情：

名称：${char.name}
称号：${char.title || '无'}
等级：${char.level}
星级：${'⭐'.repeat(char.starRating)}

属性：
攻击：${char.attack}
防御：${char.defense}
速度：${char.speed}

技能：${char.skillName || '无'}
${char.skillDescription || ''}

背景故事：
${char.backgroundStory || '暂无'}

统计数据：
浏览量：${char.viewCount}
点赞数：${char.likeCount}`)
    fetchCharacters() // 刷新浏览量
  } catch (error) {
    console.error('获取详情失败:', error)
  }
}

// 关闭弹窗
const closeModal = () => {
  showCreateModal.value = false
  form.value = {
    name: '',
    originalPerson: '',
    avatar: '🎭',
    title: '',
    level: 1,
    starRating: 3,
    attack: 100,
    defense: 100,
    speed: 100,
    skillName: '',
    skillDescription: '',
    backgroundStory: '',
    series: '斗罗大陆',
    isPublic: true
  }
}

onMounted(() => {
  fetchCharacters()
})
</script>
