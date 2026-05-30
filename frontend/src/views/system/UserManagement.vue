<template>
  <div>
    <el-card style="margin-bottom:16px">
      <el-button type="primary" @click="showAddUser">新增用户</el-button>
    </el-card>
    <el-card>
      <el-table :data="users" border>
        <el-table-column prop="id" label="ID" width="60"></el-table-column>
        <el-table-column prop="username" label="用户名" width="150"></el-table-column>
        <el-table-column prop="role" label="角色" width="100"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="showEditUser(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDeleteUser(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog :title="userMode==='add'?'新增用户':'编辑用户'" :visible.sync="userDialogVisible" width="400px">
      <el-form ref="userForm" :model="userForm" :rules="userRules" label-width="80px">
        <el-form-item label="用户名" prop="username"><el-input v-model="userForm.username"></el-input></el-form-item>
        <el-form-item label="密码" prop="password"><el-input v-model="userForm.password" type="password"></el-input></el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="userForm.role">
            <el-option label="管理员" value="admin"></el-option>
            <el-option label="普通用户" value="user"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="userDialogVisible=false">取消</el-button>
        <el-button type="primary" @click="saveUser">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
/* 账户管理页面：管理员可新增/编辑/删除系统账户 */
import { getUsers, createUser, updateUser, deleteUser } from '../../api/user'
export default {
  data() {
    return {
      users: [],
      userDialogVisible: false, userMode: 'add', currentUser: null,
      userForm: { username: '', password: '', role: 'user' },
      userRules: { username: [{ required: true, message: '请输入用户名' }], password: [{ required: true, message: '请输入密码' }], role: [{ required: true, message: '请选择角色' }] }
    }
  },
  created() { this.loadUsers() },
  methods: {
    async loadUsers() { const res = await getUsers(); this.users = res.data || [] },
    showAddUser() { this.userMode = 'add'; this.currentUser = null; this.userForm = { username: '', password: '', role: 'user' }; this.userDialogVisible = true },
    showEditUser(row) { this.userMode = 'edit'; this.currentUser = row; this.userForm = { username: row.username, password: '', role: row.role }; this.userDialogVisible = true },
    async saveUser() {
      this.$refs.userForm.validate(async valid => {
        if (!valid) return
        if (this.userMode === 'add') await createUser(this.userForm)
        else await updateUser(this.currentUser.id, this.userForm)
        this.$message.success('保存成功'); this.userDialogVisible = false; this.loadUsers()
      })
    },
    async handleDeleteUser(row) {
      await this.$confirm('确认删除该用户？', '提示', { type: 'warning' })
      await deleteUser(row.id); this.$message.success('删除成功'); this.loadUsers()
    }
  }
}
</script>