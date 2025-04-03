<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-card class="search-form-container">
      <el-form :model="queryParams" ref="queryFormRef" :inline="true">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="queryParams.username" placeholder="请输入用户名" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="queryParams.email" placeholder="请输入邮箱" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="queryParams.roleName" placeholder="请输入角色名称" clearable @keyup.enter="handleQuery" />
        </el-form-item>
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

    <!-- 表格区域 -->
    <el-card class="table-container">
      <template #header>
        <div class="card-header">
          <span class="card-title">用户列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon>
              <Plus />
            </el-icon> 新增
          </el-button>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="userList"
        border
        stripe
        style="width: 100%"
        row-key="id"
      >
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="username" label="用户名" min-width="120" show-overflow-tooltip />
        <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip />
        <el-table-column prop="roleName" label="角色" min-width="120" show-overflow-tooltip />
        <el-table-column prop="projectName" label="项目" min-width="150" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === 0 ? 'success' : 'danger'">
              {{ scope.row.status === 0 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="角色文档" width="120" align="center">
          <template #default="scope">
            <el-button type="info" size="small" @click="handleViewRoleDoc(scope.row)" v-if="scope.row.roleDoc">角色文档</el-button>
          </template>
        </el-table-column>
        <el-table-column label="项目文档" width="120" align="center">
          <template #default="scope">
            <el-button type="info" size="small" @click="handleViewProjectDoc(scope.row)" v-if="scope.row.projectDoc">项目文档</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" align="center" fixed="right">
          <template #default="scope">
            <div class="action-buttons">
              <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button type="primary" size="small" @click="handlePassword(scope.row)">修改密码</el-button>
              <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页器 -->
      <el-pagination
        v-model:current-page="pageInfo.pageNum"
        v-model:page-size="pageInfo.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageInfo.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        class="pagination"
      />
    </el-card>

    <!-- 用户组件（包含添加/编辑/删除弹窗） -->
    <UserUrlComponents ref="userUrlComponentsRef" @refresh="getList" />

    <!-- 文档查看抽屉 -->
    <el-drawer
      :title="docDrawerTitle"
      v-model="docDrawerVisible"
      size="60%"
      :close-on-click-modal="true"
      direction="rtl"
    >
      <MarkdownView :content="currentDocContent" :showCatalog="true" theme="light" />
    </el-drawer>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import type { FormInstance } from 'element-plus'
import { ElMessage } from 'element-plus'
import { listUserRoleProjectByPage } from '@/api/repeater/userurl'
import type { UserRoleProject } from '@/api/repeater/userurl.ts'
import UserUrlComponents from '@/components/repeater/UserUrlComponents.vue'
import MarkdownView from '@/components/markdown/MarkdownView.vue'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'

// 表单引用
const queryFormRef = ref<FormInstance>()
const userUrlComponentsRef = ref<InstanceType<typeof UserUrlComponents>>()

// 数据加载状态
const loading = ref(false)

// 用户列表数据
const userList = ref<UserRoleProject[]>([])

// 文档查看相关
const docDrawerVisible = ref(false)
const docDrawerTitle = ref('文档查看')
const currentDocContent = ref('')

// 分页参数
const pageInfo = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  pages: 0,
  item: {
    username: '',
    email: '',
    roleName: '',
    projectName: '',
    projectDoc: '',
    roleDoc: ''
  }
})

// 查询参数
const queryParams = reactive({
  username: '',
  email: '',
  roleName: '',
  projectName: '',
  projectDoc: '',
  roleDoc: ''
})

// 获取用户列表
const getList = async () => {
  loading.value = true
  try {
    // 将查询参数赋值给分页查询条件
    pageInfo.item = {
      username: queryParams.username,
      email: queryParams.email,
      roleName: queryParams.roleName,
      projectName: queryParams.projectName,
      projectDoc: queryParams.projectDoc,
      roleDoc: queryParams.roleDoc
    }

    const res = await listUserRoleProjectByPage(pageInfo)
    userList.value = res.items || []
    pageInfo.total = res.total
    pageInfo.pages = res.pages
  } catch (error: any) {
    ElMessage.error(error.message || '获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 查看角色文档
const handleViewRoleDoc = (row: any) => {
  docDrawerTitle.value = `角色文档 - ${row.roleName}`
  currentDocContent.value = row.roleDoc || '暂无文档内容'
  docDrawerVisible.value = true
}

// 查看项目文档
const handleViewProjectDoc = (row: any) => {
  docDrawerTitle.value = `项目文档 - ${row.projectName}`
  currentDocContent.value = row.projectDoc || '暂无文档内容'
  docDrawerVisible.value = true
}

// 查询操作
const handleQuery = () => {
  pageInfo.pageNum = 1
  getList()
}

// 重置查询
const resetQuery = () => {
  if (queryFormRef.value) {
    queryFormRef.value.resetFields()
  }
  handleQuery()
}

// 新增用户
const handleAdd = () => {
  userUrlComponentsRef.value?.handleAdd()
}

// 编辑用户
const handleEdit = (row: any) => {
  userUrlComponentsRef.value?.handleEdit(row)
}

// 修改密码
const handlePassword = (row: any) => {
  userUrlComponentsRef.value?.handlePassword(row)
}

// 删除用户
const handleDelete = (row: any) => {
  userUrlComponentsRef.value?.handleDelete(row)
}

// 处理页码变化
const handleCurrentChange = () => {
  getList()
}

// 处理每页条数变化
const handleSizeChange = () => {
  pageInfo.pageNum = 1
  getList()
}

// 组件挂载完成后获取数据
onMounted(() => {
  getList()
})
</script>

<style scoped lang="scss">
.app-container {
  padding: 20px;
}

.search-form-container {
  margin-bottom: 20px;
}

.table-container {
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;

  :deep(.el-table) {
    --el-table-border-color: var(--el-border-color-lighter);
    --el-table-header-bg-color: var(--el-fill-color-light);

    th {
      font-weight: bold;
    }
  }
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

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 8px;
  flex-wrap: wrap;
}
</style>
