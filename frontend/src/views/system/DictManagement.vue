<template>
  <div>
    <el-card style="margin-bottom:16px">
      <el-button type="primary" @click="showAddItem">新增字典项</el-button>
    </el-card>
    <el-card>
      <el-tabs v-model="activeType">
        <el-tab-pane label="著作类型" name="work_type"></el-tab-pane>
        <el-tab-pane label="状态" name="work_status"></el-tab-pane>
      </el-tabs>
      <el-table :data="items" border>
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="itemValue" label="字典值" width="200"></el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="80"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="showEditItem(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDeleteItem(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog :title="itemMode==='add'?'新增字典项':'编辑字典项'" :visible.sync="itemDialogVisible" width="400px">
      <el-form ref="itemForm" :model="itemForm" :rules="itemRules" label-width="80px">
        <el-form-item label="字典值" prop="itemValue"><el-input v-model="itemForm.itemValue"></el-input></el-form-item>
        <el-form-item label="排序" prop="sortOrder"><el-input-number v-model="itemForm.sortOrder" :min="0"></el-input-number></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="itemDialogVisible=false">取消</el-button>
        <el-button type="primary" @click="saveItem">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
/* 数据字典管理页面：按字典类型（著作类型/状态）维护字典项，支持增删改 */
import { getDictItems, createDictItem, updateDictItem, deleteDictItem } from '../../api/dict'
export default {
  data() {
    return {
      activeType: 'work_type', items: [],
      itemDialogVisible: false, itemMode: 'add', currentItem: null,
      itemForm: { itemValue: '', sortOrder: 0 },
      itemRules: { itemValue: [{ required: true, message: '请输入字典值' }] }
    }
  },
  /* 切换 Tab 时重新加载当前字典类型的项列表 */
  watch: { activeType: { immediate: true, handler() { this.loadItems() } } },
  methods: {
    async loadItems() { const res = await getDictItems(this.activeType); this.items = res.data || [] },
    showAddItem() { this.itemMode = 'add'; this.currentItem = null; this.itemForm = { itemValue: '', sortOrder: 0 }; this.itemDialogVisible = true },
    showEditItem(row) { this.itemMode = 'edit'; this.currentItem = row; this.itemForm = { itemValue: row.itemValue, sortOrder: row.sortOrder }; this.itemDialogVisible = true },
    async saveItem() {
      this.$refs.itemForm.validate(async valid => {
        if (!valid) return
        const data = Object.assign({}, this.itemForm, { typeCode: this.activeType })
        if (this.itemMode === 'add') await createDictItem(data)
        else await updateDictItem(this.currentItem.id, data)
        this.$message.success('保存成功'); this.itemDialogVisible = false; this.loadItems()
      })
    },
    async handleDeleteItem(row) {
      await this.$confirm('确认删除该字典项？', '提示', { type: 'warning' })
      await deleteDictItem(row.id); this.$message.success('删除成功'); this.loadItems()
    }
  }
}
</script>