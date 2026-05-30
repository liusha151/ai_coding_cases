<template>
  <el-dialog :title="mode==='add'?'新增著作':'编辑著作'" :visible.sync="visible" width="700px" @closed="handleClosed">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>
        <el-col :span="12"><el-form-item label="工号" prop="workNo"><el-input v-model="form.workNo"></el-input></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="姓名" prop="authorName"><el-input v-model="form.authorName"></el-input></el-form-item></el-col>
      </el-row>
      <el-row>
        <el-col :span="12"><el-form-item label="著作类型" prop="workType">
          <dict-select type-code="work_type" v-model="form.workType"></dict-select>
        </el-form-item></el-col>
        <el-col :span="12"><el-form-item label="状态" prop="status">
          <dict-select type-code="work_status" v-model="form.status"></dict-select>
        </el-form-item></el-col>
      </el-row>
      <el-form-item label="名称" prop="workName"><el-input v-model="form.workName"></el-input></el-form-item>
      <el-form-item label="发表路径" prop="publishPath"><el-input v-model="form.publishPath"></el-input></el-form-item>
      <el-row>
        <el-col :span="12"><el-form-item label="个人排名" prop="personalRank"><el-input-number v-model="form.personalRank" :min="1"></el-input-number></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="取得时间" prop="acquireDate"><el-date-picker v-model="form.acquireDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期"></el-date-picker></el-form-item></el-col>
      </el-row>
      <el-form-item label="著作人" prop="coAuthors"><el-input v-model="form.coAuthors"></el-input></el-form-item>
      <el-form-item label="备注" prop="remark"><el-input v-model="form.remark" type="textarea" :rows="3"></el-input></el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="visible=false">取消</el-button>
      <el-button type="primary" :loading="saving" @click="save">保存</el-button>
    </span>
  </el-dialog>
</template>
<script>
/* 著作新增/编辑表单组件：支持数据字典下拉选择、表单验证 */
import DictSelect from '../../components/DictSelect'
import { createWorks, updateWorks } from '../../api/works'
export default {
  components: { DictSelect },
  props: { visible: Boolean, mode: String, row: Object },
  data() {
    return {
      /* 表单模型：字段默认值与后端 Works 实体对应 */
      form: { workNo: '', authorName: '', workType: '', workName: '', publishPath: '', status: '', personalRank: 1, coAuthors: '', acquireDate: '', remark: '' },
      /* 表单校验规则：必填字段检查 */
      rules: {
        workNo: [{ required: true, message: '请输入工号' }],
        authorName: [{ required: true, message: '请输入姓名' }],
        workType: [{ required: true, message: '请选择著作类型' }],
        workName: [{ required: true, message: '请输入名称' }],
        status: [{ required: true, message: '请选择状态' }],
        personalRank: [{ required: true, message: '请输入个人排名' }],
        acquireDate: [{ required: true, message: '请选择取得时间' }],
        coAuthors: [{ required: true, message: '请输入著作人' }]
      },
      saving: false
    }
  },
  /* 编辑模式下监听 row 属性变化，将数据回填至表单 */
  watch: {
    row: {
      immediate: true,
      handler(n) {
        if (n && this.mode === 'edit') {
          this.form = Object.assign({}, n)
        } else {
          this.resetForm()
        }
      }
    }
  },
  methods: {
    /* 重置表单至初始状态 */
    resetForm() { this.form = { workNo: '', authorName: '', workType: '', workName: '', publishPath: '', status: '', personalRank: 1, coAuthors: '', acquireDate: '', remark: '' } },
    /* 对话框关闭时清除验证状态并重置表单 */
    handleClosed() { this.$refs.form.clearValidate(); this.resetForm() },
    /* 保存操作：新增或更新后触发列表刷新 */
    async save() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.saving = true
        try {
          if (this.mode === 'add') await createWorks(this.form)
          else await updateWorks(this.row.id, this.form)
          this.$message.success('保存成功')
          this.$emit('update:visible', false)
          this.$emit('saved')
        } catch (e) { this.$message.error('保存失败') }
        finally { this.saving = false }
      })
    }
  }
}
</script>