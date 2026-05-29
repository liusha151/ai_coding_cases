<template>
  <div>
    <el-card style="margin-bottom:16px">
      <el-form :inline="true" :model="query">
        <el-form-item label="姓名">
          <el-select v-model="query.authorName" filterable clearable>
            <el-option v-for="n in authorNames" :key="n" :label="n" :value="n"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="著作类型">
          <dict-select type-code="work_type" v-model="query.workType"></dict-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadChart">查询</el-button>
          <el-button @click="query={authorName:'',workType:''};loadChart()">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-row :gutter="16">
      <el-col :span="12" v-for="(item,idx) in charts" :key="idx" style="margin-bottom:16px">
        <el-card><div :ref="'chart'+idx" style="height:300px"></div></el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import * as echarts from 'echarts'
import { getStatistics } from '../../api/statistics'
import { getAuthorNames } from '../../api/works'
import DictSelect from '../../components/DictSelect'
export default {
  components: { DictSelect },
  data() {
    return {
      query: { authorName: '', workType: '' },
      authorNames: [],
      charts: [
        { title: '按著作类型', key: 'byWorkType' },
        { title: '按状态', key: 'byStatus' },
        { title: '按排名', key: 'byRank' },
        { title: '按取得年份', key: 'byYear' }
      ]
    }
  },
  async created() {
    const res = await getAuthorNames()
    this.authorNames = res.data || []
    this.$nextTick(() => this.loadChart())
  },
  methods: {
    async loadChart() {
      const res = await getStatistics(this.query)
      const data = res.data
      this.charts.forEach((item, idx) => {
        const dom = this.$refs['chart' + idx]
        if (!dom || dom.length === 0) return
        const chart = echarts.init(dom[0])
        const list = data[item.key] || []
        chart.setOption({
          title: { text: item.title, left: 'center' },
          tooltip: { trigger: 'item' },
          xAxis: { type: 'category', data: list.map(i => i.name) },
          yAxis: { type: 'value' },
          series: [{ type: 'bar', data: list.map(i => i.value), itemStyle: { color: '#409eff' } }]
        })
      })
    }
  }
}
</script>
