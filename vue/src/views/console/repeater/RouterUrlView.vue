<template>
  <div class="app-container">
    <!-- 搜索工具栏 -->
    <div class="search-toolbar">
      <div class="form-item">
        <label class="form-label">主机地址</label>
        <el-input v-model="queryParams.host" placeholder="请输入主机地址" clearable @keyup.enter="handleQuery" />
      </div>
      <div class="form-item">
        <label class="form-label">路由路径</label>
        <el-input v-model="queryParams.router" placeholder="请输入路由路径" clearable @keyup.enter="handleQuery" />
      </div>
      <div class="form-item">
        <label class="form-label">协议类型</label>
        <el-select v-model="queryParams.protocol" placeholder="请选择协议类型" clearable class="form-select">
          <el-option label="HTTP" value="HTTP" />
          <el-option label="HTTPS" value="HTTPS" />
          <el-option label="TCP" value="TCP" />
          <el-option label="UDP" value="UDP" />
        </el-select>
      </div>
      <div class="form-item" v-if="isWebProtocolQuery">
        <label class="form-label">HTTP方法</label>
        <el-select v-model="queryParams.httpMethod" placeholder="请选择HTTP方法" clearable class="form-select">
          <el-option label="GET" :value="0" />
          <el-option label="POST" :value="1" />
          <el-option label="PUT" :value="2" />
          <el-option label="DELETE" :value="3" />
        </el-select>
      </div>
      <div class="form-item">
        <label class="form-label">路由类型</label>
        <el-select v-model="queryParams.type" placeholder="请选择路由类型" clearable class="form-select">
          <el-option label="内部" :value="0" />
          <el-option label="外部" :value="1" />
        </el-select>
      </div>
      <div class="search-buttons">
        <el-button type="primary" class="search-btn" @click="handleQuery">
          <el-icon><Search /></el-icon> 搜索
        </el-button>
        <el-button class="reset-btn" @click="resetQuery">
          <el-icon><Refresh /></el-icon> 重置
        </el-button>
      </div>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="table-container" shadow="hover">
      <template #header>
        <div class="card-header">
          <span class="card-title">路由URL列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon> 新增
          </el-button>
        </div>
      </template>

      <!-- 表格数据 -->
      <el-table v-loading="loading" :data="routerUrlList" border stripe>
        <el-table-column label="序号" type="index" width="70" align="center" />
        <el-table-column label="完整URL" min-width="300" :show-overflow-tooltip="true">
          <template #default="scope">
            <div class="url-display">
              <el-tag size="small" :type="getProtocolTagType(scope.row.protocol) || undefined" effect="dark"
                class="protocol-tag">
                {{ scope.row.protocol }}
              </el-tag>
              <span class="url-text">{{ getFormattedUrl(scope.row) }}</span>
              <el-tooltip content="复制URL" placement="top">
                <el-button type="primary" link @click="copyUrl(scope.row)" class="copy-btn">
                  <el-icon><DocumentCopy /></el-icon>
                </el-button>
              </el-tooltip>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="协议类型" prop="protocol" width="100" align="center" />
        <el-table-column label="HTTP方法" prop="httpMethod" width="100" align="center" v-if="hasHttpMethodColumn">
          <template #default="scope">
            <el-tag v-if="isWebProtocol(scope.row.protocol)" :type="getHttpMethodTagType(scope.row.httpMethod || HttpMethodType.GET) as any">
              {{ scope.row.httpMethod || HttpMethodType.GET }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="唯一标识" prop="uniqueId" width="120" :show-overflow-tooltip="true" />
        <el-table-column label="路由类型" prop="type" width="90" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.type === 0 ? 'info' : 'warning'" size="small">
              {{ scope.row.type === 0 ? '内部' : '外部' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="关联项目" min-width="160" :show-overflow-tooltip="true">
          <template #default="scope">
            <div class="projects-tags">
              <template v-if="scope.row.projects && scope.row.projects.length > 0">
                <el-tag
                  v-for="(project, index) in scope.row.projects.slice(0, 3)"
                  :key="index"
                  size="small"
                  type="success"
                  class="project-tag"
                  :effect="index % 2 === 0 ? 'light' : 'plain'"
                >
                  {{ project.projectName }}
                </el-tag>
                <el-tooltip v-if="scope.row.projects.length > 3" :content="scope.row.projects.slice(3).map((p: Project) => p.projectName).join(', ')">
                  <el-tag
                    size="small"
                    type="info"
                    class="more-tag"
                  >
                    +{{ scope.row.projects.length - 3 }}
                  </el-tag>
                </el-tooltip>
              </template>
              <span v-else class="no-project">暂无关联项目</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="文档说明" prop="doc" min-width="200" :show-overflow-tooltip="false">
          <template #default="scope">
            <div class="doc-preview">
              <span class="doc-text">{{ truncateDoc(scope.row.doc) }}</span>
              <el-button v-if="scope.row.doc" type="primary" link @click="viewFullDoc(scope.row.doc)" class="view-doc-btn">
                查看详情
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="160" align="center">
          <template #default="scope">
            <div class="action-buttons">
              <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
              <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-pagination class="pagination"
        v-model:current-page="pageInfo.pageNum"
        v-model:page-size="pageInfo.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pageInfo.total"
        layout="total, sizes, prev, pager, next, jumper"
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </el-card>

    <!-- 路由URL编辑组件 -->
    <RouterUrlComponents ref="routerUrlFormRef" :router-url="editRouterUrl" :project-list="projectList" @success="getRouterUrlList" />

    <!-- Markdown文档查看抽屉 -->
    <el-drawer v-model="docDrawerVisible" title="文档详情" size="70%" direction="rtl" destroy-on-close>
      <MarkdownView :content="currentDoc" :theme="'light'" />
    </el-drawer>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, DocumentCopy } from '@element-plus/icons-vue'
import type { RouterProjectDTO } from '@/types/repeater/routerurl'
import type { Project } from '@/types/repeater/project'
import { HttpMethodType } from '@/types/repeater/routerurl'
import type { PageInfo } from '@/types/common'
import { listRouterUrl, deleteRouterUrl } from '@/api/repeater/routerurl'
import RouterUrlComponents from '@/components/repeater/RouterUrlComponents.vue'
import MarkdownView from '@/components/markdown/MarkdownView.vue'
import { listAllProject } from '@/api/repeater/project'

// 加载状态
const loading = ref(false)
// 路由URL列表
const routerUrlList = ref<RouterProjectDTO[]>([])
// 编辑的路由URL对象
const editRouterUrl = ref<RouterProjectDTO>()
// 路由URL表单组件引用
const routerUrlFormRef = ref()

// 分页参数
const pageInfo = reactive<PageInfo<RouterProjectDTO>>({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  item: {} as RouterProjectDTO
})

// 项目列表
const projectList = ref<Project[]>([])

// 查询参数
const queryParams = reactive<Partial<RouterProjectDTO>>({
  host: '',
  router: '',
  protocol: '',
  type: undefined as unknown as string,
  httpMethod: undefined
})

// 文档相关
const docDrawerVisible = ref(false)
const currentDoc = ref('')

// 判断是否显示HTTP方法查询选项
const isWebProtocolQuery = computed(() => {
  return queryParams.protocol === 'HTTP' || queryParams.protocol === 'HTTPS' || !queryParams.protocol;
})

// 判断是否显示HTTP方法列
const hasHttpMethodColumn = computed(() => {
  return routerUrlList.value.some(item => isWebProtocol(item.protocol));
})

/**
 * 判断是否为Web协议
 * @param protocol 协议类型
 * @returns 是否为Web协议
 */
const isWebProtocol = (protocol: string): boolean => {
  return protocol === 'HTTP' || protocol === 'HTTPS';
}

/**
 * 获取HTTP方法标签类型
 * @param method HTTP方法类型
 * @returns Element Plus Tag类型
 */
const getHttpMethodTagType = (method: String): 'success' | 'warning' | 'danger' | 'info' | 'primary' => {
  switch (method) {
    case 'GET': return 'success';
    case 'POST': return 'primary';
    case 'PUT': return 'warning';
    case 'DELETE': return 'danger';
    default: return 'info';
  }
}

/**
 * 获取路由URL列表
 */
const getRouterUrlList = async () => {
  loading.value = true
  try {
    // 设置查询条件
    pageInfo.item = {
      host: queryParams.host || '',
      router: queryParams.router || '',
      protocol: queryParams.protocol || '',
      type: queryParams.type !== undefined ? queryParams.type : '',
      port: queryParams.port || '',
      uniqueId: '',
      doc: '',
      httpMethod: queryParams.httpMethod || undefined,
      projects: []
    }


    const res = await listRouterUrl(pageInfo)
    if (res.items) {
      // 确保HTTP方法有默认值
      routerUrlList.value = (res.items || []).map(item => {
        if (isWebProtocol(item.protocol) && item.httpMethod === undefined) {
          return { ...item, httpMethod: HttpMethodType.GET };
        }
        return item;
      }) as RouterProjectDTO[];
      pageInfo.total = res.total || 0
    }
  } catch (error) {
    console.error('获取路由URL列表失败', error)
    ElMessage.error('获取路由URL列表失败')
  } finally {
    loading.value = false
  }
}

/**
 * 处理查询
 */
const handleQuery = () => {
  pageInfo.pageNum = 1
  getRouterUrlList()
}

/**
 * 重置查询
 */
const resetQuery = () => {
  queryParams.host = ''
  queryParams.router = ''
  queryParams.protocol = ''
  queryParams.type = undefined
  queryParams.httpMethod = undefined
  handleQuery()
}

/**
 * 处理每页条数变化
 */
const handleSizeChange = (size: number) => {
  pageInfo.pageSize = size
  getRouterUrlList()
}

/**
 * 处理页码变化
 */
const handleCurrentChange = (page: number) => {
  pageInfo.pageNum = page
  getRouterUrlList()
}

/**
 * 处理添加
 */
const handleAdd = async () => {
  projectList.value = await listAllProject()
  editRouterUrl.value = {
    id: 0,
    host: '',
    port: '',
    router: '',
    protocol: 'HTTP',
    uniqueId: '',
    type: '',
    doc: '',
    httpMethod: HttpMethodType.GET,
    projects: []
  }
  routerUrlFormRef.value.open()
}

/**
 * 处理编辑
 */
const handleEdit = async (row: RouterProjectDTO) => {
  projectList.value = await listAllProject()
  editRouterUrl.value = { ...row }
  routerUrlFormRef.value.open()
}

/**
 * 处理删除
 */
const handleDelete = (row: RouterProjectDTO) => {
  ElMessageBox.confirm(
    `确认删除路由URL"${row.host}:${row.port}${row.router}"吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteRouterUrl({
        id: row.id,
        host: row.host,
        port: row.port,
        router: row.router,
        protocol: row.protocol,
        uniqueId: row.uniqueId,
        type: row.type,
        doc: row.doc,
        httpMethod: row.httpMethod
      })
      ElMessage.success('删除成功')
      getRouterUrlList()
    } catch (error) {
      console.error('删除失败', error)
      ElMessage.error('删除失败，请稍后重试')
    }
  }).catch(() => {
    // 用户取消删除
  })
}

/**
 * 截取文档内容
 * @param doc 文档内容
 * @returns 截取后的文档内容
 */
const truncateDoc = (doc: string): string => {
  if (!doc) return '';
  const maxLength = 50;
  return String(doc).length > maxLength ? String(doc).substring(0, maxLength) + '...' : String(doc);
}

/**
 * 查看完整文档
 * @param doc 文档内容
 */
const viewFullDoc = (doc: string) => {
  currentDoc.value = doc || '';  // 确保doc为空时返回空字符串
  docDrawerVisible.value = true;
}

/**
 * 获取格式化后的URL（不含协议部分）
 * @param row 路由URL数据
 * @returns 格式化后的URL字符串
 */
const getFormattedUrl = (row: RouterProjectDTO): string => {
  const host = row.host || '';
  const port = row.port ? `:${row.port}` : '';
  const router = row.router || '';

  return `${host}${port}${router}`;
}

/**
 * 获取完整URL
 * @param row 路由URL数据
 * @returns 完整URL字符串
 */
const getFullUrl = (row: RouterProjectDTO): string => {
  const protocol = row.protocol?.toLowerCase() || '';
  return `${protocol}://${getFormattedUrl(row)}`;
}

/**
 * 获取协议标签类型
 * @param protocol 协议类型
 * @returns Element Plus Tag类型
 */
const getProtocolTagType = (protocol: string): '' | 'success' | 'warning' | 'danger' | 'info' => {
  switch (protocol) {
    case 'HTTP': return 'info';
    case 'HTTPS': return 'success';
    case 'TCP': return 'warning';
    case 'UDP': return 'danger';
    default: return '';
  }
}

/**
 * 复制URL到剪贴板
 * @param row 路由URL数据
 */
const copyUrl = (row: RouterProjectDTO) => {
  const url = getFullUrl(row);
  navigator.clipboard.writeText(url)
    .then(() => {
      ElMessage.success('URL已复制到剪贴板');
    })
    .catch(err => {
      console.error('复制失败', err);
      ElMessage.error('复制失败，请手动复制');
    });
}

/**
 * 在新标签页中打开URL
 * @param row 路由URL数据
 */
const openUrl = (row: RouterProjectDTO) => {
  if (isWebProtocol(row.protocol)) {
    const url = getFullUrl(row);
    window.open(url, '_blank');
  }
}

// 组件挂载时获取数据
onMounted(() => {
  getRouterUrlList()
})
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.search-toolbar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 15px;
  padding: 15px 20px;
  margin-bottom: 20px;
  background-color: #1e1e1e;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .form-item {
    display: flex;
    flex-direction: column;
    min-width: 180px;

    .form-label {
      font-size: 14px;
      margin-bottom: 8px;
      color: #eaeaea;
      font-weight: 500;
    }

    :deep(.el-input__wrapper) {
      background-color: #2a2a2a;
      border: 1px solid #3e3e3e;

      &:hover, &:focus, &.is-focus {
        border-color: #409eff;
      }

      .el-input__inner {
        color: #eaeaea;

        &::placeholder {
          color: #8c8c8c;
        }
      }
    }

    :deep(.el-select) {
      width: 100%;

      .el-input__wrapper {
        background-color: #2a2a2a;
        border: 1px solid #3e3e3e;
      }
    }
  }

  .form-select {
    width: 180px;
  }

  .search-buttons {
    display: flex;
    gap: 10px;
    margin-left: auto;
    align-self: flex-end;

    .search-btn {
      background: #67c23a;
      border-color: #67c23a;

      &:hover {
        background: #85ce61;
        border-color: #85ce61;
      }
    }

    .reset-btn {
      color: #eaeaea;
      background-color: transparent;
      border-color: #606266;

      &:hover {
        background-color: rgba(255, 255, 255, 0.1);
      }
    }
  }
}

.search-form-container {
  margin-bottom: 20px;
  border-radius: 8px;

  .search-form {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }

  :deep(.el-form-item) {
    margin-bottom: 12px;
    margin-right: 20px;
  }

  .search-btns {
    margin-left: auto;
  }
}

.table-container {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);

  :deep(.el-table) {
    --el-table-border-color: var(--el-border-color-lighter);
    border-radius: 4px;
    overflow: hidden;

    .el-table__row {
      transition: background-color 0.3s;

      &:hover {
        background-color: var(--el-fill-color-lighter);
      }
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
}

.card-title {
  font-size: var(--font-size-large);
  font-weight: var(--font-weight-semibold);
  color: var(--text-color-primary);
}

.pagination {
  margin-top: 24px;
  text-align: center;
  padding: 10px 0;
}

.doc-preview {
  display: flex;
  align-items: center;
  gap: 8px;

  .doc-text {
    color: var(--el-text-color-regular);
    font-size: 14px;
  }

  .view-doc-btn {
    padding: 2px 6px;
  }
}

.projects-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
  padding: 2px 0;
}

.project-tag {
  margin: 2px;
}

.more-tag {
  margin: 2px;
  cursor: pointer;
}

.no-project {
  color: var(--el-text-color-secondary);
  font-size: 13px;
  font-style: italic;
}

.url-display {
  display: flex;
  align-items: center;
  gap: 8px;
}

.protocol-tag {
  min-width: 50px;
  text-align: center;
}

.url-text {
  font-family: monospace;
  color: var(--el-color-primary-light-3);
  flex: 1;
  word-break: break-all;
  font-size: 13px;
  padding: 0 4px;
}

.copy-btn {
  flex-shrink: 0;
  margin-left: 4px;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 12px;
}
</style>
