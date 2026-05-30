<template>
  <el-select v-model="val" :placeholder="placeholder" :clearable="clearable" @change="$emit('input', $event)">
    <el-option v-for="item in items" :key="item.id" :label="item.itemValue" :value="item.itemValue"></el-option>
  </el-select>
</template>
<script>
/* 数据字典下拉选择组件：根据 typeCode 从后端加载字典项列表 */
import { getDictItems } from '../api/dict'
export default {
  props: { typeCode: String, value: String, placeholder: { type: String, default: '请选择' }, clearable: { type: Boolean, default: true } },
  data() { return { items: [], val: this.value } },
  watch: { value(v) { this.val = v } },
  created() { getDictItems(this.typeCode).then(res => { this.items = res.data || [] }) }
}
</script>