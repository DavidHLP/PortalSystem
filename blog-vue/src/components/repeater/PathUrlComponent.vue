<template>
  <div class="path-url-component">
    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="hover">
      <el-form :model="queryParams" class="search-form">
        <el-row :gutter="20">
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="协议">
              <el-select v-model="queryParams.protocol" placeholder="选择协议" clearable>
                <el-option
                  v-for="protocol in protocols"
                  :key="protocol.value"
                  :label="protocol.label"
                  :value="protocol.value"getUrlList
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="方法">
              <el-select v-model="queryParams.method" placeholder="选择方法" clearable>
                <el-option
                  v-for="method in methods"
                  :key="method.value"
                  :label="method.label"
                  :value="method.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="主机">
              <el-select
                v-model="queryParams.hostId"
                placeholder="选择主机"
                clearable
                filterable
                :loading="hostLoading"
              >
                <el-option
                  v-for="item in hostList"
                  :key="item.id"
                  :label="item.address"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="端口">
              <el-select
                v-model="queryParams.portId"
                placeholder="选择端口"
                clearable
                filterable
                :loading="portLoading"
              >
                <el-option
                  v-for="item in portList"
                  :key="item.id"
                  :label="item.number"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="路由">
              <el-select
                v-model="queryParams.routerId"
                placeholder="选择路由"
                clearable
                filterable
                :loading="routerLoading"
              >
                <el-option
                  v-for="item in routerList"
                  :key="item.id"
                  :label="item.path"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="项目">
              <el-select
                v-model="queryParams.projectId"
                placeholder="选择项目"
                clearable
                filterable
                :loading="projectLoading"
              >
                <el-option
                  v-for="item in projectList"
                  :key="item.id"
                  :label="item.projectName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="8" :lg="6">
            <el-form-item label="状态">
              <el-select v-model="queryParams.isActive" placeholder="选择状态" clearable>
                <el-option
                  v-for="status in statusOptions"
                  :key="status.value"
                  :label="status.label"
                  :value="status.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="24" :lg="24" class="search-actions">
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 内容区域 -->
    <el-card class="content-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span class="card-title">URL管理</span>
          <el-button type="primary" @click="handleAdd">新增URL</el-button>
        </div>
      </template>

      <el-collapse v-model="activeNames" class="project-list">
        <el-collapse-item
          v-for="project in groupedUrls"
          :key="project.projectId"
          :name="project.projectId"
          class="project-item"
        >
          <template #title>
            <div class="project-header">
              <div class="project-info">
                <div class="project-main">
                  <div class="project-stats">
                    <span class="project-name">{{ project.projectName }}</span>
                    <span class="project-interface">{{ project.projectInterfaceName }}</span>
                    <el-tag size="small" effect="plain" type="info" class="stat-tag">
                      <el-icon><Link /></el-icon>
                      <span class="tag-text">{{ project.urls.length }} URL</span>
                    </el-tag>
                    <el-tag size="small" effect="plain" type="success" class="stat-tag">
                      <el-icon><Check /></el-icon>
                      <span class="tag-text">{{ project.getCount }} GET</span>
                    </el-tag>
                    <el-tag size="small" effect="plain" type="warning" class="stat-tag">
                      <el-icon><Plus /></el-icon>
                      <span class="tag-text">{{ project.postCount }} POST</span>
                    </el-tag>
                    <el-tag size="small" effect="plain" type="primary" class="stat-tag">
                      <el-icon><Edit /></el-icon>
                      <span class="tag-text">{{ project.putCount }} PUT</span>
                    </el-tag>
                    <el-tag size="small" effect="plain" type="danger" class="stat-tag">
                      <el-icon><Delete /></el-icon>
                      <span class="tag-text">{{ project.deleteCount }} DELETE</span>
                    </el-tag>
                    <el-tag size="small" effect="plain" type="info" class="stat-tag">
                      <el-icon><Monitor /></el-icon>
                      <span class="tag-text">{{ project.httpCount }} HTTP</span>
                    </el-tag>
                    <el-tag size="small" effect="plain" type="success" class="stat-tag">
                      <el-icon><Lock /></el-icon>
                      <span class="tag-text">{{ project.httpsCount }} HTTPS</span>
                    </el-tag>
                  </div>
                </div>
              </div>
            </div>
          </template>

          <div class="urls-container">
            <div
              v-for="url in project.urls"
              :key="url.id"
              class="url-item"
            >
              <div class="url-header">
                <div class="url-info">
                  <el-tag size="small" class="method-tag" :type="getMethodType(url.method)">{{ url.method }}</el-tag>
                  <div class="url-details">
                    <div class="url-main">
                      <span class="url-title">{{ formatUrl(url) }}</span>
                      <el-tag :type="url.isActive ? 'success' : 'danger'" effect="light" size="small" class="status-tag">
                        {{ url.isActive ? '启用' : '禁用' }}
                      </el-tag>
                    </div>
                    <div class="url-meta">
                      <span class="url-time">
                        <el-icon><Calendar /></el-icon>
                        {{ formatDate(url.createdAt) }}
                      </span>
                    </div>
                  </div>
                </div>
                <div class="action-buttons">
                  <el-button type="primary" link @click="handleEdit(url)">
                    <el-icon><Edit /></el-icon>
                    编辑
                  </el-button>
                  <el-button type="danger" link @click="handleDelete(url)">
                    <el-icon><Delete /></el-icon>
                    删除
                  </el-button>
                </div>
              </div>

              <div class="url-content">
                <el-descriptions :column="3" border size="small">
                  <el-descriptions-item label="ID" :span="1">{{ url.id }}</el-descriptions-item>
                  <el-descriptions-item label="协议" :span="1">{{ url.protocol }}</el-descriptions-item>
                  <el-descriptions-item label="方法" :span="1">{{ url.method }}</el-descriptions-item>
                  <el-descriptions-item label="主机" :span="1">{{ url.host?.address }}</el-descriptions-item>
                  <el-descriptions-item label="端口" :span="1">{{ url.port?.number }}</el-descriptions-item>
                  <el-descriptions-item label="路由" :span="1">{{ url.router?.path }}</el-descriptions-item>
                  <el-descriptions-item label="创建时间" :span="3">{{ formatDate(url.createdAt) }}</el-descriptions-item>
                  <el-descriptions-item label="描述" :span="3">
                    <div class="description-preview">
                      <span class="truncate-text">{{ url.description || '' }}</span>
                      <el-button type="primary" link @click="handleViewDetail(url)">查看详情</el-button>
                    </div>
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </div>
          </div>
        </el-collapse-item>
      </el-collapse>
      <!-- 添加分页组件 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.limit"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑抽屉 -->
    <el-drawer
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '新增URL' : '编辑URL'"
      size="60%"
      destroy-on-close
    >
      <el-scrollbar height="calc(100vh - 120px)">
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="100px"
          class="url-form"
        >
          <el-form-item label="协议" prop="protocol">
            <el-select v-model="form.protocol" placeholder="请选择协议">
              <el-option
                v-for="protocol in protocols"
                :key="protocol.value"
                :label="protocol.label"
                :value="protocol.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="主机" prop="hostId">
            <el-select
              v-model="form.hostId"
              placeholder="点击选择主机，输入关键词搜索"
              filterable
              remote
              :remote-method="filterHosts"
              :loading="hostLoading"
            >
              <el-option
                v-for="item in filteredHostList"
                :key="item.id"
                :label="item.address"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="端口" prop="portId">
            <el-select
              v-model="form.portId"
              placeholder="点击选择端口，输入关键词搜索"
              filterable
              remote
              :remote-method="filterPorts"
              :loading="portLoading"
            >
              <el-option
                v-for="item in filteredPortList"
                :key="item.id"
                :label="item.number"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="路由" prop="routerId">
            <el-select
              v-model="form.routerId"
              placeholder="点击选择路由，输入关键词搜索"
              filterable
              remote
              :remote-method="filterRouters"
              :loading="routerLoading"
            >
              <el-option
                v-for="item in filteredRouterList"
                :key="item.id"
                :label="item.path"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="项目" prop="projectId">
            <el-select
              v-model="form.projectId"
              placeholder="点击选择项目，输入关键词搜索"
              filterable
              remote
              :remote-method="filterProjects"
              :loading="projectLoading"
            >
              <el-option
                v-for="item in filteredProjectList"
                :key="item.id"
                :label="item.projectName"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="方法" prop="method">
            <el-select v-model="form.method" placeholder="请选择方法">
              <el-option
                v-for="method in methods"
                :key="method.value"
                :label="method.label"
                :value="method.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-switch v-model="form.isActive" />
          </el-form-item>
          <el-form-item label="描述">
            <md-editor-element
              v-model="form.description"
              :height="300"
              :min-height="300"
              :max-height="500"
              :auto-resize="true"
              placeholder="请输入描述内容..."
            />
          </el-form-item>
        </el-form>
      </el-scrollbar>
      <template #footer>
        <div class="drawer-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-drawer>

    <!-- 详情抽屉 -->
    <el-drawer
      v-model="detailVisible"
      title="详细信息"
      size="60%"
      destroy-on-close
    >
      <el-scrollbar height="calc(100vh - 120px)">
        <div class="detail-content">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="ID">{{ currentDetail?.id }}</el-descriptions-item>
            <el-descriptions-item label="协议">{{ currentDetail?.protocol }}</el-descriptions-item>
            <el-descriptions-item label="方法">{{ currentDetail?.method }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="currentDetail?.isActive ? 'success' : 'danger'" effect="light">
                {{ currentDetail?.isActive ? '启用' : '禁用' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="主机">{{ currentDetail?.host?.address }}</el-descriptions-item>
            <el-descriptions-item label="端口">{{ currentDetail?.port?.number }}</el-descriptions-item>
            <el-descriptions-item label="路由">{{ currentDetail?.router?.path }}</el-descriptions-item>
            <el-descriptions-item label="项目">{{ currentDetail?.project?.projectName }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ formatDate(currentDetail?.createdAt) }}</el-descriptions-item>
          </el-descriptions>
          <div class="detail-description">
            <h3>详细描述</h3>
            <markdown-view
              :content="currentDetail?.description || ''"z
              :max-height="500"
            />
          </div>
        </div>
      </el-scrollbar>
      <template #footer>
        <div class="drawer-footer">
          <el-button @click="detailVisible = false">关闭</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import type { Url, GroupedProject as BaseGroupedProject } from '@/api/repeater/pathurl'
import { getUrlList, createUrl, updateUrl, deleteUrl, listAllHost, listAllPort, listAllRouter, listAllProject } from '@/api/repeater/pathurl'
import type { HostUrl } from '@/api/repeater/host_url'
import type { PortUrl } from '@/api/repeater/porturl'
import type { ProjectUrl } from '@/api/repeater/projectUrl'
import type { RouterUrl } from '@/api/repeater/routerurl'
import MdEditorElement from '@/components/markdown/MdEditorElement.vue'
import MarkdownView from '@/components/markdown/MarkdownView.vue'
import { formatDate } from '@/utils/date.ts'
import { Calendar, Folder, Edit, Delete, Link, Check, Plus, Monitor, Lock } from '@element-plus/icons-vue'

// 类型定义
interface QueryParams {
  protocol: string
  method: string
  isActive: boolean | undefined
  hostId?: number
  portId?: number
  routerId?: number
  projectId?: number
  page: number
  limit: number
}

interface FormData extends Omit<Url, 'id' | 'createdAt'> {
  id?: number
}

// 常量定义
const protocols = [
  { label: 'HTTP', value: 'HTTP' },
  { label: 'HTTPS', value: 'HTTPS' }
]

const methods = [
  { label: 'GET', value: 'GET' },
  { label: 'POST', value: 'POST' },
  { label: 'PUT', value: 'PUT' },
  { label: 'DELETE', value: 'DELETE' }
]

const statusOptions = [
  { label: '启用', value: true },
  { label: '禁用', value: false }
]

// 响应式状态
const urlList = ref<Url[]>([])
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'edit'>('add')
const detailVisible = ref(false)
const currentDetail = ref<Url | null>(null)
const activeNames = ref<string[]>([])
const formRef = ref<FormInstance>()

// 查询参数
const queryParams = ref<QueryParams>({
  protocol: '',
  method: '',
  isActive: undefined,
  hostId: undefined,
  portId: undefined,
  routerId: undefined,
  projectId: undefined,
  page: 1,
  limit: 10
})

// 表单数据
const form = ref<FormData>({
  protocol: '',
  hostId: undefined,
  portId: undefined,
  routerId: undefined,
  projectId: undefined,
  method: '',
  isActive: true,
  description: ''
})

// 表单验证规则
const rules = {
  protocol: [{ required: true, message: '请选择协议', trigger: 'change' }],
  method: [{ required: true, message: '请选择方法', trigger: 'change' }],
  hostId: [{ required: true, message: '请选择主机', trigger: 'change' }],
  portId: [{ required: true, message: '请选择端口', trigger: 'change' }],
  routerId: [{ required: true, message: '请选择路由', trigger: 'change' }],
  projectId: [{ required: true, message: '请选择项目', trigger: 'change' }]
}

// 列表数据
const hostList = ref<HostUrl[]>([])
const portList = ref<PortUrl[]>([])
const routerList = ref<RouterUrl[]>([])
const projectList = ref<ProjectUrl[]>([])

const filteredHostList = ref<HostUrl[]>([])
const filteredPortList = ref<PortUrl[]>([])
const filteredRouterList = ref<RouterUrl[]>([])
const filteredProjectList = ref<ProjectUrl[]>([])

// 加载状态
const loading = ref(false)
const hostLoading = ref(false)
const portLoading = ref(false)
const routerLoading = ref(false)
const projectLoading = ref(false)

// 扩展的 GroupedProject 接口
interface GroupedProject extends BaseGroupedProject {
  activeUrls: string[]
}

// 项目分组数据
const groupedUrls = ref<GroupedProject[]>([])

// 方法定义
const formatUrl = (item: Url): string => {
  return `${item.protocol}://${item.host?.address}:${item.port?.number}${item.router?.path}`
}

const handleSearch = () => {
  fetchData(queryParams.value)
}

const handleReset = () => {
  queryParams.value = {
    protocol: '',
    method: '',
    isActive: undefined,
    hostId: undefined,
    portId: undefined,
    routerId: undefined,
    projectId: undefined,
    page: 1,
    limit: 10
  }
  fetchData(queryParams.value)
}

const total = ref(0)

const handleSizeChange = (val: number) => {
  queryParams.value.limit = val
  fetchData(queryParams.value)
}

const handleCurrentChange = (val: number) => {
  queryParams.value.page = val
  fetchData(queryParams.value)
}

const fetchData = async (queryParams: QueryParams) => {
  loading.value = true
  try {
    const data = await getUrlList(queryParams)
    groupedUrls.value = data.items.map(project => ({
      ...project,
      activeUrls: [] // 为每个项目添加 activeUrls 数组用于控制折叠面板
    }))
    total.value = data.total
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  dialogType.value = 'add'
  form.value = {
    protocol: '',
    hostId: undefined,
    portId: undefined,
    routerId: undefined,
    projectId: undefined,
    method: '',
    isActive: true,
    description: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row: Url) => {
  dialogType.value = 'edit'
  form.value = {
    id: row.id,
    protocol: row.protocol,
    hostId: row.hostId,
    portId: row.portId,
    routerId: row.routerId,
    projectId: row.projectId,
    method: row.method,
    isActive: row.isActive,
    description: row.description
  }
  dialogVisible.value = true
}

const handleDelete = async (row: Url) => {
  try {
    await ElMessageBox.confirm('确认删除该记录吗？', '提示', {
      type: 'warning'
    })
    await deleteUrl(row.id!)
    ElMessage.success('删除成功')
    fetchData(queryParams.value)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleViewDetail = (row: Url) => {
  currentDetail.value = row
  detailVisible.value = true
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()

    if (dialogType.value === 'add') {
      const { id, ...createData } = form.value
      await createUrl(createData)
      ElMessage.success('新增成功')
    } else {
      if (form.value.id) {
        const { id, ...updateData } = form.value
        await updateUrl(id, updateData)
        ElMessage.success('更新成功')
      }
    }
    dialogVisible.value = false
    fetchAllList()
  } catch (error) {
    ElMessage.error(dialogType.value === 'add' ? '新增失败' : '更新失败')
  }
}

// 过滤方法
const filterHosts = (query: string) => {
  if (query) {
    hostLoading.value = true
    filteredHostList.value = hostList.value.filter(item =>
      item.address.toLowerCase().includes(query.toLowerCase())
    )
    hostLoading.value = false
  } else {
    filteredHostList.value = hostList.value
  }
}

const filterPorts = (query: string) => {
  if (query) {
    portLoading.value = true
    filteredPortList.value = portList.value.filter(item =>
      item.number.toLowerCase().includes(query.toLowerCase())
    )
    portLoading.value = false
  } else {
    filteredPortList.value = portList.value
  }
}

const filterRouters = (query: string) => {
  if (query) {
    routerLoading.value = true
    filteredRouterList.value = routerList.value.filter(item =>
      item.path.toLowerCase().includes(query.toLowerCase())
    )
    routerLoading.value = false
  } else {
    filteredRouterList.value = routerList.value
  }
}

const filterProjects = (query: string) => {
  if (query) {
    projectLoading.value = true
    filteredProjectList.value = projectList.value.filter(item =>
      item.projectName.toLowerCase().includes(query.toLowerCase())
    )
    projectLoading.value = false
  } else {
    filteredProjectList.value = projectList.value
  }
}

const fetchAllList = async () => {
  try {
    hostList.value = await listAllHost()
    portList.value = await listAllPort()
    routerList.value = await listAllRouter()
    projectList.value = await listAllProject()

    filteredHostList.value = hostList.value
    filteredPortList.value = portList.value
    filteredRouterList.value = routerList.value
    filteredProjectList.value = projectList.value
    await fetchData(queryParams.value)
  } catch (error) {
    ElMessage.error('获取数据失败')
  }
}

// 获取方法对应的标签类型
const getMethodType = (method: string): string => {
  const methodMap: Record<string, string> = {
    'GET': 'success',
    'POST': 'warning',
    'PUT': 'primary',
    'DELETE': 'danger'
  }
  return methodMap[method] || 'info'
}

onMounted(async () => {
  await fetchAllList()
})
</script>

<style scoped lang="scss">
.path-url-component {
  padding: 24px;
  background-color: var(--el-bg-color-page);
  min-height: 100vh;

  .search-card {
    margin-bottom: 24px;
    border-radius: 12px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    border: 1px solid var(--el-border-color-light);
    background: linear-gradient(145deg, var(--el-bg-color), var(--el-bg-color-overlay));

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
    }
  }

  .search-form {
    .search-actions {
      display: flex;
      justify-content: flex-end;
      gap: 16px;
      margin-top: 24px;
      padding-top: 16px;
      border-top: 1px solid var(--el-border-color-lighter);

      .el-button {
        min-width: 100px;
        border-radius: 8px;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-1px);
        }
      }
    }
  }

  .content-card {
    border-radius: 12px;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    border: 1px solid var(--el-border-color-light);
    background: linear-gradient(145deg, var(--el-bg-color), var(--el-bg-color-overlay));

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
    }

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px 24px;
      border-bottom: 1px solid var(--el-border-color-lighter);

      .card-title {
        font-size: 18px;
        font-weight: 600;
        color: var(--el-text-color-primary);
        display: flex;
        align-items: center;
        gap: 8px;

        &::before {
          content: '';
          width: 4px;
          height: 18px;
          background: var(--el-color-primary);
          border-radius: 2px;
        }
      }

      .el-button {
        border-radius: 8px;
        padding: 8px 20px;
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-1px);
          box-shadow: 0 4px 12px rgba(var(--el-color-primary-rgb), 0.2);
        }
      }
    }
  }

  .project-list {
    .project-item {
      margin-bottom: 20px;
      background-color: var(--el-bg-color);
      border-radius: 12px;
      overflow: hidden;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

      &:hover {
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
      }

      :deep(.el-collapse-item__header) {
        background-color: var(--el-bg-color-overlay);
        border-bottom: 1px solid var(--el-border-color-light);
        padding: 20px 24px;
        font-weight: 500;
        color: var(--el-text-color-primary);
        border-radius: 12px 12px 0 0;
        transition: all 0.3s ease;

        &:hover {
          background-color: var(--el-fill-color-light);
        }

        .project-header {
          width: 100%;

          .project-info {
            width: 100%;

            .project-main {
              display: flex;
              flex-direction: column;
              gap: 12px;

              .project-title {
                display: flex;
                align-items: center;
                gap: 12px;

                .project-name {
                  font-size: 16px;
                  font-weight: 600;
                  color: var(--el-text-color-primary);
                }

                .project-interface {
                  font-size: 14px;
                  color: var(--el-text-color-secondary);
                  padding: 2px 8px;
                  background-color: var(--el-fill-color-light);
                  border-radius: 4px;
                }
              }

              .project-stats {
                display: flex;
                flex-wrap: wrap;
                gap: 8px;
                align-items: center;

                .stat-tag {
                  display: inline-flex;
                  align-items: center;
                  justify-content: center;
                  gap: 4px;
                  padding: 0 8px;
                  border-radius: 4px;
                  font-size: 12px;
                  transition: all 0.3s ease;
                  height: 24px;
                  line-height: 24px;
                  white-space: nowrap;

                  .el-icon {
                    font-size: 14px;
                    line-height: 1;
                    vertical-align: middle;
                    margin-right: 2px;
                    margin-top: -2px;
                  }

                  .tag-text {
                    display: inline-flex;
                    align-items: center;
                    height: 100%;
                    line-height: inherit;
                  }

                  &:hover {
                    transform: translateY(-1px);
                  }
                }
              }
            }
          }
        }
      }

      :deep(.el-collapse-item__content) {
        padding: 0;
        background-color: var(--el-bg-color);
      }

      :deep(.el-collapse-item__wrap) {
        border: none;
      }
    }

    .urls-container {
      padding: 24px;
      background-color: var(--el-bg-color-page);

      .url-item {
        margin-bottom: 20px;
        background-color: var(--el-bg-color);
        border-radius: 12px;
        overflow: hidden;
        box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

        &:last-child {
          margin-bottom: 0;
        }

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
        }

        .url-header {
          padding: 20px;
          background-color: var(--el-bg-color);
          border-bottom: 1px solid var(--el-border-color-light);
          display: flex;
          align-items: flex-start;
          justify-content: space-between;

          .url-info {
            display: flex;
            align-items: flex-start;
            gap: 20px;
            flex: 1;

            .method-tag {
              font-weight: 500;
              min-width: 70px;
              text-align: center;
              border-radius: 6px;
              padding: 4px 12px;
            }

            .url-details {
              flex: 1;
              display: flex;
              flex-direction: column;
              gap: 12px;

              .url-main {
                display: flex;
                align-items: center;
                gap: 16px;

                .url-title {
                  flex: 1;
                  font-size: 15px;
                  color: var(--el-text-color-primary);
                  font-weight: 500;
                  word-break: break-all;
                }

                .status-tag {
                  flex-shrink: 0;
                  border-radius: 6px;
                }
              }

              .url-meta {
                display: flex;
                align-items: center;
                gap: 20px;
                font-size: 13px;
                color: var(--el-text-color-secondary);

                .url-time {
                  display: flex;
                  align-items: center;
                  gap: 6px;

                  .el-icon {
                    font-size: 16px;
                  }
                }
              }
            }
          }

          .action-buttons {
            display: flex;
            gap: 12px;
            margin-left: 20px;

            .el-button {
              display: flex;
              align-items: center;
              gap: 6px;
              padding: 6px 12px;
              border-radius: 6px;
              transition: all 0.3s ease;

              &:hover {
                transform: translateY(-1px);
              }

              .el-icon {
                font-size: 16px;
              }
            }
          }
        }

        .url-content {
          padding: 20px;
          background-color: var(--el-bg-color);

          :deep(.el-descriptions) {
            margin-bottom: 0;

            .el-descriptions__header {
              display: none;
            }

            .el-descriptions__body {
              padding: 0;
            }

            .el-descriptions__label {
              background-color: var(--el-fill-color-light);
              padding: 12px 16px;
              font-weight: 500;
              color: var(--el-text-color-regular);
              border-radius: 6px;
            }

            .el-descriptions__content {
              padding: 12px 16px;
            }
          }

          .description-preview {
            display: flex;
            align-items: flex-start;
            gap: 12px;

            .truncate-text {
              flex: 1;
              display: -webkit-box;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;
              overflow: hidden;
              text-overflow: ellipsis;
              line-height: 1.6;
              max-height: 3.2em;
              color: var(--el-text-color-secondary);
            }

            .el-button {
              flex-shrink: 0;
              margin-top: 4px;
              border-radius: 6px;
            }
          }
        }
      }
    }
  }

  .url-form {
    padding: 24px;

    :deep(.el-form-item__label) {
      font-weight: 500;
      color: var(--text-color-secondary);
      padding-bottom: 8px;
    }

    .el-select {
      width: 100%;
    }

    :deep(.el-form-item) {
      margin-bottom: 24px;
    }

    :deep(.el-input__wrapper),
    :deep(.el-select__wrapper) {
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
      transition: all 0.3s ease;

      &:hover {
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
      }

      &.is-focus {
        box-shadow: 0 0 0 2px rgba(var(--el-color-primary-rgb), 0.2);
      }
    }
  }

  .drawer-footer {
    padding: 24px;
    text-align: right;
    border-top: 1px solid var(--el-border-color-lighter);
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: var(--el-bg-color);

    .el-button {
      min-width: 100px;
      margin-left: 16px;
      border-radius: 8px;
      transition: all 0.3s ease;

      &:hover {
        transform: translateY(-1px);
      }
    }

    .el-button--primary {
      background: linear-gradient(45deg, var(--el-color-primary), var(--el-color-primary-light-1));
      border: none;
      box-shadow: 0 4px 12px rgba(var(--el-color-primary-rgb), 0.2);

      &:hover {
        transform: translateY(-1px);
        box-shadow: 0 6px 16px rgba(var(--el-color-primary-rgb), 0.3);
      }

      &:active {
        transform: translateY(0);
      }
    }
  }

  .detail-content {
    padding: 24px;

    .el-descriptions {
      margin-bottom: 24px;
      background-color: var(--el-bg-color);
      border-radius: 12px;
      overflow: hidden;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);

      :deep(.el-descriptions__header) {
        margin-bottom: 0;
        padding: 20px 24px;
        background-color: var(--el-color-primary-light-9);
        border-bottom: 1px solid var(--el-color-primary-light-7);
      }

      :deep(.el-descriptions__body) {
        padding: 24px;
      }

      :deep(.el-descriptions__label) {
        font-weight: 500;
        color: var(--text-color-secondary);
        width: 140px;
        padding: 12px 16px;
        border-radius: 6px;
      }

      :deep(.el-descriptions__content) {
        color: var(--text-color-primary);
        padding: 12px 16px;
      }
    }

    .detail-description {
      background-color: var(--el-bg-color);
      padding: 24px;
      border-radius: 12px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);

      h3 {
        margin-bottom: 20px;
        color: var(--text-color-primary);
        font-size: 18px;
        font-weight: 600;
        padding-bottom: 16px;
        border-bottom: 1px solid var(--el-color-primary-light-7);
      }
    }
  }

  .pagination-container {
    margin-top: 24px;
    display: flex;
    justify-content: flex-end;
    padding: 16px 0;
    border-top: 1px solid var(--el-border-color-lighter);

    :deep(.el-pagination) {
      .el-pagination__total,
      .el-pagination__sizes,
      .el-pagination__jump {
        margin-right: 16px;
      }

      .el-pagination__total {
        font-weight: 500;
      }

      .el-pagination__sizes {
        .el-input__wrapper {
          border-radius: 6px;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
        }
      }

      .el-pager {
        li {
          border-radius: 6px;
          transition: all 0.3s ease;

          &:hover {
            transform: translateY(-1px);
          }

          &.is-active {
            background: linear-gradient(45deg, var(--el-color-primary), var(--el-color-primary-light-1));
            color: white;
          }
        }
      }
    }
  }
}

// 暗色模式适配
@media (prefers-color-scheme: dark) {
  .path-url-component {
    background-color: var(--el-bg-color-page);

    .search-card,
    .content-card {
      background: linear-gradient(145deg, var(--el-bg-color), var(--el-bg-color-overlay));
      border-color: rgba(255, 255, 255, 0.1);
    }

    .url-item {
      background-color: var(--el-bg-color);
      border: 1px solid rgba(255, 255, 255, 0.1);

      :deep(.el-collapse-item__header) {
        background-color: rgba(255, 255, 255, 0.05);
        border-bottom-color: rgba(255, 255, 255, 0.1);
      }
    }

    :deep(.el-input__wrapper),
    :deep(.el-select__wrapper) {
      background-color: var(--el-bg-color-overlay);
      border-color: rgba(255, 255, 255, 0.1);
    }

    .detail-content {
      .el-descriptions {
        background-color: var(--el-bg-color-overlay);
        border-color: rgba(255, 255, 255, 0.1);
      }

      .detail-description {
        background-color: var(--el-bg-color-overlay);
        border-color: rgba(255, 255, 255, 0.1);
      }
    }
  }
}

// 响应式设计
@media screen and (max-width: 768px) {
  .path-url-component {
    padding: 16px;

    .search-card,
    .content-card {
      border-radius: 8px;
    }

    .url-header {
      flex-direction: column;
      gap: 16px;

      .action-buttons {
        margin-left: 0;
        width: 100%;
        justify-content: flex-end;
      }
    }

    .url-content {
      padding: 16px;
    }

    .drawer-footer {
      padding: 16px;
    }

    :deep(.el-collapse-item__header) {
      padding: 16px;

      .project-header {
        .project-info {
          .project-main {
            .project-title {
              flex-direction: column;
              align-items: flex-start;
              gap: 4px;
            }

            .project-stats {
              justify-content: flex-start;
            }
          }
        }
      }
    }
  }
}
</style>
