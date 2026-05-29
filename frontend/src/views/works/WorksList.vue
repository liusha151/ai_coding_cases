<template>
  <div>
    <el-card style="margin-bottom:16px">
      <el-form :inline="true" :model="query">
        <el-form-item label="姓名"><el-input v-model="query.authorName" placeholder="姓名" style="width:150px"></el-input></el-form-item>
        <el-form-item label="著作类型">
          <dict-select type-code="work_type" v-model="query.workType"></dict-select>
        </el-form-item>
        <el-form-item label="名称"><el-input v-model="query.workName" placeholder="名称" style="width:150px"></el-input></el-form-item>
        <el-form-item label="状态">
          <dict-select type-code="work_status" v-model="query.status"></dict-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
          <el-button @click="reset">重置</el-button>
          <el-button type="success" @click="showAdd">新增著作</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <el-table :data="list" border stripe @row-dblclick="showDetail">
        <el-table-column prop="workNo" label="工号" width="100"></el-table-column>
        <el-table-column prop="authorName" label="姓名" width="100"></el-table-column>
        <el-table-column prop="workType" label="著作类型" width="120"></el-table-column>
        <el-table-column prop="workName" label="名称" min-width="180"></el-table-column>
        <el-table-column prop="publishPath" label="发表路径" min-width="150"></el-table-column>
        <el-table-column prop="status" label="状态" width="100"></el-table-column>
        <el-table-column prop="personalRank" label="个人排名" width="80"></el-table-column>
        <el-table-column prop="coAuthors" label="著作人" width="150"></el-table-column>
        <el-table-column prop="acquireDate" label="取得时间" width="110"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="showDetail(scope.row)">详情</el-button>
            <el-button size="mini" type="primary" @click="showEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @current-change="handlePageChange" :current-page="page" :total="total" :page-size="size" layout="total,prev,pager,next" style="margin-top:16px;text-align:right"></el-pagination>
    </el-card>
    <works-form ref="worksForm" :visible.sync="formVisible" :mode="formMode" :row="currentRow" @saved="search"></works-form>
    <works-detail ref="worksDetail" :visible.sync="detailVisible" :row="currentRow"></works-detail>
  </div>
</template>
<script>
import { getWorksList, deleteWorks } from '../../api/works'
import DictSelect from '../../components/DictSelect'
import WorksForm from './WorksForm'
import WorksDetail from './WorksDetail'
export default {
  components: { DictSelect, WorksForm, WorksDetail },
  data() {
    return {
      query: { authorName: '', workType: '', workName: '', status: '' },
      list: [], page: 1, size: 10, total: 0,
      formVisible: false, formMode: 'add', currentRow: null,
      detailVisible: false
    }
  },
  created() { this.search() },
  methods: {
    async search() {
      this.page = 1
      const res = await getWorksList({ ...this.query, page: this.page, size: this.size })
      this.list = res.data.list; this.total = res.data.total
    },
    async handlePageChange(p) {
      this.page = p
      const res = await getWorksList({ ...this.query, page: this.page, size: this.size })
      this.list = res.data.list; this.total = res.data.total
    },
    reset() { this.query = { authorName: '', workType: '', workName: '', status: '' }; this.search() },
    showAdd() { this.formMode = 'add'; this.currentRow = null; this.formVisible = true },
    showEdit(row) { this.formMode = 'edit'; this.currentRow = row; this.formVisible = true },
    showDetail(row) { this.currentRow = row; this.detailVisible = true },
    async handleDelete(row) {
      await this.$confirm('确认删除该著作信息？', '提示', { type: 'warning' })
      await deleteWorks(row.id)
      this.$message.success('删除成功'); this.search()
    }
  }
}
</script>
