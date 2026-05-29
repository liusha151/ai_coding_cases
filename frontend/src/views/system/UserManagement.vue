<template>
  <div>
    <el-card style="margin-bottom:16px">
      <el-button type="primary" @click="showAdd">新增账户</el-button>
    </el-card>
    <el-card>
      <el-table :data="list" border>
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="username" label="用户名" width="150"></el-table-column>
        <el-table-column prop="role" label="角色" :formatter="r=>r.role==='admin'?'管理员':'普通用户'" width="120"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="showEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog :title="mode==='add'?'新增账户':'编辑账户'" :visible.sync="dialogVisible" width="450px">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username"><el-input v-model="form.username"></el-input></el-form-item>
        <el-form-item label="密码" prop="password"><el-input v-model="form.password" type="password" :placeholder="mode==='edit'?'留空不修改':'请输入密码'"></el-input></el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role"><el-option label="普通用户" value="user"></el-option><el-option label="管理员" value="admin"></el-option></el-select>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { getUserList, createUser, updateUser, deleteUser } from '../../api/user'
export default {
  data() {
    return {
      list: [], dialogVisible: false, mode: 'add', currentRow: null,
      form: { username: '', password: '', role: 'user' },
      rules: {
        username: [{ required: true, message: '请输入用户名' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色' }]
      }
    }
  },
  created() { this.loadList() },
  methods: {
    async loadList() { const res = await getUserList(); this.list = res.data || [] },
    showAdd() { this.mode = 'add'; this.currentRow = null; this.form = { username: '', password: '', role: 'user' }; this.dialogVisible = true },
    showEdit(row) { this.mode = 'edit'; this.currentRow = row; this.form = { username: row.username, password: '', role: row.role }; this.dialogVisible = true },
    async save() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        if (this.mode === 'add') await createUser(this.form)
        else await updateUser(this.currentRow.id, this.form)
        this.$message.success('保存成功'); this.dialogVisible = false; this.loadList()
      })
    },
    async handleDelete(row) {
      await this.$confirm('确认删除该账户？', '提示', { type: 'warning' })
      await deleteUser(row.id); this.$message.success('删除成功'); this.loadList()
    }
  }
}
</script>
