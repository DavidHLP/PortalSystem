<template>
  <div class="app-container">
    <!-- 搜索工具栏 -->
    <el-card class="search-form-container">
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="主机地址" prop="host">
          <el-input v-model="queryParams.host" placeholder="请输入主机地址" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="路由路径" prop="router">
          <el-input v-model="queryParams.router" placeholder="请输入路由路径" clearable @keyup.enter="handleQuery" />
        </el-form-item>
        <el-form-item label="协议类型" prop="protocol">
          <el-select v-model="queryParams.protocol" placeholder="请选择协议类型" clearable>
            <el-option label="HTTP" value="HTTP" />
            <el-option label="HTTPS" value="HTTPS" />
            <el-option label="TCP" value="TCP" />
            <el-option label="UDP" value="UDP" />
          </el-select>
        </el-form-item>
        <el-form-item label="HTTP方法" prop="httpMethod" v-if="isWebProtocolQuery">
          <el-select v-model="queryParams.httpMethod" placeholder="请选择HTTP方法" clearable>
            <el-option label="GET" :value="0" />
            <el-option label="POST" :value="1" />
            <el-option label="PUT" :value="2" />
            <el-option label="DELETE" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="路由类型" prop="type">
          <el-select v-model="queryParams.type" placeholder="请选择路由类型" clearable>
            <el-option label="内部" :value="0" />
            <el-option label="外部" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <el-icon>
              <Search />
            </el-icon> 搜索
          </el-button>
          <el-button @click="resetQuery">
            <el-icon>
              <Refresh />
            </el-icon> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作工具栏 -->
    <el-card class="table-container">
      <template #header>
        <div class="card-header">
          <span class="card-title">路由URL列表</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon>
              <Plus />
            </el-icon> 新增
          </el-button>
        </div>
      </template>

      <!-- 表格数据 -->
      <el-table v-loading="loading" :data="routerUrlList" border stripe>
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="完整URL" min-width="320" :show-overflow-tooltip="true">
          <template #default="scope">
            <div class="url-display">
              <el-tag size="small" :type="getProtocolTagType(scope.row.protocol) || undefined" effect="dark"
                class="protocol-tag">
                {{ scope.row.protocol }}
              </el-tag>
              <span class="url-text">{{ getFormattedUrl(scope.row) }}
                <el-tooltip content="复制URL" placement="top">
                  <el-button type="primary" size="small" circle @click="copyUrl(scope.row)">
                    <el-icon>
                      <DocumentCopy />
                    </el-icon>
                  </el-button>
                </el-tooltip>
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="协议类型" prop="protocol" width="120" align="center" />
        <el-table-column label="HTTP方法" prop="httpMethod" width="120" align="center" v-if="hasHttpMethodColumn">
          <template #default="scope">
            <el-tag v-if="isWebProtocol(scope.row.protocol)" :type="getHttpMethodTagType(scope.row.httpMethod || HttpMethodType.GET) as any">
              {{ scope.row.httpMethod || HttpMethodType.GET }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="唯一标识" prop="uniqueId" width="120" :show-overflow-tooltip="true" />
        <el-table-column label="路由类型" prop="type" width="120" align="center">
          <template #default="scope">
            {{ scope.row.type === 0 ? '内部' : '外部' }}
          </template>
        </el-table-column>
        <el-table-column label="文档说明" prop="doc" min-width="220" :show-overflow-tooltip="false">
          <template #default="scope">
            <div class="doc-preview">
              {{ truncateDoc(scope.row.doc) }}
              <el-button v-if="scope.row.doc" type="primary" link @click="viewFullDoc(scope.row.doc)">
                查看详情
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="180" align="center">
          <template #default="scope">
            <div class="action-buttons">
              <el-button type="primary" size="small" @click="handleEdit(scope.row)">
                编辑
              </el-button>
              <el-button type="danger" size="small" @click="handleDelete(scope.row)">
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-pagination class="pagination" v-model:current-page="pageInfo.pageNum" v-model:page-size="pageInfo.pageSize"
        :page-sizes="[10, 20, 50, 100]" :total="pageInfo.total" layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-card>

    <!-- 路由URL编辑组件 -->
    <RouterUrlComponents ref="routerUrlFormRef" :router-url="editRouterUrl" @success="getRouterUrlList" />

    <!-- Markdown文档查看抽屉 -->
    <el-drawer v-model="docDrawerVisible" title="文档详情" size="70%" direction="rtl" destroy-on-close>
      <MarkdownView :content="currentDoc" :theme="'light'" />
    </el-drawer>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, DocumentCopy, Link } from '@element-plus/icons-vue'
import type { RouterUrl } from '@/types/repeater/routerurl'
import { HttpMethodType } from '@/types/repeater/routerurl'
import type { PageInfo } from '@/types/common'
import { listRouterUrl, deleteRouterUrl } from '@/api/repeater/routerurl'
import RouterUrlComponents from '@/components/repeater/RouterUrlComponents.vue'
import MarkdownView from '@/components/markdown/MarkdownView.vue'

/**
 * 格式化日期时间
 * @param time 时间戳
 * @returns 格式化后的日期时间字符串
 */
const formatDateTime = (time: string | number | Date): string => {
  if (!time) {
    return '';
  }
  const date = new Date(time);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  const seconds = String(date.getSeconds()).padStart(2, '0');

  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

// 加载状态
const loading = ref(false)
// 路由URL列表
const routerUrlList = ref<RouterUrl[]>([])
// 编辑的路由URL对象
const editRouterUrl = ref<RouterUrl>()
// 路由URL表单组件引用
const routerUrlFormRef = ref()

// 分页参数
const pageInfo = reactive<PageInfo<RouterUrl>>({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  item: {} as RouterUrl
})

// 查询参数
const queryParams = reactive<Partial<RouterUrl>>({
  host: '',
  router: '',
  protocol: '',
  type: undefined,
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
 * 获取HTTP方法名称
 * @param method HTTP方法类型
 * @returns HTTP方法名称
 */
const getHttpMethodName = (method: HttpMethodType | undefined): string => {
  if (method === undefined) {
    return '';
  }
  switch (method) {
    case HttpMethodType.GET: return 'GET';
    case HttpMethodType.POST: return 'POST';
    case HttpMethodType.PUT: return 'PUT';
    case HttpMethodType.DELETE: return 'DELETE';
    default: return '';
  }
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
      type: queryParams.type || '',
      port: queryParams.port || '',
      uniqueId: '',
      doc: '',
      httpMethod: queryParams.httpMethod || undefined
    }

    const res = await listRouterUrl(pageInfo)
    if (res.items) {
      // 确保HTTP方法有默认值
      routerUrlList.value = (res.items || []).map(item => {
        if (isWebProtocol(item.protocol) && item.httpMethod === undefined) {
          return { ...item, httpMethod: HttpMethodType.GET };
        }
        return item;
      }) as RouterUrl[];
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
const handleAdd = () => {
  editRouterUrl.value = {
    host: '',
    port: '',
    router: '',
    protocol: 'HTTP',
    uniqueId: '',
    type: '',
    doc: '',
    httpMethod: HttpMethodType.GET
  }
  routerUrlFormRef.value.open()
}

/**
 * 处理编辑
 */
const handleEdit = (row: RouterUrl) => {
  editRouterUrl.value = { ...row }
  routerUrlFormRef.value.open()
}

/**
 * 处理删除
 */
const handleDelete = (row: RouterUrl) => {
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
const getFormattedUrl = (row: RouterUrl): string => {
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
const getFullUrl = (row: RouterUrl): string => {
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
const copyUrl = (row: RouterUrl) => {
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
const openUrl = (row: RouterUrl) => {
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

.search-form-container {
  margin-bottom: 20px;
}

.table-container {
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;

  :deep(.el-table) {
    --el-table-border-color: var(--el-border-color-lighter);
    --el-table-header-bg-color: var(--el-fill-color-light);

    th {
      font-weight: bold;
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: var(--font-size-large);
  font-weight: var(--font-weight-semibold);
  color: var(--text-color-primary);
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.doc-preview {
  display: flex;
  align-items: center;
  gap: 8px;
}

.url-display {
  display: flex;
  align-items: center;
  gap: 10px;
}

.protocol-tag {
  min-width: 60px;
  text-align: center;
}

.url-text {
  font-family: monospace;
  color: var(--el-color-primary-light-3);
  flex: 1;
  word-break: break-all;
}

.url-actions {
  display: flex;
  gap: 8px;
  margin-left: auto;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 8px;
}
</style>
