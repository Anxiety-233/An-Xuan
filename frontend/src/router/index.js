import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Growth from '../views/Growth.vue'
import Couple from '../views/Couple.vue'
import Characters from '../views/Characters.vue'
import Login from '../views/Login.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: { requiresAuth: true }
  },
  {
    path: '/growth',
    name: 'Growth',
    component: Growth,
    meta: { requiresAuth: true }
  },
  {
    path: '/couple',
    name: 'Couple',
    component: Couple,
    meta: { requiresAuth: true }
  },
  {
    path: '/characters',
    name: 'Characters',
    component: Characters,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
