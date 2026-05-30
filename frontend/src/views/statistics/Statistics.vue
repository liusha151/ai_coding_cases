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
      /* 图表配置：前三个（著作类型、状态、排名）使用饼图，第四个（年份）使用柱状图 */
      charts: [
        { title: '按著作类型', key: 'byWorkType', type: 'pie' },
        { title: '按状态', key: 'byStatus', type: 'pie' },
        { title: '按排名', key: 'byRank', type: 'pie' },
        { title: '按取得年份', key: 'byYear', type: 'bar' }
      ]
    }
  },
  async created() {
    const res = await getAuthorNames()
    this.authorNames = res.data || []
    this.$nextTick(() => this.loadChart())
  },
  methods: {
    /* 加载统计数据并渲染图表 */
    async loadChart() {
      const res = await getStatistics(this.query)
      const data = res.data
      this.charts.forEach((item, idx) => {
        const dom = this.$refs['chart' + idx]
        if (!dom || dom.length === 0) return
        const chart = echarts.init(dom[0])
        const list = data[item.key] || []
        if (item.type === 'pie') {
          /* 饼图配置：名称作为图例，数值决定饼块大小 */
          chart.setOption({
            title: { text: item.title, left: 'center' },
            tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
            legend: { orient: 'vertical', left: 'left' },
            series: [{
              type: 'pie', radius: '50%', center: ['50%', '60%'],
              data: list.map(i => ({ name: i.name, value: i.value })),
              emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,0.5)' } }
            }]
          })
        } else {
          /* 柱状图配置：适用于按年份的时间维度展示 */
          chart.setOption({
            title: { text: item.title, left: 'center' },
            tooltip: { trigger: 'item' },
            xAxis: { type: 'category', data: list.map(i => i.name) },
            yAxis: { type: 'value' },
            series: [{ type: 'bar', data: list.map(i => i.value), itemStyle: { color: '#409eff' } }]
          })
        }
      })
    }
  }
}
</script>