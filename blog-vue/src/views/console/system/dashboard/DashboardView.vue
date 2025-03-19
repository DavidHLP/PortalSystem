<template>
  <div class="dashboard-view">
    <h1 class="page-title">仪表盘</h1>

    <!-- 数据概览卡片 -->
    <el-row :gutter="20" class="data-overview">
      <el-col :xs="24" :sm="12" :md="6" v-for="(card, index) in overviewCards" :key="index">
        <el-card shadow="hover" class="overview-card" :body-style="{ padding: '20px' }">
          <div class="card-content">
            <el-icon class="card-icon" :style="{ color: card.color }">
              <component :is="card.icon" />
            </el-icon>
            <div class="card-info">
              <h3 class="card-title">{{ card.title }}</h3>
              <p class="card-value">{{ card.value }}</p>
              <p class="card-change" :class="card.trend">
                <el-icon v-if="card.trend === 'up'"><ArrowUp /></el-icon>
                <el-icon v-else><ArrowDown /></el-icon>
                {{ card.change }}
              </p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-section">
      <el-col :xs="24" :md="16">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="chart-header">
              <h3>访问趋势</h3>
              <el-radio-group v-model="timeRange" size="small">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">全年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <div class="chart-container" ref="visitChartRef"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="8">
        <el-card shadow="hover" class="chart-card">
          <template #header>
            <div class="chart-header">
              <h3>访问来源</h3>
            </div>
          </template>
          <div class="chart-container" ref="sourceChartRef"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近文章与系统信息 -->
    <el-row :gutter="20" class="bottom-section">
      <el-col :xs="24" :md="16">
        <el-card shadow="hover" class="recent-articles">
          <template #header>
            <div class="card-header">
              <h3>最近发布文章</h3>
              <el-button text>查看全部</el-button>
            </div>
          </template>
          <el-table :data="recentArticles" style="width: 100%" :show-header="false">
            <el-table-column>
              <template #default="scope">
                <div class="article-item">
                  <div class="article-title">{{ scope.row.title }}</div>
                  <div class="article-info">
                    <span>{{ scope.row.category }}</span>
                    <el-divider direction="vertical" />
                    <span>{{ scope.row.views }} 阅读</span>
                    <el-divider direction="vertical" />
                    <span>{{ scope.row.date }}</span>
                  </div>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="8">
        <el-card shadow="hover" class="system-info">
          <template #header>
            <div class="card-header">
              <h3>系统信息</h3>
            </div>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="系统版本">v1.0.0</el-descriptions-item>
            <el-descriptions-item label="服务器状态">
              <el-tag type="success">正常</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="数据库状态">
              <el-tag type="success">正常</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="上次更新">2023-06-15 14:30</el-descriptions-item>
            <el-descriptions-item label="当前用户等级">
              <el-tag type="warning">管理员</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup name="dashboardView">
import { ref, onMounted, onUnmounted, shallowRef, nextTick, watch } from 'vue'
// ECharts按需导入
import * as echarts from 'echarts'
import { ArrowUp, ArrowDown, View, ChatLineSquare, Document, User } from '@element-plus/icons-vue'

// 数据概览卡片数据
const overviewCards = ref([
  {
    title: '文章总数',
    value: '128',
    change: '12% 比上月',
    trend: 'up',
    icon: 'Document',
    color: '#409EFF'
  },
  {
    title: '评论总数',
    value: '584',
    change: '8% 比上月',
    trend: 'up',
    icon: 'ChatLineSquare',
    color: '#67C23A'
  },
  {
    title: '访客数量',
    value: '8,267',
    change: '5% 比上月',
    trend: 'up',
    icon: 'User',
    color: '#E6A23C'
  },
  {
    title: '总浏览量',
    value: '24,689',
    change: '3% 比上月',
    trend: 'down',
    icon: 'View',
    color: '#F56C6C'
  }
])

// 最近文章数据
const recentArticles = ref([
  {
    title: '如何使用Vue3和TypeScript构建现代Web应用',
    category: '前端开发',
    views: 1243,
    date: '2023-06-10'
  },
  {
    title: 'Spring Boot 3.0新特性详解',
    category: '后端开发',
    views: 982,
    date: '2023-06-08'
  },
  {
    title: 'Docker容器化部署实践指南',
    category: '运维技术',
    views: 756,
    date: '2023-06-05'
  },
  {
    title: '深入理解JavaScript异步编程',
    category: '前端开发',
    views: 1567,
    date: '2023-06-01'
  },
  {
    title: '数据结构与算法：从入门到精通',
    category: '编程基础',
    views: 2134,
    date: '2023-05-28'
  }
])

// 图表相关
const visitChartRef = ref()
const sourceChartRef = ref()
const timeRange = ref('month')
const visitChart = shallowRef<any>(null)
const sourceChart = shallowRef<any>(null)

// 初始化访问趋势图表
const initVisitChart = () => {
  if (!visitChartRef.value) return

  visitChart.value = echarts.init(visitChartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['访问量', '用户量']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '访问量',
        type: 'line',
        data: [820, 932, 901, 934, 1290, 1330, 1320],
        smooth: true,
        lineStyle: {
          color: '#409EFF',
          width: 3
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: 'rgba(64, 158, 255, 0.7)'
              },
              {
                offset: 1,
                color: 'rgba(64, 158, 255, 0.1)'
              }
            ]
          }
        }
      },
      {
        name: '用户量',
        type: 'line',
        data: [320, 332, 301, 334, 390, 330, 320],
        smooth: true,
        lineStyle: {
          color: '#67C23A',
          width: 3
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              {
                offset: 0,
                color: 'rgba(103, 194, 58, 0.7)'
              },
              {
                offset: 1,
                color: 'rgba(103, 194, 58, 0.1)'
              }
            ]
          }
        }
      }
    ]
  }

  visitChart.value.setOption(option)
}

// 初始化访问来源图表
const initSourceChart = () => {
  if (!sourceChartRef.value) return

  sourceChart.value = echarts.init(sourceChartRef.value)
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center',
      data: ['搜索引擎', '直接访问', '社交媒体', '外部链接', '其他']
    },
    series: [
      {
        name: '访问来源',
        type: 'pie',
        radius: ['50%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '14',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          { value: 1048, name: '搜索引擎' },
          { value: 735, name: '直接访问' },
          { value: 580, name: '社交媒体' },
          { value: 484, name: '外部链接' },
          { value: 300, name: '其他' }
        ]
      }
    ]
  }

  sourceChart.value.setOption(option)
}

// 监听窗口大小变化，调整图表尺寸
const resizeCharts = () => {
  visitChart.value?.resize()
  sourceChart.value?.resize()
}

onMounted(() => {
  nextTick(() => {
    initVisitChart()
    initSourceChart()
    window.addEventListener('resize', resizeCharts)
  })
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeCharts)
  visitChart.value?.dispose()
  sourceChart.value?.dispose()
})

// 监听时间范围变化，更新图表数据
watch(timeRange, (newValue: string) => {
  let xAxisData: string[] = []
  let visitData: number[] = []
  let userData: number[] = []

  if (newValue === 'week') {
    xAxisData = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    visitData = [820, 932, 901, 934, 1290, 1330, 1320]
    userData = [320, 332, 301, 334, 390, 330, 320]
  } else if (newValue === 'month') {
    xAxisData = [...Array(30).keys()].map(i => `${i + 1}日`)
    visitData = [...Array(30).keys()].map(() => Math.floor(Math.random() * 1000 + 500))
    userData = [...Array(30).keys()].map(() => Math.floor(Math.random() * 500 + 200))
  } else if (newValue === 'year') {
    xAxisData = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
    visitData = [2520, 2200, 2880, 3010, 3320, 3600, 3290, 3180, 3550, 3820, 4380, 4500]
    userData = [1520, 1320, 1580, 1710, 1820, 1950, 1790, 1680, 1880, 1920, 2080, 2300]
  }

  visitChart.value?.setOption({
    xAxis: {
      data: xAxisData
    },
    series: [
      {
        name: '访问量',
        data: visitData
      },
      {
        name: '用户量',
        data: userData
      }
    ]
  })
})
</script>

<style lang="scss" scoped>
.dashboard-view {
  padding: 20px;
}

.page-title {
  font-size: 24px;
  margin-bottom: 24px;
  color: $text-primary;
  font-weight: $font-weight-semibold;
}

.data-overview {
  margin-bottom: 20px;
}

.overview-card {
  height: 120px;
  transition: transform 0.3s;

  &:hover {
    transform: translateY(-5px);
  }

  .card-content {
    display: flex;
    align-items: center;
  }

  .card-icon {
    font-size: 48px;
    margin-right: 16px;
  }

  .card-info {
    flex: 1;
  }

  .card-title {
    font-size: $font-size-small;
    color: $text-tertiary;
    margin: 0 0 8px;
  }

  .card-value {
    font-size: 24px;
    font-weight: $font-weight-bold;
    margin: 0 0 4px;
    color: $text-primary;
  }

  .card-change {
    font-size: $font-size-small;
    margin: 0;
    display: flex;
    align-items: center;

    &.up {
      color: #67C23A;
    }

    &.down {
      color: #F56C6C;
    }

    .el-icon {
      margin-right: 4px;
    }
  }
}

.chart-section, .bottom-section {
  margin-bottom: 20px;
}

.chart-card {
  margin-bottom: 20px;

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    h3 {
      margin: 0;
      font-size: $font-size-large;
      font-weight: $font-weight-medium;
    }
  }

  .chart-container {
    height: 350px;
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  h3 {
    margin: 0;
    font-size: $font-size-large;
    font-weight: $font-weight-medium;
  }
}

.recent-articles {
  .article-item {
    padding: 12px 0;
    border-bottom: 1px solid #ebeef5;

    &:last-child {
      border-bottom: none;
    }
  }

  .article-title {
    font-weight: $font-weight-medium;
    margin-bottom: 8px;
    color: $text-primary;
  }

  .article-info {
    font-size: $font-size-small;
    color: $text-tertiary;
    display: flex;
    align-items: center;
  }
}

.system-info {
  :deep(.el-descriptions__cell) {
    padding: 12px;
  }
}
</style>
