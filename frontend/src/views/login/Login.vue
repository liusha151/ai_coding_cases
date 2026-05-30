<template>
  <div class="login-container" style="display:flex;justify-content:center;align-items:center;height:100vh;background:linear-gradient(135deg,#0a1628,#1a3a5c)">
    <el-card style="width:400px;padding:30px">
      <h2 style="text-align:center;margin-bottom:30px">个人著作信息管理系统</h2>
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item prop="username"><el-input v-model="form.username" placeholder="用户名" prefix-icon="el-icon-user"></el-input></el-form-item>
        <el-form-item prop="password"><el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="el-icon-lock" @keyup.enter.native="handleLogin"></el-input></el-form-item>
        <el-form-item><el-button type="primary" style="width:100%" :loading="loading" @click="handleLogin">登 录</el-button></el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
/* 登录页面组件：调用后端 API 进行用户认证 */
import { login } from '../../api/auth'
export default {
  data() {
    return {
      form: { username: '', password: '' },
      rules: { username: [{ required: true, message: '请输入用户名' }], password: [{ required: true, message: '请输入密码' }] },
      loading: false
    }
  },
  methods: {
    /* 处理登录请求：验证表单 → 调用 API → 保存 token → 跳转主页 */
    async handleLogin() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const res = await login(this.form)
          this.$store.commit('SET_TOKEN', { token: res.data.token, username: res.data.username, role: res.data.role })
          this.$router.push('/')
        } catch (e) { this.$message.error('登录失败') }
        finally { this.loading = false }
      })
    }
  }
}
</script>