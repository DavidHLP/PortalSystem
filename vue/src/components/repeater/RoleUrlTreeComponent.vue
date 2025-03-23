<template>
  <div class="role-url-tree-component">
    <div v-if="!readonly" class="operation-bar">
      <el-button type="primary" @click="handleAddUrl">
        <el-icon><Plus /></el-icon>添加URL
      </el-button>
    </div>
    <el-card class="tree-card" :body-style="{ padding: '0' }">
      <div class="tree-header">
        <div class="header-item method">请求方法</div>
        <div class="header-item url">URL地址</div>
        <div v-if="!readonly" class="header-item operation">操作</div>
      </div>
      <div class="tree-content">
        <el-tree
          :data="treeData"
          :props="defaultProps"
          node-key="id"
          :default-expand-all="true"
          :expand-on-click-node="false"
          class="modern-tree"
        >
          <template #default="{ node, data }">
            <div class="custom-tree-node" :class="{ 'is-project': data.type === 'project' }">
              <div class="node-content">
                <div v-if="data.type === 'url'" class="method-tag">
                  <el-tag size="small" :type="getMethodType(data.method)" effect="dark">
                    {{ data.method }}
                  </el-tag>
                </div>
                <div class="node-label" :class="{ 'project-label': data.type === 'project' }">
                  <el-icon v-if="data.type === 'project'"><Folder /></el-icon>
                  <el-icon v-else><Link /></el-icon>
                  {{ node.label }}
                </div>
              </div>
              <div v-if="!readonly && data.type === 'url'" class="operation">
                <el-button type="danger" link @click="handleDeleteUrl(data)">
                  <el-icon><Delete /></el-icon>
                  删除
                </el-button>
              </div>
            </div>
          </template>
        </el-tree>
      </div>
    </el-card>

    <el-dialog
      v-model="addUrlDialogVisible"
      title="添加URL"
      width="800px"
      destroy-on-close
      class="add-url-dialog"
    >
      <div class="dialog-content">
        <el-alert
          title="选择URL提示"
          type="info"
          description="请在下方树形结构中选择要添加的URL，可多选。选中项目时会自动选中其下所有URL。"
          :closable="false"
          class="dialog-alert"
        />
        <el-card class="tree-card">
          <el-tree
            :data="availableUrlsTree"
            :props="defaultProps"
            node-key="id"
            show-checkbox
            :default-checked-keys="selectedUrls"
            @check="handleCheck"
            class="modern-tree"
          >
            <template #default="{ node, data }">
              <div class="custom-tree-node" :class="{ 'is-project': data.type === 'project' }">
                <div class="node-content">
                  <div v-if="data.type === 'url'" class="method-tag">
                    <el-tag size="small" :type="getMethodType(data.method)" effect="dark">
                      {{ data.method }}
                    </el-tag>
                  </div>
                  <div class="node-label" :class="{ 'project-label': data.type === 'project' }">
                    <el-icon v-if="data.type === 'project'"><Folder /></el-icon>
                    <el-icon v-else><Link /></el-icon>
                    {{ node.label }}
                  </div>
                </div>
              </div>
            </template>
          </el-tree>
        </el-card>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="addUrlDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmitUrls">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getRoleUrlListByRoleId, batchAddUrls, deleteRoleUrlRelation } from '@/api/repeater/roleurl'
import { listAll } from '@/api/repeater/pathurl'
import type { RoleUrl } from '@/types/repeater/roleurl'
import type { Url } from '@/api/repeater/pathurl'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Folder, Link } from '@element-plus/icons-vue'

interface TreeNode {
  id: string | number
  label: string
  type: 'project' | 'url'
  method?: string
  children?: TreeNode[]
  originalData?: Url
  projectId?: number
}

const props = defineProps<{
  roleId: number
  readonly?: boolean
}>()

const emit = defineEmits<{
  (e: 'update'): void
}>()

const treeData = ref<TreeNode[]>([])
const availableUrlsTree = ref<TreeNode[]>([])
const selectedUrls = ref<number[]>([])
const addUrlDialogVisible = ref(false)

const defaultProps = {
  children: 'children',
  label: 'label'
}

const getMethodType = (method: string) => {
  const typeMap: Record<string, string> = {
    GET: 'success',
    POST: 'warning',
    PUT: 'info',
    DELETE: 'danger'
  }
  return typeMap[method] || 'info'
}

const transformData = (urls: RoleUrl[]): TreeNode[] => {
  const projectMap = new Map<number, TreeNode>()

  urls.forEach(url => {
    if (!url.project) return

    let projectNode = projectMap.get(url.project.id!)
    if (!projectNode) {
      projectNode = {
        id: `project_${url.project.id!}`,
        projectId: url.project.id!,
        label: `${url.project.projectName} (${url.project.projectInterfaceName})`,
        type: 'project',
        children: []
      }
      projectMap.set(url.project.id!, projectNode)
    }

    const urlNode: TreeNode = {
      id: Number(url.id!),
      label: `${url.protocol}://${url.host?.address}:${url.port?.number}${url.router?.path}`,
      type: 'url',
      method: url.method,
      originalData: url as unknown as Url,
      projectId: url.project.id!
    }

    projectNode.children?.push(urlNode)
  })

  return Array.from(projectMap.values())
}

const loadData = async () => {
  try {
    const urls = await getRoleUrlListByRoleId(props.roleId)
    treeData.value = transformData(urls)
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载URL数据失败')
  }
}

const loadAvailableUrls = async () => {
  try {
    const allUrls = await listAll()
    const currentUrls = await getRoleUrlListByRoleId(props.roleId)
    const currentUrlIds = new Set(currentUrls.map(url => url.id))

    const availableUrls = allUrls.filter(url => !currentUrlIds.has(url.id))
    availableUrlsTree.value = transformData(availableUrls as unknown as RoleUrl[])
  } catch (error) {
    console.error('加载可用URL失败:', error)
    ElMessage.error('加载可用URL失败')
  }
}

const handleAddUrl = async () => {
  await loadAvailableUrls()
  addUrlDialogVisible.value = true
}

const handleCheck = (data: any, { checkedKeys }: { checkedKeys: (string | number)[] }) => {
  selectedUrls.value = checkedKeys
    .filter(id => {
      if (typeof id === 'number') return true
      if (typeof id === 'string' && !id.startsWith('project_')) {
        const numId = Number(id)
        return !isNaN(numId)
      }
      return false
    })
    .map(id => Number(id))
}

const handleSubmitUrls = async () => {
  try {
    const urlIds = selectedUrls.value.filter(id =>
      availableUrlsTree.value.some(project =>
        project.children?.some(url => Number(url.id) === id)
      )
    )

    await batchAddUrls(props.roleId, urlIds)
    ElMessage.success('添加成功')
    addUrlDialogVisible.value = false
    selectedUrls.value = []
    await loadData()
    await loadAvailableUrls()
    emit('update')
  } catch (error) {
    console.error('添加URL失败:', error)
    ElMessage.error('添加URL失败')
  }
}

const handleDeleteUrl = async (data: TreeNode) => {
  try {
    await ElMessageBox.confirm('确认删除该URL吗？', '提示', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })

    await deleteRoleUrlRelation(props.roleId, Number(data.id))
    ElMessage.success('删除成功')
    loadData()
    emit('update')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除URL失败:', error)
      ElMessage.error('删除URL失败')
    }
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.role-url-tree-component {
  .operation-bar {
    margin-bottom: 16px;
  }

  .tree-card {
    border: 1px solid var(--el-border-color-light);
    border-radius: 8px;
    overflow: hidden;

    .tree-header {
      display: flex;
      align-items: center;
      height: 48px;
      padding: 0 20px;
      background-color: var(--el-bg-color-page);
      border-bottom: 1px solid var(--el-border-color-light);
      font-weight: 500;
      color: var(--el-text-color-regular);

      .header-item {
        &.method {
          width: 100px;
        }

        &.url {
          flex: 1;
        }

        &.operation {
          width: 80px;
          text-align: center;
        }
      }
    }

    .tree-content {
      padding: 16px;
    }
  }

  .modern-tree {
    :deep(.el-tree-node__content) {
      height: 40px;
    }

    .custom-tree-node {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-right: 8px;

      &.is-project {
        font-weight: 500;
      }

      .node-content {
        display: flex;
        align-items: center;
        gap: 8px;

        .method-tag {
          width: 80px;
          text-align: center;
        }

        .node-label {
          display: flex;
          align-items: center;
          gap: 4px;
          color: var(--el-text-color-regular);

          &.project-label {
            color: var(--el-text-color-primary);
          }

          .el-icon {
            font-size: 16px;
          }
        }
      }

      .operation {
        opacity: 0;
        transition: opacity 0.2s;
      }

      &:hover .operation {
        opacity: 1;
      }
    }
  }
}

.add-url-dialog {
  .dialog-content {
    .dialog-alert {
      margin-bottom: 16px;
    }
  }

  :deep(.el-dialog__body) {
    padding: 20px;
  }
}
</style>
