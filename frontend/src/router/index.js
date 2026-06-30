import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Growth from '../views/Growth.vue'
import Couple from '../views/Couple.vue'
import Characters from '../views/Characters.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/growth',
    name: 'Growth',
    component: Growth
  },
  {
    path: '/couple',
    name: 'Couple',
    component: Couple
  },
  {
    path: '/characters',
    name: 'Characters',
    component: Characters
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
