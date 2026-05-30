<template>
  <el-container style="min-height:100vh">
    <el-aside width="220px" style="background:#0d2137">
      <div class="aside-title" style="padding:20px;color:#1e90ff;font-size:18px;text-align:center">著作管理系统</div>
      <el-menu :default-active="$route.path" router background-color="#0d2137" text-color="#c8d6e5" active-text-color="#1e90ff">
        <el-menu-item index="/works"><i class="el-icon-document"></i>个人著作管理</el-menu-item>
        <el-menu-item index="/statistics"><i class="el-icon-data-analysis"></i>个人著作统计</el-menu-item>
        <el-submenu index="system" v-if="isAdmin">
          <template slot="title"><i class="el-icon-setting"></i>系统管理</template>
          <el-menu-item index="/system/dict">数据字典管理</el-menu-item>
          <el-menu-item index="/system/users">账户管理</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="app-header" style="display:flex;align-items:center;justify-content:space-between;padding:0 20px">
        <span class="header-title" style="font-size:16px">个人著作信息管理系统</span>
        <div style="display:flex;align-items:center;gap:12px">
          <ThemeToggle />
          <span>{{ username }}</span>
          <el-button type="text" @click="logout">退出</el-button>
        </div>
      </el-header>
      <el-main><router-view /></el-main>
    </el-container>
  </el-container>
</template>
<script>
/* 主布局组件：包含侧边导航菜单和顶部工具栏 */
import ThemeToggle from '../components/ThemeToggle'
import { isAdmin } from '../utils/auth'
export default {
  components: { ThemeToggle },
  computed: { username() { return this.$store.state.username }, isAdmin() { return isAdmin() } },
  methods: {
    /* 退出登录：清除状态并跳转至登录页 */
    logout() {
      this.$store.commit('LOGOUT'); this.$router.push('/login')
    }
  }
}
</script>