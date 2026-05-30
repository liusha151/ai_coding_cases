/* 路由配置：定义登录页与主页（含子路由）的映射关系，并添加鉴权守卫 */
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
    /* 登录页：无需认证 */
    { path: '/login', component: Login },
    {
      /* 主布局：包含侧边菜单和顶栏，所有业务页面作为子路由 */
      path: '/', component: MainLayout, meta: { requiresAuth: true },
      children: [
        { path: 'works', component: WorksList },
        { path: 'statistics', component: Statistics },
        /* 系统管理功能仅 admin 可用 */
        { path: 'system/dict', component: DictManagement, meta: { role: 'admin' } },
        { path: 'system/users', component: UserManagement, meta: { role: 'admin' } },
        { path: '', redirect: '/works' }
      ]
    }
  ]
})

/* 路由前置守卫：未登录时自动跳转登录页 */
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
