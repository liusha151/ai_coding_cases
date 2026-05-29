<template>
  <el-dialog title="著作详情" :visible.sync="visible" width="600px">
    <el-descriptions :column="2" border v-if="row">
      <el-descriptions-item label="工号">{{ row.workNo }}</el-descriptions-item>
      <el-descriptions-item label="姓名">{{ row.authorName }}</el-descriptions-item>
      <el-descriptions-item label="著作类型">{{ row.workType }}</el-descriptions-item>
      <el-descriptions-item label="状态">{{ row.status }}</el-descriptions-item>
      <el-descriptions-item label="名称" :span="2">{{ row.workName }}</el-descriptions-item>
      <el-descriptions-item label="发表路径" :span="2">{{ row.publishPath }}</el-descriptions-item>
      <el-descriptions-item label="个人排名">{{ row.personalRank }}</el-descriptions-item>
      <el-descriptions-item label="取得时间">{{ row.acquireDate }}</el-descriptions-item>
      <el-descriptions-item label="著作人" :span="2">{{ row.coAuthors }}</el-descriptions-item>
      <el-descriptions-item label="备注" :span="2">{{ row.remark }}</el-descriptions-item>
    </el-descriptions>
  </el-dialog>
</template>
<script>
export default {
  props: { visible: Boolean, row: Object }
}
</script>
