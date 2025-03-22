<template>
  <div class="router-url-container">
    <!-- 顶部卡片 -->
    <el-card class="router-url-card">
      <!-- 搜索区域 -->
      <div class="search-box">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-row :gutter="20" class="search-row">
            <el-col :xs="24" :sm="12" :md="8" :lg="8">
              <el-form-item label="路径">
                <el-input
                  v-model="searchForm.path"
                  placeholder="输入路由路径"
                  clearable
                  prefix-icon="Link"
                  @keyup.enter="handleSearch"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="24" :md="16" :lg="16">
              <el-form-item class="action-buttons">
                <el-button type="primary" @click="handleSearch" icon="Search">查询</el-button>
                <el-button @click="resetSearch" icon="RefreshRight">重置</el-button>
                <el-button type="primary" @click="handleAdd" icon="Plus">添加路由</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>

      <!-- 表格工具栏 -->
      <div class="table-toolbar">
        <div class="left-actions">
          <el-button-group>
            <el-button type="primary" plain icon="RefreshLeft" @click="fetchData">刷新</el-button>
            <el-button type="danger" plain icon="Delete" :disabled="selectedRoutes.length === 0" @click="handleBatchDelete">批量删除</el-button>
          </el-button-group>
          <span class="selected-count" v-if="selectedRoutes.length > 0">
            已选择 <el-tag type="info">{{ selectedRoutes.length }}</el-tag> 项
          </span>
        </div>
        <div class="right-actions">
          <el-tooltip content="密度" placement="top">
            <el-dropdown trigger="click">
              <el-button icon="Grid" circle plain></el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="tableSize = 'large'">宽松</el-dropdown-item>
                  <el-dropdown-item @click="tableSize = 'default'">默认</el-dropdown-item>
                  <el-dropdown-item @click="tableSize = 'small'">紧凑</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-tooltip>
        </div>
      </div>

      <!-- 数据表格 -->
      <el-table
        :data="routerUrls"
        border
        v-loading="loading"
        :size="tableSize"
        @selection-change="handleSelectionChange"
        row-key="id"
        class="router-table"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80" sortable />
        <el-table-column prop="path" label="路径" min-width="200" sortable>
          <template #default="scope">
            <el-tag size="medium" type="primary">{{ scope.row.path }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" sortable>
          <template #default="scope">
            <div class="time-info">
              <el-icon><Clock /></el-icon>
              <span>{{ formatDate(scope.row.createdAt) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button-group>
              <el-tooltip content="编辑" placement="top">
                <el-button
                  size="small"
                  @click="handleEdit(scope.row)"
                  type="primary"
                  icon="Edit"
                  circle
                ></el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button
                  size="small"
                  @click="handleDelete(scope.row)"
                  type="danger"
                  icon="Delete"
                  circle
                ></el-button>
              </el-tooltip>
            </el-button-group>
          </template>
        </el-table-column>
        <template #empty>
          <div class="empty-data">
            <el-empty description="暂无数据" :image-size="100">
              <el-button type="primary" @click="handleAdd">添加路由</el-button>
            </el-empty>
          </div>
        </template>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          :current-page="currentPage"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </el-card>

    <!-- 新增/编辑抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      :title="dialogType === 'add' ? '添加路由' : '编辑路由'"
      direction="rtl"
      size="40%"
      destroy-on-close
    >
      <div class="drawer-content">
        <el-form
          :model="form"
          :rules="formRules"
          ref="formRef"
          label-width="80px"
          class="drawer-form"
          status-icon
        >
          <el-alert
            v-if="dialogType === 'edit'"
            title="您正在编辑现有路由，请谨慎操作。"
            type="warning"
            :closable="false"
            show-icon
            style="margin-bottom: 20px;"
          ></el-alert>

          <el-form-item label="路径" prop="path">
            <el-input
              v-model="form.path"
              placeholder="请输入路由路径"
              prefix-icon="Link"
            />
          </el-form-item>
        </el-form>
        <div class="drawer-footer">
          <el-button @click="drawerVisible = false" icon="Close">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading" icon="Check">确定</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRouterUrls, addRouterUrl, updateRouterUrl, deleteRouterUrl } from '@/api/repeater/routerurl'

// 表格数据
const routerUrls = ref([])
const loading = ref(false)
const tableSize = ref('default')
const selectedRoutes = ref([])
const submitLoading = ref(false)

// 抽屉控制
const drawerVisible = ref(false)
const dialogType = ref('add')

// 表单数据
const form = ref({
  id: null,
  path: ''
})

// 表单校验规则
const formRules = {
  path: [
    { required: true, message: '请输入路由路径', trigger: 'blur' },
    { min: 1, max: 200, message: '长度不能超过200个字符', trigger: 'blur' }
  ]
}

// 表单引用
const formRef = ref(null)

// 搜索表单
const searchForm = reactive({
  path: ''
})

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 获取路由列表
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      path: searchForm.path || undefined
    }
    const response = await getRouterUrls(params)
    routerUrls.value = response.items
    total.value = response.total
  } catch (error) {
    console.error('获取路由列表失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 处理表格选择变化
const handleSelectionChange = (selection) => {
  selectedRoutes.value = selection
}

// 批量删除
const handleBatchDelete = () => {
  if (selectedRoutes.value.length === 0) return

  ElMessageBox.confirm(`确认删除选中的 ${selectedRoutes.value.length} 个路由吗？`, '批量删除', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 循环单个删除
      const promises = selectedRoutes.value
        .filter(route => route.id)
        .map(route => deleteRouterUrl(route.id))

      await Promise.all(promises)
      ElMessage.success(`成功删除 ${selectedRoutes.value.length} 个路由`)
      fetchData()
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }).catch(() => {})
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchData()
}

// 重置搜索
const resetSearch = () => {
  searchForm.path = ''
  handleSearch()
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  fetchData()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  fetchData()
}

// 新增路由
const handleAdd = () => {
  dialogType.value = 'add'
  resetForm()
  drawerVisible.value = true
}

// 编辑路由
const handleEdit = (row) => {
  dialogType.value = 'edit'
  resetForm()
  form.value = { ...row }
  drawerVisible.value = true
}

// 删除路由
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该路由吗？', '提示', {
      type: 'warning'
    })
    await deleteRouterUrl(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 重置表单
const resetForm = () => {
  form.value = { id: null, path: '' }
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (dialogType.value === 'add') {
          await addRouterUrl(form.value)
          ElMessage.success('添加成功')
        } else {
          await updateRouterUrl(form.value.id, form.value)
          ElMessage.success('更新成功')
        }
        drawerVisible.value = false
        fetchData()
      } catch (error) {
        console.error('提交表单失败:', error)
        ElMessage.error(dialogType.value === 'add' ? '添加失败' : '更新失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleString()
}

// 初始化
onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.router-url-container {
  padding: 20px;

  .router-url-card {
    margin-bottom: 20px;
    transition: all 0.3s;
    border-radius: 8px;
    overflow: hidden;
  }

  .search-box {
    margin-bottom: 20px;
    background-color: var(--vue-color-primary-light-9, #f0faf5);
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    border: 1px solid var(--vue-color-primary-light-7, #c1e7d5);
    transition: all 0.3s;

    &:hover {
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
      transform: translateY(-2px);
    }

    .search-row {
      width: 100%;
    }

    .action-buttons {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      margin-bottom: 0;

      @media (max-width: 768px) {
        justify-content: center;
        margin-top: 10px;
      }
    }

    .el-form-item {
      margin-bottom: 0;
      width: 100%;
    }
  }

  .table-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    .left-actions, .right-actions {
      display: flex;
      align-items: center;
      gap: 12px;
    }

    .selected-count {
      margin-left: 16px;
      font-size: 14px;
    }

    @media (max-width: 768px) {
      flex-direction: column;
      gap: 10px;

      .left-actions, .right-actions {
        width: 100%;
        justify-content: space-between;
      }
    }
  }

  .router-table {
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
    margin-bottom: 15px;

    :deep(th.el-table__cell) {
      background-color: var(--vue-color-primary-light-9);
      color: var(--text-color-primary);
      font-weight: var(--font-weight-semibold);
      padding: 14px 12px;
    }

    :deep(td.el-table__cell) {
      color: var(--text-color-secondary);
      padding: 14px 12px;
    }

    .time-info {
      display: flex;
      align-items: center;
      gap: 5px;
    }

    .empty-data {
      padding: 40px 0;
    }
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;

    .el-pagination {
      padding: 0;
      font-weight: normal;
    }
  }

  // 抽屉样式
  :deep(.el-drawer) {
    .el-drawer__header {
      margin-bottom: 0;
      padding: 16px 20px;
      background-color: var(--vue-color-primary-light-9);
      border-bottom: 1px solid var(--vue-color-primary-light-7);

      .el-drawer__title {
        color: var(--text-color-primary);
        font-weight: var(--font-weight-semibold);
        font-size: var(--font-size-large);
      }
    }

    .el-drawer__body {
      padding: 0;
    }
  }

  .drawer-content {
    height: 100%;
    display: flex;
    flex-direction: column;
  }

  .drawer-form {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
  }

  .drawer-footer {
    padding: 15px 20px;
    text-align: right;
    border-top: 1px solid var(--vue-color-secondary-light-8);
    background-color: var(--vue-color-secondary-light-9);

    .el-button {
      min-width: 90px;
      margin-left: 12px;
      transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
      position: relative;
      overflow: hidden;

      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: linear-gradient(120deg, rgba(255,255,255,0) 30%, rgba(255,255,255,0.4) 50%, rgba(255,255,255,0) 70%);
        transform: translateX(-100%);
        transition: all 0.6s ease;
      }

      &--primary {
        background: linear-gradient(135deg, var(--vue-color-primary), var(--vue-color-primary-light-1));
        border: none;
        color: white;
        box-shadow: 0 4px 10px rgba(66, 184, 131, 0.3);

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 6px 15px rgba(66, 184, 131, 0.4);

          &::before {
            transform: translateX(100%);
          }
        }

        &:active {
          transform: translateY(0);
          box-shadow: 0 2px 8px rgba(66, 184, 131, 0.3);
        }
      }

      &:not(.el-button--primary) {
        border: 1px solid var(--el-border-color);
        background: white;

        &:hover {
          border-color: var(--vue-color-primary-light-5);
          color: var(--vue-color-primary);
          transform: translateY(-2px);
          box-shadow: 0 6px 15px rgba(0, 0, 0, 0.05);

          &::before {
            transform: translateX(100%);
          }
        }

        &:active {
          transform: translateY(0);
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        }
      }
    }
  }
}

// 暗色模式适配
@media (prefers-color-scheme: dark) {
  .router-url-container {
    .search-box {
      background-color: rgba(66, 184, 131, 0.1);
      border-color: rgba(66, 184, 131, 0.2);
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
    }

    .router-table {
      background-color: var(--el-bg-color);
      color: var(--el-text-color-primary);
      border: 1px solid rgba(66, 184, 131, 0.2);

      :deep(th.el-table__cell) {
        background-color: rgba(66, 184, 131, 0.15);
        border-bottom: 2px solid rgba(66, 184, 131, 0.3);
        color: #ffffff;
      }

      :deep(td.el-table__cell) {
        border-bottom: 1px solid rgba(255, 255, 255, 0.05);
      }
    }

    .drawer-footer {
      background-color: rgba(35, 35, 35, 0.8);
      border-top-color: rgba(66, 184, 131, 0.2);

      .el-button--primary {
        background: linear-gradient(135deg, #3ba676, #42b883);
        box-shadow: 0 4px 10px rgba(45, 125, 89, 0.4);

        &:hover {
          background: linear-gradient(135deg, #42b883, #3ba676);
          box-shadow: 0 6px 15px rgba(45, 125, 89, 0.5);
        }
      }

      .el-button:not(.el-button--primary) {
        background: rgba(40, 40, 40, 0.8);
        border-color: rgba(80, 80, 80, 0.5);
        color: #e0e0e0;

        &:hover {
          background: rgba(50, 50, 50, 0.9);
          border-color: rgba(66, 184, 131, 0.5);
          color: #5ccb9c;
        }
      }
    }
  }
}

@media screen and (max-width: 768px) {
  .router-url-container {
    padding: 10px;

    .router-table {
      :deep(.el-table__cell) {
        padding: 10px 8px;
      }
    }

    :deep(.el-drawer) {
      width: 90% !important;
    }
  }
}
</style>
