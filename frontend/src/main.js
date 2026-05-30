/* 应用入口：初始化 Vue 实例，注册 ElementUI、路由和状态管理 */
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(ElementUI)
Vue.config.productionTip = false

new Vue({ router, store, render: h => h(App) }).$mount('#app')
