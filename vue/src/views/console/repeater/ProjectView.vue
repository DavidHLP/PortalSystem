<template>
  <div class="app-container">
    <!-- 搜索工具栏 -->
    <el-card class="search-form-container">
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <el-icon>
              <Search />
            </el-icon> 搜索
          </el-button>
          <el-button @click="resetQuery">
            <el-icon>
              <Refresh />
            </el-icon> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作工具栏 -->
    <el-card class="table-container">
      <template #header>
        <div class="card-header">
          <span class="card-title">项目列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon>
              <Plus />
            </el-icon> 新增
          </el-button>
        </div>
      </template>

      <!-- 表格数据 -->
      <el-table v-loading="loading" :data="projectList" border stripe>
        <el-table-column label="序号" prop="id" width="60" align="center" />
        <el-table-column label="项目名称" prop="projectName" min-width="180" :show-overflow-tooltip="true" />
        <el-table-column label="文档说明" prop="doc" min-width="220" :show-overflow-tooltip="false">
          <template #default="scope">
            <div class="doc-preview">
              {{ truncateDoc(scope.row.doc != null && scope.row.doc.length > 20 ? scope.row.doc.slice(0, 20) : scope.row.doc) }}
              <el-button v-if="scope.row.doc" type="primary" link @click="viewFullDoc(scope.row.doc)">
                查看详情
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="gmtCreate" min-width="160" align="center">
          <template #default="scope">
            {{ formatDateTime(scope.row.gmtCreate) }}
          </template>
        </el-table-column>
        <el-table-column label="修改时间" prop="gmtModified" min-width="160" align="center">
          <template #default="scope">
            {{ formatDateTime(scope.row.gmtModified) }}
          </template>
        </el-table-column>
        <el-table-column label="角色" prop="roleUrls" min-width="160" align="center">
          <template #default="scope">
            <el-button type="success" link @click="handleViewRoles(scope.row)">
              查看角色
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="180" align="center">
          <template #default="scope">
            <el-button type="primary" link @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="danger" link @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-pagination class="pagination" v-model:current-page="pageInfo.pageNum" v-model:page-size="pageInfo.pageSize"
        :page-sizes="[10, 20, 50, 100]" :total="pageInfo.total" layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-card>

    <!-- 项目编辑组件 -->
    <ProjectComponents ref="projectFormRef" :project="editProject" @success="getProjectList" />

    <!-- Markdown文档查看抽屉 -->
    <el-drawer v-model="docDrawerVisible" title="文档详情" size="70%" direction="rtl" destroy-on-close>
      <MarkdownView :content="currentDoc" :theme="'light'" />
    </el-drawer>

    <!-- 项目角色抽屉组件 -->
    <ProjectRoleDrawer ref="roleDrawerRef" :project="currentProject" />
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import type { Project, ProjectRoleDTO } from '@/types/repeater/project'
import type { PageInfo } from '@/types/common'
import { listProject, deleteProject } from '@/api/repeater/project'
import ProjectComponents from '@/components/repeater/ProjectComponents.vue'
import MarkdownView from '@/components/markdown/MarkdownView.vue'
import ProjectRoleDrawer from '@/components/repeater/ProjectRoleDrawer.vue'

/**
 * 格式化日期时间
 * @param time 时间戳
 * @returns 格式化后的日期时间字符串
 */
const formatDateTime = (time: string | number | Date): string => {
  if (!time) {
    return '';
  }
  const date = new Date(time);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// 加载状态
const loading = ref(false)
// 项目列表
const projectList = ref<ProjectRoleDTO[]>([])
// 编辑的项目对象
const editProject = ref<Project>()
// 项目表单组件引用
const projectFormRef = ref()

// 分页参数
const pageInfo = reactive<PageInfo<Project>>({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  item: {} as Project
})

// 查询参数
const queryParams = reactive<Project>({
  id: 0,
  projectName: '',
  doc: '',
  gmtCreate: '',
  gmtModified: '',
  isDeleted: 0
})

// 文档相关
const docDrawerVisible = ref(false)
const currentDoc = ref('')

// 角色相关
const roleDrawerRef = ref()
const currentProject = ref<ProjectRoleDTO>()

/**
 * 获取项目列表
 */
const getProjectList = async () => {
  loading.value = true
  try {
    // 设置查询条件
    pageInfo.item = {
      id: queryParams.id,
      projectName: queryParams.projectName,
      doc: queryParams.doc,
      gmtCreate: queryParams.gmtCreate,
      gmtModified: queryParams.gmtModified,
      isDeleted: queryParams.isDeleted
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
  editProject.value = {} as Project
  projectFormRef.value.open()
}

/**
 * 处理编辑
 */
const handleEdit = (row: ProjectRoleDTO) => {
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

/**
 * 截取文档内容
 * @param doc 文档内容
 * @returns 截取后的文档内容
 */
const truncateDoc = (doc: string): string => {
  if (!doc) return '';
  const maxLength = 50;
  return doc.length > maxLength ? doc.substring(0, maxLength) + '...' : doc;
}

/**
 * 查看完整文档
 * @param doc 文档内容
 */
const viewFullDoc = (doc: string) => {
  currentDoc.value = doc;
  docDrawerVisible.value = true;
}

/**
 * 查看项目角色
 * @param row 项目信息
 */
const handleViewRoles = (row: ProjectRoleDTO) => {
  currentProject.value = row;
  roleDrawerRef.value.open();
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
