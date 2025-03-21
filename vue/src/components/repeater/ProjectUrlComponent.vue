<template>
  <div class="project-url-management-container">
    <!-- 顶部卡片 -->
    <el-card class="project-url-card">
      <!-- 搜索区域 -->
      <div class="search-box">
        <el-form :model="searchForm" class="search-form">
          <el-row :gutter="20" class="search-row">
            <el-col :xs="24" :sm="12" :md="8" :lg="8">
              <el-form-item label="项目名称" class="form-item">
                <el-input
                  v-model="searchForm.projectName"
                  placeholder="请输入项目名称"
                  clearable
                  prefix-icon="Document"
                  @keyup.enter="handleSearch"
                />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8" :lg="8">
              <el-form-item label="项目别名" class="form-item">
                <el-input
                  v-model="searchForm.projectInterfaceName"
                  placeholder="请输入项目别名"
                  clearable
                  prefix-icon="Link"
                  @keyup.enter="handleSearch"
                />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="24" :md="8" :lg="8">
              <el-form-item class="form-item action-item">
                <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
                <el-button icon="RefreshRight" @click="handleReset">重置</el-button>
                <el-button type="primary" icon="Plus" @click="handleAdd">添加项目</el-button>
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
            <el-button type="danger" plain icon="Delete" :disabled="selectedProjects.length === 0" @click="handleBatchDelete">批量删除</el-button>
          </el-button-group>
          <span class="selected-count" v-if="selectedProjects.length > 0">
            已选择 <el-tag type="info">{{ selectedProjects.length }}</el-tag> 项
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
        :data="currentPageData"
        border
        v-loading="loading"
        :size="tableSize"
        @selection-change="handleSelectionChange"
        row-key="id"
        class="project-table"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column property="id" label="ID" width="80" sortable />
        <el-table-column property="projectName" label="项目名称" sortable>
          <template #default="scope">
            <el-tag size="medium" type="success">{{ scope.row.projectName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column property="projectInterfaceName" label="项目别名" sortable />
        <el-table-column label="项目文档" min-width="220">
          <template #default="scope">
            <div v-if="scope.row.description" class="description-container">
              <div class="description-text">{{ getMarkdownPreview(scope.row.description) }}</div>
              <el-button type="primary" link size="small" @click="handleViewDoc(scope.row)">
                查看详情
              </el-button>
            </div>
            <span v-else class="text-muted">暂无描述</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button-group>
              <el-tooltip content="查看文档" placement="top" v-if="scope.row.description">
                <el-button
                  size="small"
                  @click="handleViewDoc(scope.row)"
                  type="info"
                  icon="Document"
                  circle
                ></el-button>
              </el-tooltip>
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
              <el-button type="primary" @click="handleAdd">添加项目</el-button>
            </el-empty>
          </div>
        </template>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          background
        />
      </div>

      <!-- 新增/编辑抽屉 -->
      <el-drawer
        v-model="drawerVisible"
        :title="isEdit ? '编辑项目' : '添加项目'"
        direction="rtl"
        size="60%"
        destroy-on-close
      >
        <div class="drawer-content">
          <el-form
            :model="formData"
            :rules="rules"
            ref="formRef"
            label-width="100px"
            class="drawer-form"
            :status-icon="true"
          >
            <el-alert
              v-if="isEdit"
              title="您正在编辑现有项目，请谨慎操作。"
              type="warning"
              :closable="false"
              show-icon
              style="margin-bottom: 20px;"
            ></el-alert>

            <el-form-item label="项目名称" prop="projectName">
              <el-input
                v-model="formData.projectName"
                placeholder="请输入项目名称"
                prefix-icon="Document"
              />
            </el-form-item>
            <el-form-item label="项目别名" prop="projectInterfaceName">
              <el-input
                v-model="formData.projectInterfaceName"
                placeholder="请输入项目别名"
                prefix-icon="Link"
              />
            </el-form-item>
            <el-form-item label="项目文档" prop="description">
              <el-tabs type="border-card">
                <el-tab-pane label="编辑">
                  <MdEditorElement
                    v-model="safeDescription"
                    :height="500"
                  />
                </el-tab-pane>
                <el-tab-pane label="预览">
                  <div class="markdown-preview-wrapper">
                    <markdown-view v-if="safeDescription" :content="safeDescription" :show-catalog="false" />
                    <el-empty v-else description="暂无内容" :image-size="80"></el-empty>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </el-form-item>
          </el-form>
          <div class="drawer-footer">
            <el-button @click="drawerVisible = false" icon="Close">取消</el-button>
            <el-button type="primary" @click="submitForm" icon="Check">确认</el-button>
          </div>
        </div>
      </el-drawer>

      <!-- 文档详情抽屉 -->
      <el-drawer
        v-model="docDrawerVisible"
        :title="`项目详情: ${currentDocProject.projectName || ''}`"
        direction="rtl"
        size="60%"
        destroy-on-close
        class="description-drawer"
      >
        <template #header>
          <div class="description-header">
            <div class="project-info">
              <el-tag size="large" effect="dark" type="success">{{ currentDocProject.projectName }}</el-tag>
              <span class="project-alias" v-if="currentDocProject.projectInterfaceName">
                别名: {{ currentDocProject.projectInterfaceName }}
              </span>
            </div>
          </div>
        </template>
        <template #default>
          <div class="full-description-container">
            <el-empty v-if="!currentDocProject.description" description="暂无描述" />
            <MarkdownView
              v-else
              :content="currentDocProject.description"
              :showCatalog="true"
              codeTheme="atom"
            />
          </div>
        </template>
        <template #footer>
          <div class="description-footer">
            <el-button @click="docDrawerVisible = false" icon="Close">关闭</el-button>
            <el-button
              type="primary"
              @click="handleEdit(currentDocProject)"
              icon="Edit"
            >编辑</el-button>
          </div>
        </template>
      </el-drawer>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import { getProjectUrlList, createProjectUrl, updateProjectUrl, deleteProjectUrl, type ProjectUrl } from '@/api/repeater/projectUrl'
import 'md-editor-v3/lib/style.css'
import MdEditorElement from '@/components/markdown/MdEditorElement.vue'
import MarkdownView from '@/components/markdown/MarkdownView.vue'
// 数据加载状态
const loading = ref(false)

// 分页相关
const currentPage = ref(1)
const pageSize = ref(5)
const total = ref(0)
const dataList = ref<ProjectUrl[]>([])
const tableSize = ref('default')
const selectedProjects = ref<ProjectUrl[]>([])

// 计算当前页面数据
const currentPageData = computed(() => {
  return dataList.value
})

// 搜索表单
const searchForm = reactive({
  projectName: '',
  projectInterfaceName: ''
})

// 表单数据类型
type FormDataType = {
  id?: number;
  projectName: string;
  projectInterfaceName: string;
  description: string;
}

// 表单相关
const drawerVisible = ref(false)
const isEdit = ref(false)
const formRef = ref<FormInstance>()
const formData = reactive<FormDataType>({
  projectName: '',
  projectInterfaceName: '',
  description: ''
})

// 表单验证规则
const rules = {
  projectName: [
    { required: true, message: '请输入项目名称', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  projectInterfaceName: [
    { required: true, message: '请输入项目别名', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ]
}

// 确保传给Markdown编辑器的值始终是字符串
const safeDescription = computed({
  get: () => formData.description || '',
  set: (val: string) => {
    formData.description = val
  }
})

// 文档详情查看相关
const docDrawerVisible = ref(false)
const currentDocProject = ref<{
  id?: number;
  projectName: string;
  projectInterfaceName: string;
  description: string;
}>({
  projectName: '',
  projectInterfaceName: '',
  description: ''
})

// 查看文档详情
const handleViewDoc = (row: ProjectUrl) => {
  currentDocProject.value = {
    id: row.id,
    projectName: row.projectName,
    projectInterfaceName: row.projectInterfaceName,
    description: row.description || '' // 确保description是字符串类型
  }
  docDrawerVisible.value = true
}

// 处理表格选择变化
const handleSelectionChange = (selection: ProjectUrl[]) => {
  selectedProjects.value = selection
}

// 批量删除
const handleBatchDelete = () => {
  if (selectedProjects.value.length === 0) return

  ElMessageBox.confirm(`确认删除选中的 ${selectedProjects.value.length} 个项目吗？`, '批量删除', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 循环单个删除
      const promises = selectedProjects.value
        .filter(project => project.id)
        .map(project => deleteProjectUrl(project.id as number))

      await Promise.all(promises)
      ElMessage.success(`成功删除 ${selectedProjects.value.length} 个项目`)
      fetchData()
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }).catch(() => {})
}

// 初始化
onMounted(() => {
  fetchData()
})

// 获取数据
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      projectName: searchForm.projectName || undefined,
      projectInterfaceName: searchForm.projectInterfaceName || undefined
    }
    const res = await getProjectUrlList(currentPage.value, pageSize.value, params)
    dataList.value = res.items
    total.value = res.total
  } catch (error) {
    console.error('Failed to fetch project URL data:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchData()
}

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    projectName: '',
    projectInterfaceName: ''
  })
  currentPage.value = 1
  fetchData()
}

// 分页大小变化
const handleSizeChange = (val: number) => {
  pageSize.value = val
  fetchData()
}

// 页码变化
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  fetchData()
}

// 添加项目
const handleAdd = () => {
  isEdit.value = false
  resetForm()
  drawerVisible.value = true
}

// 编辑项目
const handleEdit = (row: any) => {
  isEdit.value = true
  resetForm()
  Object.assign(formData, {
    id: row.id,
    projectName: row.projectName,
    projectInterfaceName: row.projectInterfaceName,
    description: row.description || ''
  })
  drawerVisible.value = true

  // 如果是从描述抽屉打开的，关闭描述抽屉
  if (docDrawerVisible.value) {
    docDrawerVisible.value = false
  }
}

// 删除项目
const handleDelete = (row: ProjectUrl) => {
  ElMessageBox.confirm('确认删除该项目吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      try {
        if (row.id) {
          await deleteProjectUrl(row.id)
          ElMessage.success('删除成功')
          fetchData()
        }
      } catch (error) {
        console.error('Failed to delete project URL:', error)
        ElMessage.error('删除失败')
      }
    })
    .catch(() => {
      // 取消删除操作
    })
}

// 重置表单
const resetForm = () => {
  formData.projectName = ''
  formData.projectInterfaceName = ''
  formData.description = ''
  formData.id = undefined
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const submitData: ProjectUrl = {
          ...formData,
          projectName: formData.projectName,
          projectInterfaceName: formData.projectInterfaceName
        }

        if (isEdit.value && formData.id) {
          await updateProjectUrl(submitData)
          ElMessage.success('更新成功')
        } else {
          await createProjectUrl(submitData)
          ElMessage.success('添加成功')
        }
        drawerVisible.value = false
        fetchData()
      } catch (error) {
        console.error('Failed to submit form:', error)
        ElMessage.error(isEdit.value ? '更新失败' : '添加失败')
      }
    }
  })
}

// 获取Markdown内容的简短预览
const getMarkdownPreview = (markdown: string | null): string => {
  if (!markdown) return '';

  // 移除Markdown标记，只保留文本内容
  const textContent = markdown
    .replace(/#+\s+/g, '') // 移除标题
    .replace(/(\*\*|__)(.*?)\1/g, '$2') // 移除粗体
    .replace(/(\*|_)(.*?)\1/g, '$2') // 移除斜体
    .replace(/~~(.*?)~~/g, '$2') // 移除删除线
    .replace(/`{1,3}[^`]*`{1,3}/g, '') // 移除代码块
    .replace(/\[([^\]]+)\]\([^)]+\)/g, '$1') // 替换链接为文本
    .replace(/!\[([^\]]+)\]\([^)]+\)/g, '图片') // 替换图片为文本
    .replace(/\n/g, ' ') // 替换换行为空格
    .trim();

  // 返回前80个字符加省略号
  return textContent.length > 80 ? textContent.substring(0, 80) + '...' : textContent;
}
</script>

<style scoped lang="scss">
.project-url-management-container {
  padding: 20px;

  .project-url-card {
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

    .form-item {
      margin-bottom: 0;
      width: 100%;
    }

    .action-item {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      margin-bottom: 0;

      @media (max-width: 768px) {
        justify-content: center;
        margin-top: 10px;
      }
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

  .project-table {
    margin-bottom: 20px;
    border-radius: 6px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);

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
      color: var(--el-text-color-secondary);
    }

    .text-muted {
      color: var(--text-color-tertiary);
      font-style: italic;
    }

    .empty-data {
      padding: 40px 0;
    }
  }

  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
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

      .project-info {
        display: flex;
        align-items: center;
        gap: 15px;

        .project-alias {
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
}

// 深色模式适配
@media (prefers-color-scheme: dark) {
  .project-url-management-container {
    .search-box {
      background-color: rgba(66, 184, 131, 0.1);
      border-color: rgba(66, 184, 131, 0.2);
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
    }

    .project-table {
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

    .description-drawer {
      :deep(.el-drawer__header) {
        background-color: rgba(35, 35, 35, 0.8);
        border-bottom-color: rgba(66, 184, 131, 0.2);

        .el-drawer__title {
          color: #e0e0e0;
        }
      }
    }

    .markdown-preview-wrapper {
      background-color: rgba(30, 30, 30, 0.8) !important;
      border-color: rgba(66, 184, 131, 0.2) !important;
    }
  }
}

.markdown-editor {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
}

// 文档详情样式
.full-description-container {
  .doc-header {
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 1px solid var(--el-border-color-light);

    h2 {
      margin: 0 0 10px;
      font-size: 24px;
      color: var(--el-text-color-primary);
    }

    h3 {
      margin: 0;
      font-size: 18px;
      color: var(--el-text-color-secondary);
      font-weight: normal;
    }
  }

  :deep(.markdown-body) {
    padding: 10px;
    background-color: var(--el-bg-color, #fff);
    border-radius: 8px;
  }
}

// 深色模式下文档详情样式
@media (prefers-color-scheme: dark) {
  .full-description-container {
    .doc-header {
      border-bottom-color: rgba(80, 80, 80, 0.5);
    }

    :deep(.markdown-body) {
      background-color: rgba(40, 40, 40, 0.8);
    }
  }
}
</style>