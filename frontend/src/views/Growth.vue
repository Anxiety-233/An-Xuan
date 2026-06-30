<template>
  <div class="min-h-screen bg-background">
    <div class="max-w-5xl mx-auto px-6 py-12">
      <!-- 头部 -->
      <div class="flex items-center justify-between mb-8">
        <h1 class="text-4xl font-serif font-bold">我的<span class="italic text-terracotta">成长</span>记录</h1>
        <button
          @click="showCreateModal = true"
          class="px-6 py-2 bg-terracotta text-white rounded-full hover:bg-terracotta-hover transition">
          + 新建记录
        </button>
      </div>

      <!-- 记录列表 -->
      <div v-if="loading" class="text-center py-12">
        <p class="text-muted">加载中...</p>
      </div>

      <div v-else-if="records.length === 0" class="bg-white rounded-2xl p-8 border border-border text-center">
        <p class="text-muted">还没有成长记录，开始记录你的第一步吧！</p>
      </div>

      <div v-else class="space-y-4">
        <div
          v-for="record in records"
          :key="record.id"
          class="bg-white rounded-2xl p-6 border border-border hover:shadow-md transition">
          <div class="flex items-start justify-between mb-3">
            <div class="flex-1">
              <h3 class="text-xl font-bold text-ink mb-2">{{ record.title }}</h3>
              <div class="flex items-center gap-4 text-sm text-muted">
                <span>📅 {{ record.recordDate }}</span>
                <span v-if="record.studyDuration">⏱️ {{ record.studyDuration }} 分钟</span>
              </div>
            </div>
            <div class="flex gap-2">
              <button
                @click="editRecord(record)"
                class="px-3 py-1 text-sm border border-border rounded-lg hover:border-terracotta transition">
                编辑
              </button>
              <button
                @click="deleteRecord(record.id)"
                class="px-3 py-1 text-sm border border-red-200 text-red-600 rounded-lg hover:bg-red-50 transition">
                删除
              </button>
            </div>
          </div>

          <p class="text-muted mb-3 whitespace-pre-wrap">{{ record.content }}</p>

          <div v-if="record.tags" class="flex gap-2 flex-wrap">
            <span
              v-for="tag in record.tags.split(',')"
              :key="tag"
              class="px-3 py-1 bg-terracotta-tint text-terracotta text-sm rounded-full">
              {{ tag }}
            </span>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div v-if="totalPages > 1" class="flex justify-center gap-2 mt-8">
        <button
          v-for="page in totalPages"
          :key="page"
          @click="currentPage = page; fetchRecords()"
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

    <!-- 创建/编辑弹窗 -->
    <div
      v-if="showCreateModal"
      class="fixed inset-0 bg-black/50 flex items-center justify-center z-50"
      @click.self="closeModal">
      <div class="bg-white rounded-2xl p-8 max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        <h2 class="text-2xl font-serif font-bold mb-6">
          {{ editingRecord ? '编辑记录' : '新建记录' }}
        </h2>

        <form @submit.prevent="saveRecord" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-ink mb-2">标题 *</label>
            <input
              v-model="form.title"
              type="text"
              required
              class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
              placeholder="今天学了什么？">
          </div>

          <div>
            <label class="block text-sm font-medium text-ink mb-2">内容</label>
            <textarea
              v-model="form.content"
              rows="6"
              class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
              placeholder="记录今天的学习内容..."></textarea>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-ink mb-2">日期 *</label>
              <input
                v-model="form.recordDate"
                type="date"
                required
                class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta">
            </div>

            <div>
              <label class="block text-sm font-medium text-ink mb-2">学习时长（分钟）</label>
              <input
                v-model.number="form.studyDuration"
                type="number"
                min="0"
                class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
                placeholder="60">
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-ink mb-2">标签（逗号分隔）</label>
            <input
              v-model="form.tags"
              type="text"
              class="w-full px-4 py-2 border border-border rounded-lg focus:outline-none focus:ring-2 focus:ring-terracotta"
              placeholder="Java, Spring Boot, 后端开发">
          </div>

          <div class="flex gap-3 pt-4">
            <button
              type="submit"
              class="flex-1 py-3 bg-terracotta text-white rounded-full hover:bg-terracotta-hover transition">
              {{ editingRecord ? '保存修改' : '创建记录' }}
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
import { growthApi } from '../api/growth'

const loading = ref(false)
const records = ref([])
const currentPage = ref(1)
const totalPages = ref(0)
const pageSize = 10

const showCreateModal = ref(false)
const editingRecord = ref(null)
const form = ref({
  title: '',
  content: '',
  recordDate: new Date().toISOString().split('T')[0],
  studyDuration: null,
  tags: ''
})

// 获取记录列表
const fetchRecords = async () => {
  loading.value = true
  try {
    const userId = 1 // 使用测试用户 ID
    const res = await growthApi.getRecords(userId, currentPage.value, pageSize)
    records.value = res.data.records
    totalPages.value = res.data.pages
  } catch (error) {
    console.error('获取记录失败:', error)
    alert('获取记录失败，请检查后端服务')
  } finally {
    loading.value = false
  }
}

// 保存记录
const saveRecord = async () => {
  try {
    const userId = 1 // 使用测试用户 ID
    const data = {
      ...form.value,
      userId
    }

    if (editingRecord.value) {
      await growthApi.updateRecord(editingRecord.value.id, data)
      alert('更新成功！')
    } else {
      await growthApi.createRecord(data)
      alert('创建成功！')
    }

    closeModal()
    fetchRecords()
  } catch (error) {
    console.error('保存失败:', error)
    alert('保存失败：' + (error.response?.data?.message || error.message))
  }
}

// 编辑记录
const editRecord = (record) => {
  editingRecord.value = record
  form.value = {
    title: record.title,
    content: record.content,
    recordDate: record.recordDate,
    studyDuration: record.studyDuration,
    tags: record.tags
  }
  showCreateModal.value = true
}

// 删除记录
const deleteRecord = async (id) => {
  if (!confirm('确定要删除这条记录吗？')) return

  try {
    const userId = 1 // 使用测试用户 ID
    await growthApi.deleteRecord(id, userId)
    alert('删除成功！')
    fetchRecords()
  } catch (error) {
    console.error('删除失败:', error)
    alert('删除失败：' + (error.response?.data?.message || error.message))
  }
}

// 关闭弹窗
const closeModal = () => {
  showCreateModal.value = false
  editingRecord.value = null
  form.value = {
    title: '',
    content: '',
    recordDate: new Date().toISOString().split('T')[0],
    studyDuration: null,
    tags: ''
  }
}

onMounted(() => {
  fetchRecords()
})
</script>
