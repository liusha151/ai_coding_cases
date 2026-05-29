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
import DictSelect from '../../components/DictSelect'
import { createWorks, updateWorks } from '../../api/works'
export default {
  components: { DictSelect },
  props: { visible: Boolean, mode: String, row: Object },
  data() {
    return {
      form: { workNo: '', authorName: '', workType: '', workName: '', publishPath: '', status: '', personalRank: 1, coAuthors: '', acquireDate: '', remark: '' },
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
  watch: {
    row: { immediate: true, handler(n) { if (n && this.mode === 'edit') this.form = { ...n } else this.resetForm() } }
  },
  methods: {
    resetForm() { this.form = { workNo: '', authorName: '', workType: '', workName: '', publishPath: '', status: '', personalRank: 1, coAuthors: '', acquireDate: '', remark: '' } },
    handleClosed() { this.$refs.form.clearValidate(); this.resetForm() },
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
