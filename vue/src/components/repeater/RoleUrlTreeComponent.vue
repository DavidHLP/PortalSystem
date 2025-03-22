<template>
  <div class="role-url-tree-component">
    <div class="operation-bar">
      <el-button type="primary" @click="handleAddUrl">添加URL</el-button>
    </div>
    <el-card class="tree-card">
      <el-tree
        :data="treeData"
        :props="defaultProps"
        node-key="id"
        default-expand-all
        :expand-on-click-node="false"
      >
        <template #default="{ node, data }">
          <span class="custom-tree-node">
            <span class="node-content">
              <el-tag v-if="data.type === 'url'" size="small" :type="getMethodType(data.method)">
                {{ data.method }}
              </el-tag>
              <span class="node-label">{{ node.label }}</span>
            </span>
            <span v-if="data.type === 'url'" class="operation">
              <el-button type="danger" link @click="handleDeleteUrl(data)">删除</el-button>
            </span>
          </span>
        </template>
      </el-tree>
    </el-card>

    <el-dialog
      v-model="addUrlDialogVisible"
      title="添加URL"
      width="800px"
      destroy-on-close
    >
      <el-card class="tree-card">
        <el-tree
          :data="availableUrlsTree"
          :props="defaultProps"
          node-key="id"
          show-checkbox
          :default-checked-keys="selectedUrls"
          @check="handleCheck"
        >
          <template #default="{ node, data }">
            <span class="custom-tree-node">
              <span class="node-content">
                <el-tag v-if="data.type === 'url'" size="small" :type="getMethodType(data.method)">
                  {{ data.method }}
                </el-tag>
                <span class="node-label">{{ node.label }}</span>
              </span>
            </span>
          </template>
        </el-tree>
      </el-card>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addUrlDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmitUrls">确定</el-button>
        </span>
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
import { ElMessage } from 'element-plus'

interface TreeNode {
  id: number
  label: string
  type: 'project' | 'url'
  method?: string
  children?: TreeNode[]
  originalData?: Url
}

const props = defineProps<{
  roleId: number
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
        id: url.project.id!,
        label: `${url.project.projectName} (${url.project.projectInterfaceName})`,
        type: 'project',
        children: []
      }
      projectMap.set(url.project.id!, projectNode)
    }

    const urlNode: TreeNode = {
      id: url.id!,
      label: `${url.protocol}://${url.host?.address}:${url.port?.number}${url.router?.path}`,
      type: 'url',
      method: url.method,
      originalData: url as unknown as Url
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
  }
}

const handleAddUrl = async () => {
  await loadAvailableUrls()
  addUrlDialogVisible.value = true
}

const handleCheck = (data: any, { checkedKeys, checkedNodes }: { checkedKeys: number[], checkedNodes: any[] }) => {
  selectedUrls.value = checkedNodes
    .filter(node => node.type === 'url')
    .map(node => node.id)
}

const handleSubmitUrls = async () => {
  try {
    await batchAddUrls(props.roleId, selectedUrls.value)
    ElMessage.success('添加成功')
    addUrlDialogVisible.value = false
    selectedUrls.value = []
    await loadData()
    await loadAvailableUrls()
    emit('update')
  } catch (error) {
    console.error('添加URL失败:', error)
  }
}

const handleDeleteUrl = async (data: TreeNode) => {
  try {
    await deleteRoleUrlRelation(props.roleId, data.id)
    ElMessage.success('删除成功')
    loadData()
    emit('update')
  } catch (error) {
    console.error('删除URL失败:', error)
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
    :deep(.el-card__body) {
      padding: 16px;
    }
  }

  .custom-tree-node {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    padding-right: 8px;

    .node-content {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .node-label {
      font-size: 14px;
    }

    .operation {
      display: none;
    }

    &:hover .operation {
      display: inline-block;
    }
  }
}
</style>
