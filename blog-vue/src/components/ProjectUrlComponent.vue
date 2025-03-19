<template>
  <div class="project-url-management-container">
    <div class="content-card">
      <div class="search-form-container">
        <el-form :model="searchForm" class="search-form">
          <div class="form-inputs-row">
            <el-form-item label="项目名称" class="form-item double-width">
              <el-input v-model="searchForm.projectName" placeholder="请输入项目名称" clearable />
            </el-form-item>
            <el-form-item label="项目别名" class="form-item double-width">
              <el-input v-model="searchForm.projectInterfaceName" placeholder="请输入项目别名" clearable />
            </el-form-item>
            <el-form-item class="form-item equal-width action-item">
              <el-button type="primary" @click="handleSearch">搜索</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </div>
        </el-form>
      </div>

      <div class="table-actions">
        <el-button type="primary" @click="handleAdd">添加项目</el-button>
      </div>

      <el-table :data="currentPageData" class="project-table" border v-loading="loading">
        <el-table-column property="id" label="ID" width="80" />
        <el-table-column property="projectName" label="项目名称" />
        <el-table-column property="projectInterfaceName" label="项目别名" />
        <el-table-column label="项目文档">
          <template #default="scope">
            <div v-if="scope.row.description" class="markdown-preview">
              {{ getMarkdownPreview(scope.row.description) }}
            </div>
            <span v-else>暂无描述</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280">
          <template #default="scope">
            <div class="action-buttons">
              <el-button type="info" size="small" @click="handleViewDoc(scope.row)" plain v-if="scope.row.description">查看文档</el-button>
              <el-button type="primary" size="small" @click="handleEdit(scope.row)" plain >编辑</el-button>
              <el-button type="danger" size="small" @click="handleDelete(scope.row)" plain>删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <el-drawer
        v-model="drawerVisible"
        :title="isEdit ? '编辑项目' : '添加项目'"
        direction="rtl"
        size="60%"
      >
        <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px" class="project-form">
          <el-form-item label="项目名称" prop="projectName">
            <el-input v-model="formData.projectName" placeholder="请输入项目名称" />
          </el-form-item>
          <el-form-item label="项目别名" prop="projectInterfaceName">
            <el-input v-model="formData.projectInterfaceName" placeholder="请输入项目别名" />
          </el-form-item>
          <el-form-item label="项目文档" prop="description" class="editor-container">
            <MdEditorElement
              v-model="safeDescription"
              :height="600"
            />
          </el-form-item>
        </el-form>
        <div class="drawer-footer">
          <el-button @click="drawerVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认</el-button>
        </div>
      </el-drawer>

      <!-- 文档详情抽屉 -->
      <el-drawer
        v-model="docDrawerVisible"
        title="项目文档详情"
        direction="rtl"
        size="60%"
      >
        <div class="doc-detail-container">
          <div class="doc-header">
            <h2>{{ currentDocProject.projectName || '' }}</h2>
            <h3>{{ currentDocProject.projectInterfaceName || '' }}</h3>
          </div>
          <div class="doc-content markdown-body">
            <MarkdownView
              :content="currentDocProject.description"
              :showCatalog="true"
              codeTheme="atom"
            />
          </div>
        </div>
      </el-drawer>
    </div>
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
    projectName: row.projectName,
    projectInterfaceName: row.projectInterfaceName,
    description: row.description || '' // 确保description是字符串类型
  }
  docDrawerVisible.value = true
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
const handleEdit = (row: ProjectUrl) => {
  isEdit.value = true
  resetForm()
  Object.assign(formData, {
    ...row,
    description: row.description || ''
  })
  drawerVisible.value = true
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

  .content-card {
    background: var(--el-bg-color, #fff);
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
    border: 1px solid var(--el-border-color-lighter, #ebeef5);
    transition: all 0.3s;
  }

  .search-form-container {
    margin-bottom: 20px;
    padding: 16px;
    background-color: var(--vue-color-primary-light-9, #f0faf5);
    border-radius: 6px;
    border: 1px solid var(--vue-color-primary-light-7, #c1e7d5);
  }

  .form-inputs-row {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    align-items: flex-start;
  }

  .form-item {
    margin-bottom: 10px;
  }

  .double-width {
    width: calc(33% - 10px);
  }

  .equal-width {
    width: calc(25% - 10px);
  }

  .action-item {
    display: flex;
    justify-content: flex-start;

    .el-button {
      margin-right: 10px;
      transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
      position: relative;
      overflow: hidden;
      font-weight: 500;
      letter-spacing: 0.5px;

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
        background: linear-gradient(135deg, #42b883 0%, #36a071 100%);
        border-color: #36a071;
        box-shadow: 0 2px 6px rgba(54, 160, 113, 0.3);
        color: white;

        &:hover {
          background: linear-gradient(135deg, #4ac68d 0%, #42b883 100%);
          transform: translateY(-3px);
          box-shadow: 0 4px 12px rgba(54, 160, 113, 0.4);

          &::before {
            transform: translateX(100%);
          }
        }
      }

      &--default {
        border: 1px solid #dcdfe6;
        background: linear-gradient(135deg, #ffffff 0%, #f5f7fa 100%);

        &:hover {
          color: #42b883;
          border-color: #9cd8bc;
          background: linear-gradient(135deg, #ffffff 0%, #f0faf5 100%);
          transform: translateY(-3px);
          box-shadow: 0 4px 12px rgba(66, 184, 131, 0.15);

          &::before {
            transform: translateX(100%);
          }
        }
      }

      &:active {
        transform: translateY(0);
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      }
    }
  }

  .table-actions {
    margin-bottom: 15px;
    display: flex;
    justify-content: flex-end;

    .el-button {
      border-radius: 8px;
      padding: 10px 24px;
      font-weight: 600;
      transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
      background: linear-gradient(120deg, #3ecf8e 0%, #42b883 50%, #3aa876 100%);
      background-size: 200% auto;
      border: none;
      color: white;
      letter-spacing: 0.5px;
      position: relative;
      overflow: hidden;
      box-shadow: 0 4px 10px rgba(66, 184, 131, 0.3), 0 1px 3px rgba(0, 0, 0, 0.08);

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

      &:hover {
        transform: translateY(-3px) scale(1.02);
        background-position: right center;
        box-shadow: 0 6px 16px rgba(66, 184, 131, 0.4), 0 2px 5px rgba(0, 0, 0, 0.08);

        &::before {
          transform: translateX(100%);
        }
      }

      &:active {
        transform: translateY(0) scale(0.98);
        box-shadow: 0 2px 8px rgba(66, 184, 131, 0.3);
      }
    }
  }

  .project-table {
    margin-bottom: 20px;
    border-radius: 6px;
    overflow: hidden;

    .markdown-preview {
      max-height: 80px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      color: var(--el-text-color-secondary);
    }

    .action-buttons {
      display: flex;
      gap: 10px;

      .el-button {
        border-radius: 6px;
        transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
        padding: 6px 14px;
        font-weight: 500;
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
          background-color: #e6f7f0;
          color: #36a071;
          border-color: #9cd8bc;
          box-shadow: 0 2px 4px rgba(66, 184, 131, 0.1);

          &:hover {
            background-color: #d0f0e2;
            color: #2d7d59;
            border-color: #42b883;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(66, 184, 131, 0.2);

            &::before {
              transform: translateX(100%);
            }
          }
        }

        &--danger {
          background-color: #fff1f0;
          color: #f56c6c;
          border-color: #fbc4c4;
          box-shadow: 0 2px 4px rgba(245, 108, 108, 0.1);

          &:hover {
            background-color: #ffe4e3;
            color: #e64242;
            border-color: #f56c6c;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(245, 108, 108, 0.2);

            &::before {
              transform: translateX(100%);
            }
          }
        }

        &--info {
          background-color: #f0f7ff;
          color: #409eff;
          border-color: #c6e2ff;
          box-shadow: 0 2px 4px rgba(64, 158, 255, 0.1);

          &:hover {
            background-color: #e0f0ff;
            color: #2e8cf0;
            border-color: #a0cfff;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(64, 158, 255, 0.2);

            &::before {
              transform: translateX(100%);
            }
          }
        }

        &:hover {
          transform: translateY(-2px);
        }

        &:active {
          transform: translateY(0);
          box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
        }
      }
    }
  }

  .pagination-container {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }

  .project-form {
    max-width: 100%;

    .el-form-item__label {
      font-weight: var(--font-weight-medium, 500);
      color: var(--text-color-secondary, #476582);
    }

    .el-input__inner:focus,
    .el-textarea__inner:focus {
      border-color: var(--vue-color-primary, #42b883);
      box-shadow: 0 0 0 3px rgba(66, 184, 131, 0.1);
    }

    .editor-container {
      margin-bottom: 20px;
    }
  }

  .drawer-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    width: 100%;

    .el-button {
      min-width: 90px;
      border-radius: 8px;
      transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
      padding: 10px 20px;
      font-weight: 500;
      letter-spacing: 0.5px;
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
        background: linear-gradient(135deg, #42b883 0%, #36a071 100%);
        border: none;
        color: white;
        box-shadow: 0 4px 10px rgba(66, 184, 131, 0.3), 0 1px 3px rgba(0, 0, 0, 0.08);

        &:hover {
          background: linear-gradient(135deg, #4ac68d 0%, #42b883 100%);
          transform: translateY(-3px);
          box-shadow: 0 6px 15px rgba(66, 184, 131, 0.4), 0 2px 5px rgba(0, 0, 0, 0.08);

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
        border: 1px solid #dcdfe6;
        background: white;

        &:hover {
          border-color: #9cd8bc;
          color: #42b883;
          transform: translateY(-3px);
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

// 深色模式适配
@media (prefers-color-scheme: dark) {
  .project-url-management-container {
    .action-item {
      .el-button {
        &--primary {
          background: linear-gradient(135deg, #3ba676 0%, #2d7d59 100%);
          box-shadow: 0 3px 8px rgba(45, 125, 89, 0.4);

          &:hover {
            background: linear-gradient(135deg, #42b883 0%, #3ba676 100%);
            box-shadow: 0 5px 15px rgba(45, 125, 89, 0.5);
          }
        }

        &--default {
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

    .table-actions .el-button {
      background: linear-gradient(45deg, #2d7d59 0%, #36a071 50%, #42b883 100%);
      box-shadow: 0 4px 10px rgba(45, 125, 89, 0.4);

      &:hover {
        background-position: right center;
        box-shadow: 0 6px 16px rgba(45, 125, 89, 0.5);
      }
    }

    .project-table .action-buttons {
      .el-button--primary {
        background-color: rgba(66, 184, 131, 0.2);
        color: #5ccb9c;
        border-color: rgba(66, 184, 131, 0.4);
        box-shadow: 0 2px 8px rgba(45, 125, 89, 0.2);

        &:hover {
          background-color: rgba(66, 184, 131, 0.3);
          border-color: rgba(66, 184, 131, 0.6);
          box-shadow: 0 4px 12px rgba(45, 125, 89, 0.3);
        }
      }

      .el-button--danger {
        background-color: rgba(245, 108, 108, 0.2);
        border-color: rgba(245, 108, 108, 0.4);
        color: #ff9a9a;
        box-shadow: 0 2px 8px rgba(245, 108, 108, 0.2);

        &:hover {
          background-color: rgba(245, 108, 108, 0.3);
          border-color: rgba(245, 108, 108, 0.6);
          box-shadow: 0 4px 12px rgba(245, 108, 108, 0.3);
        }
      }

      .el-button--info {
        background-color: rgba(64, 158, 255, 0.2);
        border-color: rgba(64, 158, 255, 0.4);
        color: #8cc5ff;
        box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);

        &:hover {
          background-color: rgba(64, 158, 255, 0.3);
          border-color: rgba(64, 158, 255, 0.6);
          box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
        }
      }
    }

    .drawer-footer {
      .el-button--primary {
        background: linear-gradient(135deg, #3ba676 0%, #2d7d59 100%);
        box-shadow: 0 4px 10px rgba(45, 125, 89, 0.4);

        &:hover {
          background: linear-gradient(135deg, #42b883 0%, #3ba676 100%);
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

.markdown-editor {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
}

// 文档详情样式
.doc-detail-container {
  padding: 20px;
  height: 100%;
  overflow-y: auto;

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

  .doc-content {
    padding: 10px;
    background-color: var(--el-bg-color, #fff);
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  }
}

// 深色模式下文档详情样式
@media (prefers-color-scheme: dark) {
  .doc-detail-container {
    .doc-header {
      border-bottom-color: rgba(80, 80, 80, 0.5);
    }

    .doc-content {
      background-color: rgba(40, 40, 40, 0.8);
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.2);
    }
  }
}
</style>
