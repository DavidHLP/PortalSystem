<template>
  <div class="role-url-component">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="角色名称">
          <el-input v-model="searchForm.roleName" placeholder="请输入角色名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
          <el-button type="success" @click="handleAdd">新增</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="roleName" label="角色名称" min-width="120" />
        <el-table-column prop="project.projectName" label="所属项目" min-width="120" />
        <el-table-column prop="project.projectInterfaceName" label="项目别名" min-width="120" />
        <el-table-column prop="project.id" label="项目ID" width="100" />
        <!-- <el-table-column prop="description" label="描述" min-width="150">
          <template #default="{ row }">
            <el-popover
              placement="top-start"
              :width="400"
              trigger="hover"
            >
              <template #reference>
                <span class="description-preview">{{ row.description || '暂无描述' }}</span>
              </template>
              <markdown-view :content="row.description || '暂无描述'" />
            </el-popover>
          </template>
        </el-table-column> -->
        <el-table-column prop="createdAt" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
            <el-button type="info" link @click="handleViewDetail(row)">查看详细</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-drawer
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增角色' : dialogType === 'edit' ? '编辑角色' : '角色详情'"
      size="800px"
      destroy-on-close
      direction="rtl"
      class="role-drawer"
    >
      <el-scrollbar>
        <div class="drawer-content">
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="100px"
            :disabled="dialogType === 'view'"
            class="role-form"
          >
            <el-card class="form-card" shadow="never">
              <template #header>
                <div class="card-header">
                  <span>基本信息</span>
                </div>
              </template>
              <el-form-item label="角色名称" prop="roleName">
                <el-input
                  v-model="form.roleName"
                  placeholder="请输入角色名称"
                  clearable
                  :maxlength="50"
                  show-word-limit
                />
              </el-form-item>
              <el-form-item label="所属项目" prop="projectId">
                <el-select
                  v-model="form.projectId"
                  placeholder="请选择项目"
                  style="width: 100%"
                  filterable
                  clearable
                >
                  <el-option
                    v-for="item in projectOptions"
                    :key="item.id"
                    :label="item.projectName"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="描述" prop="description">
                <md-editor-element
                  v-model="form.description"
                  :height="200"
                  :min-height="150"
                  :max-height="300"
                  :auto-resize="true"
                  placeholder="请输入描述信息，支持Markdown格式..."
                  :theme="'dark'"
                  :toolbars="dialogType === 'view' ? [] : undefined"
                />
              </el-form-item>
            </el-card>

            <el-card v-if="form.id" class="form-card url-card" shadow="never">
              <template #header>
                <div class="card-header">
                  <span>URL配置</span>
                </div>
              </template>
              <role-url-tree-component :role-id="form.id" @update="handleUrlUpdate" />
            </el-card>

            <el-card v-if="form.id" class="form-card doc-card" shadow="never">
              <template #header>
                <div class="card-header">
                  <span>文档预览</span>
                </div>
              </template>
              <div class="doc-content">
                <markdown-view
                  :content="form.description || '暂无文档'"
                  :theme="'dark'"
                />
              </div>
            </el-card>
          </el-form>
        </div>
      </el-scrollbar>
      <template #footer>
        <div class="drawer-footer">
          <el-button @click="dialogVisible = false">{{ dialogType === 'view' ? '关闭' : '取消' }}</el-button>
          <el-button v-if="dialogType !== 'view'" type="primary" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import {
  getRoleUrlPage,
  createRoleUrl,
  updateRoleUrl,
  deleteRoleUrl,
  getRoleUrlById
} from '@/api/repeater/roleurl'
import type { RoleUrl } from '@/types/repeater/roleurl'
import type { PageInfo } from '@/utils/types/common'
import { getProjectUrlListAll } from '@/api/repeater/projectUrl'
import type { ProjectUrl } from '@/api/repeater/projectUrl'
import RoleUrlTreeComponent from './RoleUrlTreeComponent.vue'
import MdEditorElement from '@/components/markdown/MdEditorElement.vue'
import MarkdownView from '@/components/markdown/MarkdownView.vue'

const searchForm = reactive({
  roleName: ''
})

const tableData = ref<RoleUrl[]>([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit' | 'view'>('add')
const formRef = ref<FormInstance>()
const form = reactive<Partial<RoleUrl>>({
  roleName: '',
  projectId: 0,
  description: '',
  protocol: '',
  hostId: 0,
  portId: 0,
  routerId: 0,
  method: '',
  isActive: true
})

const rules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  projectId: [{ required: true, message: '请选择所属项目', trigger: 'change' }]
}

const projectOptions = ref<ProjectUrl[]>([])
const loadProjectOptions = async () => {
  const data:ProjectUrl[] = await getProjectUrlListAll()
  projectOptions.value = data
}

const loadData = async () => {
  try {
    const data = await getRoleUrlPage({
      page: currentPage.value,
      limit: pageSize.value,
      roleName: searchForm.roleName
    }) as unknown as PageInfo<RoleUrl>
    tableData.value = data.items!
    total.value = data.total!
    await loadProjectOptions()
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadData()
}

const handleReset = () => {
  searchForm.roleName = ''
  handleSearch()
}

const handleSizeChange = (val: number) => {
  pageSize.value = val
  loadData()
}

const handleCurrentChange = (val: number) => {
  currentPage.value = val
  loadData()
}

const handleAdd = () => {
  dialogType.value = 'add'
  Object.assign(form, {
    roleName: '',
    projectId: 0,
    description: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row: RoleUrl) => {
  dialogType.value = 'edit'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row: RoleUrl) => {
  try {
    await ElMessageBox.confirm('确认删除该角色吗？', '提示', {
      type: 'warning'
    })
    await deleteRoleUrl(row.id!)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    console.error('删除失败:', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const submitData: RoleUrl = {
          ...form,
          roleName: form.roleName || '',
          protocol: form.protocol || '',
          hostId: form.hostId || 0,
          portId: form.portId || 0,
          routerId: form.routerId || 0,
          projectId: form.projectId || 0,
          method: form.method || '',
          isActive: form.isActive ?? true
        }
        if (dialogType.value === 'add') {
          await createRoleUrl(submitData)
          ElMessage.success('创建成功')
        } else {
          await updateRoleUrl(submitData)
          ElMessage.success('更新成功')
          await handleUrlUpdate()
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error('提交失败:', error)
      }
    }
  })
}

const handleViewDetail = (row: RoleUrl) => {
  dialogType.value = 'view'
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleUrlUpdate = async () => {
  try {
    await loadData()
  } catch (error) {
    console.error('更新URL配置失败:', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.role-url-component {
  .search-card {
    margin-bottom: 16px;

    .search-form {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;
    }
  }

  .table-card {
    .pagination {
      margin-top: 16px;
      display: flex;
      justify-content: flex-end;
    }
  }

  :deep(.el-card__body) {
    padding: 16px;
  }

  .description-preview {
    display: inline-block;
    max-width: 200px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .drawer-footer {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 16px;
    background-color: var(--el-bg-color);
    border-top: 1px solid var(--el-border-color-light);
    display: flex;
    justify-content: flex-end;
    gap: 12px;
  }

  :deep(.el-drawer__body) {
    padding: 20px;
    height: calc(100% - 120px);
    overflow-y: auto;
  }

  .doc-card {
    margin-top: 8px;

    .doc-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .doc-content {
      max-height: 400px;
      overflow-y: auto;
    }
  }

  :deep(.el-form-item.is-disabled) {
    .el-input__wrapper,
    .el-textarea__wrapper {
      background-color: var(--el-bg-color-page);
    }
  }
}

.role-drawer {
  :deep(.el-drawer__header) {
    margin-bottom: 0;
    padding: 16px 20px;
    border-bottom: 1px solid var(--el-border-color-light);
    margin-right: 0;
  }

  .drawer-content {
    padding: 20px;
  }

  .role-form {
    .form-card {
      margin-bottom: 20px;
      border: 1px solid var(--el-border-color-light);
      background-color: var(--el-bg-color-page);

      &:last-child {
        margin-bottom: 0;
      }

      .card-header {
        display: flex;
        align-items: center;
        height: 32px;
        font-size: 15px;
        font-weight: 500;
        color: var(--el-text-color-primary);
      }

      :deep(.el-card__header) {
        padding: 12px 20px;
        border-bottom: 1px solid var(--el-border-color-light);
      }

      :deep(.el-card__body) {
        padding: 20px;
      }
    }

    .url-card {
      :deep(.el-tree) {
        background-color: transparent;
      }
    }

    .doc-card {
      .doc-content {
        padding: 16px;
        background-color: var(--el-bg-color);
        border-radius: 4px;
        min-height: 200px;
        max-height: 400px;
        overflow-y: auto;
      }
    }

    :deep(.el-form-item) {
      margin-bottom: 22px;

      &:last-child {
        margin-bottom: 0;
      }

      .el-form-item__label {
        font-weight: 500;
      }
    }
  }

  .drawer-footer {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    padding: 16px 20px;
    background-color: var(--el-bg-color);
    border-top: 1px solid var(--el-border-color-light);
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    backdrop-filter: blur(10px);
  }

  :deep(.el-drawer__body) {
    height: calc(100% - 120px);
    padding: 0;
    overflow: hidden;
  }
}

:deep(.select-dropdown-disabled) {
  .el-select-dropdown__item {
    &.selected {
      color: var(--el-text-color-regular);
      font-weight: normal;
    }
  }
}
</style>
