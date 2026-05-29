import Vue from 'vue'
import Router from 'vue-router'
import Login from '../views/login/Login.vue'
import MainLayout from '../layouts/MainLayout.vue'
import WorksList from '../views/works/WorksList.vue'
import Statistics from '../views/statistics/Statistics.vue'
import DictManagement from '../views/system/DictManagement.vue'
import UserManagement from '../views/system/UserManagement.vue'

Vue.use(Router)

const router = new Router({
  routes: [
    { path: '/login', component: Login },
    {
      path: '/', component: MainLayout, meta: { requiresAuth: true },
      children: [
        { path: 'works', component: WorksList },
        { path: 'statistics', component: Statistics },
        { path: 'system/dict', component: DictManagement, meta: { role: 'admin' } },
        { path: 'system/users', component: UserManagement, meta: { role: 'admin' } },
        { path: '', redirect: '/works' }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
