<template>
  <div class="port-url-container">
    <!-- 顶部卡片 -->
    <el-card class="port-url-card">
      <!-- 搜索区域 -->
      <div class="search-box">
        <el-form :inline="true" :model="queryParams" class="demo-form-inline">
          <el-row :gutter="20" class="search-row">
            <el-col :xs="24" :sm="8" :md="8" :lg="6">
              <el-form-item label="端口号">
                <el-input
                  v-model="queryParams.number"
                  placeholder="输入端口号"
                  clearable
                  prefix-icon="Search"
                  @keyup.enter="handleQuery"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="16" :md="16" :lg="18">
              <el-form-item class="action-buttons">
                <el-button type="primary" @click="handleQuery" icon="Search">查询</el-button>
                <el-button @click="resetQuery" icon="RefreshRight">重置</el-button>
                <el-button type="primary" @click="handleAdd" icon="Plus">新增端口</el-button>
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
            <el-button type="danger" plain icon="Delete" :disabled="selectedPorts.length === 0" @click="handleBatchDelete">批量删除</el-button>
          </el-button-group>
          <span class="selected-count" v-if="selectedPorts.length > 0">
            已选择 <el-tag type="info">{{ selectedPorts.length }}</el-tag> 项
          </span>
        </div>
        <div class="right-actions">
          <el-tooltip content="列设置" placement="top">
            <el-button icon="Setting" circle plain></el-button>
          </el-tooltip>
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
        :data="portList"
        v-loading="loading"
        border
        style="width: 100%"
        :size="tableSize"
        @selection-change="handleSelectionChange"
        highlight-current-row
        row-key="id"
      >
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80" sortable></el-table-column>
        <el-table-column prop="number" label="端口号" min-width="150" sortable>
          <template v-slot="scope">
            <el-tag size="medium">{{ scope.row.number }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="220">
          <template v-slot="scope">
            <div v-if="scope.row.description" class="description-container">
              <div class="description-text">{{ truncateText(scope.row.description, 100) }}</div>
              <el-button type="primary" link size="small" @click="handleViewFullDescription(scope.row)">
                查看详情
              </el-button>
            </div>
            <span v-else class="text-muted">暂无描述</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" sortable>
          <template v-slot="scope">
            <div class="time-info">
              <el-icon><Clock /></el-icon>
              <span>{{ formatDateTime(scope.row.createdAt) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template v-slot="scope">
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
              <el-tooltip content="查看详情" placement="top">
                <el-button
                  size="small"
                  @click="handleViewFullDescription(scope.row)"
                  type="info"
                  icon="View"
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
              <el-button type="primary" @click="handleAdd">添加端口</el-button>
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
          :page-size="queryParams.pageSize"
          :current-page="queryParams.pageNum"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </el-card>

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
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="100px"
          class="drawer-form"
          :status-icon="true"
        >
          <el-alert
            v-if="form.id"
            title="您正在编辑现有端口，请谨慎操作。"
            type="warning"
            :closable="false"
            show-icon
            style="margin-bottom: 20px;"
          ></el-alert>

          <el-form-item label="端口号" prop="number">
            <el-input
              v-model="form.number"
              placeholder="请输入端口号"
              maxlength="10"
              show-word-limit
              prefix-icon="Monitor"
              clearable
            ></el-input>
            <div class="form-tip">支持数字或字符串，如 8080 或 ws</div>
          </el-form-item>

          <el-form-item label="描述" prop="description">
            <el-tabs type="border-card">
              <el-tab-pane label="编辑">
                <md-editor-element
                  v-model="form.description"
                  :height="500"
                  :theme="editorTheme"
                  placeholder="请输入端口用途说明，支持Markdown格式"
                  @upload-image="handleUploadImage"
                ></md-editor-element>
              </el-tab-pane>
              <el-tab-pane label="预览">
                <div class="markdown-preview-wrapper">
                  <markdown-view v-if="form.description" :content="form.description" :show-catalog="false" :theme="editorTheme" />
                  <el-empty v-else description="暂无内容" :image-size="80"></el-empty>
                </div>
              </el-tab-pane>
            </el-tabs>
            <div class="form-tip">支持Markdown格式，可编写详细的使用说明文档</div>
          </el-form-item>
        </el-form>

        <div class="drawer-footer">
          <el-button @click="drawerVisible = false" icon="Close">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading" icon="Check">确定</el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 查看完整描述抽屉 -->
    <el-drawer
      v-model="descriptionDialogVisible"
      :title="`端口详情: ${currentPort?.number || ''}`"
      direction="rtl"
      size="60%"
      destroy-on-close
      class="description-drawer"
    >
      <template #header>
        <div class="description-header">
          <div class="port-info">
            <el-tag size="large" effect="dark">{{ currentPort?.number }}</el-tag>
            <span class="port-create-time" v-if="currentPort?.createdAt">
              创建于: {{ formatDateTime(currentPort.createdAt) }}
            </span>
          </div>
        </div>
      </template>
      <template #default>
        <div class="full-description-container" v-if="currentPort">
          <el-empty v-if="!currentPort.description" description="暂无描述" />
          <markdown-view
            v-else
            :content="currentPort.description"
            :theme="editorTheme"
            :show-catalog="true"
            :catalog-level="3"
          />
        </div>
      </template>
      <template #footer>
        <div class="description-footer">
          <el-button @click="descriptionDialogVisible = false" icon="Close">关闭</el-button>
          <el-button type="primary" @click="handleEdit(currentPort)" v-if="currentPort" icon="Edit">编辑</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance } from 'element-plus'
import { getPortList, createPort, updatePort, deletePort, type PortUrl, type QueryParams } from '@/api/repeater/porturl'
import { formatDateTime as formatTime } from '@/utils/format'
import MdEditorElement from '@/components/markdown/MdEditorElement.vue'
import MarkdownView from '@/components/markdown/MarkdownView.vue'
import type { PageInfo } from '@/utils/types/common'

// 查询参数
const queryParams = reactive<QueryParams>({
  pageNum: 1,
  pageSize: 10,
  number: ''
})

// 数据列表
const portList = ref<PortUrl[]>([])
const total = ref(0)
const loading = ref(false)
const tableSize = ref('default')
const selectedPorts = ref<PortUrl[]>([])

// 表单相关
const drawerVisible = ref(false)
const dialogTitle = ref('')
const submitLoading = ref(false)
const formRef = ref<FormInstance>()
const form = reactive<PortUrl>({
  id: null,
  number: '',
  description: ''
})

// 表单规则
const rules = {
  number: [
    { required: true, message: '请输入端口号', trigger: 'blur' },
    { max: 10, message: '端口号长度不能超过10个字符', trigger: 'blur' }
  ],
  description: []
}

// 描述弹窗相关
const descriptionDialogVisible = ref(false)
const currentPort = ref<PortUrl | null>(null)

// 编辑器主题设置
const editorTheme = ref<'light' | 'dark'>('light')

// 获取数据列表
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getPortList(queryParams)
    portList.value = res.items
    total.value = res.total
  } catch (error) {
    console.error('获取端口列表失败', error)
    ElMessage.error('获取端口列表失败')
  } finally {
    loading.value = false
  }
}

// 查询
const handleQuery = () => {
  queryParams.pageNum = 1
  fetchData()
}

// 重置查询
const resetQuery = () => {
  queryParams.number = ''
  handleQuery()
}

// 分页处理
const handleSizeChange = (size: number) => {
  queryParams.pageSize = size
  queryParams.pageNum = 1
  fetchData()
}

const handleCurrentChange = (page: number) => {
  queryParams.pageNum = page
  fetchData()
}

// 处理表格选择变化
const handleSelectionChange = (selection: PortUrl[]) => {
  selectedPorts.value = selection
}

// 批量删除
const handleBatchDelete = () => {
  if (selectedPorts.value.length === 0) return

  ElMessageBox.confirm(`确认删除选中的 ${selectedPorts.value.length} 个端口吗？`, '批量删除', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 循环单个删除
      const promises = selectedPorts.value
        .filter(port => port.id)
        .map(port => deletePort(port.id as number))

      await Promise.all(promises)
      ElMessage.success(`成功删除 ${selectedPorts.value.length} 个端口`)
      fetchData()
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }).catch(() => {})
}

// 新增
const handleAdd = () => {
  resetForm()
  dialogTitle.value = '新增端口'
  drawerVisible.value = true
}

// 编辑
const handleEdit = (row: PortUrl) => {
  resetForm()
  Object.assign(form, row)
  dialogTitle.value = '编辑端口'
  drawerVisible.value = true

  // 如果是从描述抽屉打开的，关闭描述抽屉
  if (descriptionDialogVisible.value) {
    descriptionDialogVisible.value = false
  }
}

// 删除
const handleDelete = (row: PortUrl) => {
  ElMessageBox.confirm(`确认删除端口 ${row.number} 吗？`, '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deletePort(row.id as number)
      ElMessage.success('删除成功')
      fetchData()
    } catch (error) {
      console.error('删除失败', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 查看完整描述
const handleViewFullDescription = (row: PortUrl) => {
  currentPort.value = row
  descriptionDialogVisible.value = true
}

// 处理图片上传
const handleUploadImage = (files: FileList, callback: (urls: string[]) => void) => {
  // 这里可以调用实际的上传API
  // 现在用简单的FileReader转base64做示例
  const fileList = Array.from(files)
  const uploadPromises = fileList.map(file => {
    return new Promise<string>((resolve) => {
      const reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onload = () => {
        resolve(reader.result as string)
      }
    })
  })

  Promise.all(uploadPromises).then(urls => {
    callback(urls)
    ElMessage.success('图片插入成功')
  })
}

// 重置表单
const resetForm = () => {
  form.id = null
  form.number = ''
  form.description = ''

  if (formRef.value) {
    formRef.value.resetFields()
  }
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (form.id) {
          await updatePort(form.id, form)
          ElMessage.success('更新成功')
        } else {
          await createPort(form)
          ElMessage.success('添加成功')
        }
        drawerVisible.value = false
        fetchData()
      } catch (error: any) {
        console.error('提交失败', error)
        ElMessage.error(error.message || '提交失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 工具方法 - 格式化时间
const formatDateTime = (dateTime: string | undefined) => {
  if (!dateTime) return '-'
  return formatTime(dateTime)
}

// 工具方法 - 截断文本
const truncateText = (text: string, length: number) => {
  // 去除Markdown标记后再截断
  if (!text) return ''
  const plainText = text.replace(/#{1,6}\s|[*_~`]|\[.*?\]\(.*?\)|!\[.*?\]\(.*?\)|<.*?>|```[\s\S]*?```/g, '')
  if (plainText.length <= length) return plainText
  return plainText.substring(0, length) + '...'
}

// 初始化
onMounted(() => {
  // 检测系统主题偏好
  const prefersDarkMode = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches
  editorTheme.value = prefersDarkMode ? 'dark' : 'light'

  // 监听系统主题变化
  window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
    editorTheme.value = e.matches ? 'dark' : 'light'
  })

  fetchData()
})
</script>

<style scoped lang="scss">
.port-url-container {
  padding: 20px;

  .port-url-card {
    margin-bottom: 20px;
    transition: all 0.3s;
    border-radius: 8px;
    overflow: hidden;
  }

  .search-box {
    margin-bottom: 20px;
    background-color: var(--vue-color-primary-light-9);
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    border: 1px solid var(--vue-color-primary-light-7);
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
    }

    .el-form-item {
      margin-bottom: 0;
      width: 100%;
    }

    .el-button {
      margin-left: 8px;
      font-weight: var(--font-weight-medium);
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

    .action-buttons {
      display: flex;
      gap: 12px;
      justify-content: center;

      .el-button {
        &--primary {
          color: var(--vue-color-primary);

          &:hover {
            color: var(--vue-color-primary-dark-1);
          }
        }

        &--danger {
          color: var(--el-color-danger);

          &:hover {
            color: #ff4d4f;
          }
        }
      }
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

    .markdown-preview-wrapper {
      height: 500px;
      overflow-y: auto;
      padding: 16px;
      border: 1px solid #ebeef5;
      border-radius: 4px;
      background-color: #fff;
    }
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

    .description-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;

      .port-info {
        display: flex;
        align-items: center;
        gap: 15px;

        .port-create-time {
          font-size: 14px;
          color: var(--text-color-secondary);
        }
      }
    }

    .description-footer {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      padding: 10px 20px;
      border-top: 1px solid var(--vue-color-primary-light-7);
      background-color: var(--vue-color-primary-light-9);
    }
  }

  .full-description-container {
    padding: 20px;
    height: calc(100% - 60px);
    overflow-y: auto;
  }

  .description-text {
    flex: 1;
    margin-right: 10px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 70%;
  }

  .description-container {
    display: flex;
    align-items: center;
    width: 100%;
  }

  .text-muted {
    color: var(--text-color-tertiary);
    font-style: italic;
  }
}

// 暗色模式适配
@media (prefers-color-scheme: dark) {
  .port-url-container {
    .search-box {
      background-color: rgba(66, 184, 131, 0.1);
      border-color: rgba(66, 184, 131, 0.2);
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
    }

    .content-card {
      background-color: var(--el-bg-color);
      border: 1px solid rgba(66, 184, 131, 0.2);

      .card-header {
        background-color: rgba(66, 184, 131, 0.1);
        border-bottom-color: rgba(66, 184, 131, 0.2);
      }
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

      .action-buttons {
        .el-button {
          &--primary {
            color: #5ccb9c;

            &:hover {
              color: #7ad8b2;
            }
          }

          &--danger {
            color: #ff9a9a;

            &:hover {
              color: #ffb6b6;
            }
          }
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

    .markdown-preview-wrapper {
      background-color: rgba(30, 30, 30, 0.8) !important;
      border-color: rgba(66, 184, 131, 0.2) !important;
    }
  }
}

@media screen and (max-width: 768px) {
  .port-url-container {
    padding: 10px;

    .search-box {
      padding: 15px;

      .action-buttons {
        justify-content: center;
        margin-top: 10px;
      }
    }

    .table-toolbar {
      flex-direction: column;
      gap: 10px;

      .left-actions, .right-actions {
        width: 100%;
        justify-content: space-between;
      }
    }

    :deep(.el-drawer) {
      width: 90% !important;
    }
  }
}
</style>
