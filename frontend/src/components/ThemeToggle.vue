<template>
  <el-select v-model="currentTheme" @change="switchTheme" size="small" style="width:120px">
    <el-option label="科技风" value="tech"></el-option>
    <el-option label="简略风" value="simple"></el-option>
  </el-select>
</template>
<script>
export default {
  computed: { currentTheme: { get() { return this.$store.state.theme }, set(v) {} } },
  methods: {
    switchTheme(theme) {
      this.$store.commit('SET_THEME', theme)
      const link = document.getElementById('theme-style')
      if (link) link.href = theme === 'tech' ? '/theme/tech.css' : '/theme/simple.css'
    }
  }
}
</script>
