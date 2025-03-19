<template>
  <div class="host-url-container">
    <!-- 搜索区域 -->
    <div class="search-box">
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="主机地址">
          <el-input v-model="queryParams.address" placeholder="输入主机地址/IP" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.isActive" placeholder="选择状态" clearable class="form-item equal-width">
            <el-option label="启用" :value="true"></el-option>
            <el-option label="停用" :value="false"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="primary" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <el-table :data="hostList" v-loading="loading" border style="width: 100%">
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="address" label="主机地址" min-width="180"></el-table-column>
      <el-table-column prop="description" label="描述" min-width="220">
        <template v-slot="scope">
          <div v-if="scope.row.description" class="description-container">
            <div class="description-text">{{ truncateText(scope.row.description, 100) }}</div>
            <el-button type="primary" link size="small" @click="handleViewFullDescription(scope.row)">
              查看完整描述
            </el-button>
          </div>
          <span v-else class="text-muted">暂无描述</span>
        </template>
      </el-table-column>
      <el-table-column prop="isActive" label="状态">
        <template v-slot="scope">
          <el-tag :type="scope.row.isActive ? 'success' : 'danger'">
            {{ scope.row.isActive ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template v-slot="scope">
          {{ formatDateTime(scope.row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" fixed="right">
        <template v-slot="scope">
          <el-button
            size="small"
            @click="handleEdit(scope.row)"
            type="primary"
            link
          >编辑</el-button>
          <el-button
            size="small"
            @click="handleDelete(scope.row)"
            type="danger"
            link
          >删除</el-button>
          <el-button
            size="small"
            @click="handleToggleStatus(scope.row)"
            :type="scope.row.isActive ? 'warning' : 'success'"
            link
          >
            {{ scope.row.isActive ? '停用' : '启用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        background
        layout="total, sizes, prev, pager, next, jumper"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryParams.limit"
        :current-page="queryParams.page"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      ></el-pagination>
    </div>

    <!-- 新增/编辑抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      :title="dialogTitle"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
       size="60%"
      destroy-on-close
    >
      <div class="drawer-content">
        <el-form
          :model="formData"
          :rules="formRules"
          ref="formRef"
          label-width="100px"
          class="drawer-form"
        >
          <el-form-item label="主机地址" prop="address">
            <el-input v-model="formData.address" placeholder="请输入主机地址或IP"></el-input>
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <md-editor-element
              v-model="formData.description"
              :height="600"
              :autoFocus="false"
              :placeholder="'请输入主机用途描述，支持Markdown格式'"
            />
          </el-form-item>
          <el-form-item label="状态" prop="isActive">
            <el-switch
              v-model="formData.isActive"
              :active-value="true"
              :inactive-value="false"
              active-text="启用"
              inactive-text="停用"
            ></el-switch>
          </el-form-item>
        </el-form>
        <div class="drawer-footer">
          <el-button @click="drawerVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 完整描述对话框 -->
    <el-drawer
      v-model="descriptionDrawerVisible"
      title="完整描述"
      size="60%"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
      destroy-on-close
      direction="rtl"
    >
      <div class="full-description-container">
        <markdown-view v-if="currentDescription" :content="currentDescription" :showCatalog="true" />
      </div>
      <template #footer>
        <div class="drawer-footer">
          <el-button @click="descriptionDrawerVisible = false">关闭</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts" name="HostUrlComponent">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getHostList as fetchHostList,
  createHost,
  updateHost,
  deleteHost,
  updateHostStatus,
  type HostUrl,
  type QueryParams,
} from '@/api/repeater/host_url'
import MdEditorElement from '@/components/markdown/MdEditorElement.vue'
import MarkdownView from '@/components/markdown/MarkdownView.vue'

// 定义响应类型
interface ResponseResult<T> {
  code: number
  message?: string
  data?: T
}

// 表单引用
const formRef = ref(null)

// 表格数据
const hostList = ref<HostUrl[]>([])
const loading = ref(false)
const total = ref(0)

// 查询参数
const queryParams = reactive<QueryParams>({
  page: 1,
  limit: 10,
  address: '',
  isActive: null
})

// 表单数据
const formData = reactive<HostUrl>({
  id: null,
  address: '',
  description: '',
  isActive: true
})

// 表单校验规则
const formRules = {
  address: [
    { required: true, message: '请输入主机地址', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入描述', trigger: 'blur' },
    { max: 2000, message: '长度不能超过 2000 个字符', trigger: 'blur' }
  ]
}

// 抽屉控制
const drawerVisible = ref(false)
const dialogTitle = ref('新增主机')
const dialogType = ref('add') // add 或 edit

// 完整描述对话框
const descriptionDrawerVisible = ref(false)
const currentDescription = ref('')

// 初始化
onMounted(() => {
  getHostList()
})

// 获取主机列表
const getHostList = async () => {
  loading.value = true
  try {
    const res = await fetchHostList(queryParams)
    console.log('获取主机列表结果:', res)
    hostList.value = res.items
    total.value = res.total
  } catch (error) {
    console.error('获取主机列表失败:', error)
    ElMessage.error('获取主机列表失败')
  } finally {
    loading.value = false
  }
}

// 查询
const handleQuery = () => {
  queryParams.page = 1
  getHostList()
}

// 重置查询
const resetQuery = () => {
  queryParams.address = ''
  queryParams.isActive = null
  handleQuery()
}

// 处理分页大小变化
const handleSizeChange = (size: number) => {
  queryParams.limit = size
  getHostList()
}

// 处理页码变化
const handleCurrentChange = (page: number) => {
  queryParams.page = page
  getHostList()
}

// 新增主机
const handleAdd = () => {
  resetForm()
  dialogTitle.value = '新增主机'
  dialogType.value = 'add'
  drawerVisible.value = true
}

// 编辑主机
const handleEdit = (row: HostUrl) => {
  resetForm()
  dialogTitle.value = '编辑主机'
  dialogType.value = 'edit'
  nextTick(() => {
    formData.id = row.id
    formData.address = row.address
    formData.description = row.description
    formData.isActive = row.isActive
  })
  drawerVisible.value = true
}

// 删除主机
const handleDelete = (row: HostUrl) => {
  if (!row.id) return

  ElMessageBox.confirm(`确认删除主机 [${row.address}] 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteHost(row.id as number)
      getHostList()
    } catch (error) {
      console.error('删除主机失败:', error)
      ElMessage.error('删除主机失败')
    }
  }).catch(() => {})
}

// 切换状态
const handleToggleStatus = async (row: HostUrl) => {
  if (!row.id) return

  try {
    await updateHostStatus(row.id as number, !row.isActive)
    getHostList()
    ElMessage.success('状态更新成功')
  } catch (error) {
    console.error('更新状态失败:', error)
    ElMessage.error('更新状态失败')
  }
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    (formRef.value as any).resetFields()
  }
  formData.id = null
  formData.address = ''
  formData.description = ''
  formData.isActive = true
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return

  await (formRef.value as any).validate(async (valid: boolean) => {
    if (valid) {
      try {
        let res
        if (dialogType.value === 'add') {
          res = await createHost(formData)
        } else if (formData.id) {
          res = await updateHost(formData.id, formData)
        } else {
          ElMessage.error('表单数据异常')
          return
        }

          ElMessage.success('操作成功')
          drawerVisible.value = false
        getHostList()
      } catch (error) {
        console.error('保存主机失败:', error)
        ElMessage.error('保存主机失败')
      }
    } else {
      return false
    }
  })
}

// 格式化日期时间
const formatDateTime = (dateTime?: string) => {
  if (!dateTime) return ''

  const date = new Date(dateTime)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

// 查看完整描述
const handleViewFullDescription = (row: HostUrl) => {
  if (!row.description) return

  currentDescription.value = row.description
  descriptionDrawerVisible.value = true
}

// 添加截断文本的辅助函数
const truncateText = (text: string, maxLength: number) => {
  if (!text) return ''
  if (text.length <= maxLength) return text
  return text.substring(0, maxLength) + '...'
}
</script>

<style scoped lang="scss">
.host-url-container {
  padding: 20px;

  .search-box {
    margin-bottom: 20px;
    background-color: var(--vue-color-primary-light-9);
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    border: 1px solid var(--vue-color-primary-light-7);
    transition: all 0.3s;

    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      transform: translateY(-2px);
    }

    .el-form-item {
      margin-bottom: 0;
    }

    .el-button {
      margin-left: 8px;
      font-weight: var(--font-weight-medium);
    }
  }

  .el-table {
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

    .el-button--primary.el-button--link {
      color: var(--vue-color-primary);

      &:hover {
        color: var(--vue-color-primary-dark-1);
      }
    }

    .el-button--danger.el-button--link {
      color: var(--el-color-danger);

      &:hover {
        color: #ff4d4f;
      }
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
      margin-left: 8px;
      min-width: 90px;
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

  .form-tip {
    font-size: 12px;
    color: var(--text-color-tertiary);
    margin-top: 4px;
  }

  // 描述抽屉样式
  .description-drawer {
    :deep(.el-drawer__header) {
      margin-bottom: 0;
      padding: 16px 20px;
      background-color: var(--vue-color-primary-light-9);
      border-bottom: 1px solid var(--vue-color-primary-light-7);

      .el-drawer__title {
        font-weight: var(--font-weight-semibold);
        color: var(--text-color-primary);
      }
    }

    :deep(.el-drawer__body) {
      padding: 0;
      overflow-y: auto;
    }
  }

  .description-drawer-content {
    padding: 20px;
  }

  .description-body {
    padding: 0;
    border-radius: 8px;
    background-color: transparent;
    box-shadow: none;

    :deep(.markdown-preview-container) {
      display: flex;
      gap: 20px;

      .md-editor-catalog {
        max-width: 240px;
        border-left: 1px solid var(--el-border-color-light);
        padding-left: 16px;
      }
    }
  }
}

// 暗色模式适配
@media (prefers-color-scheme: dark) {
  .host-url-container {
    .search-box {
      background-color: rgba(66, 184, 131, 0.1);
      border-color: rgba(66, 184, 131, 0.2);
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
    }

    .el-table {
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

      .el-button--primary.el-button--link {
        color: #5ccb9c;

        &:hover {
          color: #7ad8b2;
        }
      }

      .el-button--danger.el-button--link {
        color: #ff9a9a;

        &:hover {
          color: #ffb6b6;
        }
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

    .description-drawer {
      :deep(.el-drawer__header) {
        background-color: rgba(35, 35, 35, 0.8);
        border-bottom-color: rgba(66, 184, 131, 0.2);

        .el-drawer__title {
          color: #e0e0e0;
        }
      }

      :deep(.markdown-preview-container) {
        .md-editor-catalog {
          border-left-color: rgba(66, 184, 131, 0.2);
        }
      }
    }
  }
}

.text-muted {
  color: var(--text-color-tertiary);
  font-style: italic;
}

.description-container {
  display: flex;
  align-items: center;
  width: 100%;
}

.description-text {
  flex: 1;
  margin-right: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 70%;
}

.full-description-container {
  padding: 20px;
  height: calc(100% - 60px);
  overflow-y: auto;
}

.drawer-footer {
  padding: 15px 20px;
  text-align: right;
  border-top: 1px solid var(--vue-color-secondary-light-8);
  background-color: var(--vue-color-secondary-light-9);
}

.form-item {
  &.equal-width {
    flex: 1;
    min-width: 150px;
  }

  &.double-width {
    flex: 2;
    min-width: 300px;
  }

  &.action-item {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
}
</style>
