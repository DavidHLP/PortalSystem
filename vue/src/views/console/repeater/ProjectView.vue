<template>
  <div class="app-container">
    <!-- 搜索工具栏 -->
    <el-card class="search-form-container">
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="项目名称" prop="projectName">
          <el-input
            v-model="queryParams.projectName"
            placeholder="请输入项目名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <el-icon><Search /></el-icon> 搜索
          </el-button>
          <el-button @click="resetQuery">
            <el-icon><Refresh /></el-icon> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作工具栏 -->
    <el-card class="table-container">
      <template #header>
        <div class="card-header">
          <span class="card-title">项目列表</span>
          <el-button
            type="primary"
            @click="handleAdd"
          >
            <el-icon><Plus /></el-icon> 新增
          </el-button>
        </div>
      </template>

      <!-- 表格数据 -->
      <el-table
        v-loading="loading"
        :data="projectList"
        border
        stripe
      >
        <el-table-column
          label="序号"
          type="index"
          width="60"
          align="center"
        />
        <el-table-column
          label="项目名称"
          prop="projectName"
          min-width="180"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="文档说明"
          prop="doc"
          min-width="220"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="创建时间"
          prop="gmtCreate"
          min-width="160"
          align="center"
        />
        <el-table-column
          label="修改时间"
          prop="gmtModified"
          min-width="160"
          align="center"
        />
        <el-table-column
          label="操作"
          fixed="right"
          width="180"
          align="center"
        >
          <template #default="scope">
            <el-button
              type="primary"
              link
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              link
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-pagination
        class="pagination"
        v-model:current-page="pageInfo.pageNum"
        v-model:page-size="pageInfo.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pageInfo.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 项目编辑组件 -->
    <ProjectComponents
      ref="projectFormRef"
      :project="editProject"
      @success="getProjectList"
    />
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import type { Project } from '@/types/repeater/project'
import type { PageInfo } from '@/types/common'
import { listProject, deleteProject } from '@/api/repeater/project'
import ProjectComponents from '@/components/repeater/ProjectComponents.vue'

// 加载状态
const loading = ref(false)
// 项目列表
const projectList = ref<Project[]>([])
// 编辑的项目对象
const editProject = ref<Project>()
// 项目表单组件引用
const projectFormRef = ref()

// 分页参数
const pageInfo = reactive<PageInfo<Project>>({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  item: {}
})

// 查询参数
const queryParams = reactive<Project>({
  projectName: ''
})

/**
 * 获取项目列表
 */
const getProjectList = async () => {
  loading.value = true
  try {
    // 设置查询条件
    pageInfo.item = {
      projectName: queryParams.projectName
    }

    const res = await listProject(pageInfo)
    projectList.value = res.items || []
    pageInfo.total = res.total || 0
  } catch (error) {
    console.error('获取项目列表失败', error)
    ElMessage.error('获取项目列表失败')
  } finally {
    loading.value = false
  }
}

/**
 * 处理查询
 */
const handleQuery = () => {
  pageInfo.pageNum = 1
  getProjectList()
}

/**
 * 重置查询
 */
const resetQuery = () => {
  queryParams.projectName = ''
  handleQuery()
}

/**
 * 处理每页条数变化
 */
const handleSizeChange = (size: number) => {
  pageInfo.pageSize = size
  getProjectList()
}

/**
 * 处理页码变化
 */
const handleCurrentChange = (page: number) => {
  pageInfo.pageNum = page
  getProjectList()
}

/**
 * 处理添加
 */
const handleAdd = () => {
  editProject.value = {}
  projectFormRef.value.open()
}

/**
 * 处理编辑
 */
const handleEdit = (row: Project) => {
  editProject.value = { ...row }
  projectFormRef.value.open()
}

/**
 * 处理删除
 */
const handleDelete = (row: Project) => {
  ElMessageBox.confirm(
    `确认删除项目"${row.projectName}"吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteProject({ id: row.id })
      ElMessage.success('删除成功')
      getProjectList()
    } catch (error) {
      console.error('删除失败', error)
      ElMessage.error('删除失败，请稍后重试')
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 组件挂载时获取数据
onMounted(() => {
  getProjectList()
})
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.search-form-container {
  margin-bottom: 20px;
}

.table-container {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: var(--font-size-large);
  font-weight: var(--font-weight-semibold);
  color: var(--text-color-primary);
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
