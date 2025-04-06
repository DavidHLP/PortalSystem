<template>
  <div class="app-container">
    <!-- 搜索工具栏 -->
    <el-card class="search-form-container">
      <el-form :model="queryParams" ref="queryForm" :inline="true">
        <el-form-item label="角色名称" prop="roleName">
          <el-input
            v-model="queryParams.roleName"
            placeholder="请输入角色名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item label="项目名称" prop="project.projectName">
          <el-input
            v-model="queryParams.project!.projectName"
            placeholder="请输入项目名称"
            clearable
            @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">
            <el-icon><Search /></el-icon> 搜索
          </el-button>
          <el-button @click="resetQuery">
            <el-icon><Refresh /></el-icon> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作工具栏 -->
    <el-card class="table-container">
      <template #header>
        <div class="card-header">
          <span class="card-title">角色URL列表</span>
          <el-button
            type="primary"
            @click="handleAdd"
          >
            <el-icon><Plus /></el-icon> 新增
          </el-button>
        </div>
      </template>

      <!-- 表格数据 -->
      <el-table
        v-loading="loading"
        :data="roleUrlList"
        border
        stripe
      >
        <el-table-column
          label="序号"
          type="index"
          width="60"
          align="center"
        />
        <el-table-column
          label="角色名称"
          prop="roleName"
          min-width="160"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="所属项目"
          prop="project.projectName"
          min-width="160"
          :show-overflow-tooltip="true"
        />
        <el-table-column
          label="项目文档"
          prop="project.doc"
          min-width="220"
          :show-overflow-tooltip="false"
        >
          <template #default="scope">
            <div class="doc-preview">
              {{ truncateDoc(scope.row.project.doc) }}
              <el-button v-if="scope.row.project.doc" type="primary" link @click="viewFullDoc(scope.row.project.doc)">
                查看详情
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="角色文档"
          prop="doc"
          min-width="220"
          :show-overflow-tooltip="false"
        >
          <template #default="scope">
            <div class="doc-preview">
              {{ truncateDoc(scope.row.doc) }}
              <el-button v-if="scope.row.doc" type="primary" link @click="viewFullDoc(scope.row.doc)">
                查看详情
              </el-button>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          label="创建时间"
          prop="gmtCreate"
          min-width="160"
          align="center"
        >
          <template #default="scope">
            {{ formatDateTime(scope.row.gmtCreate) }}
          </template>
        </el-table-column>
        <el-table-column
          label="修改时间"
          prop="gmtModified"
          min-width="160"
          align="center"
        >
          <template #default="scope">
            {{ formatDateTime(scope.row.gmtModified) }}
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          fixed="right"
          width="180"
          align="center"
        >
          <template #default="scope">
            <el-button
              type="primary"
              link
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              link
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <el-pagination
        class="pagination"
        v-model:current-page="pageInfo.pageNum"
        v-model:page-size="pageInfo.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="pageInfo.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 角色URL编辑组件 -->
    <RoleUrlCompoenrs
      ref="roleUrlFormRef"
      :roleUrl="editRoleUrl"
      @success="getRoleUrlList"
    />

    <!-- Markdown文档查看抽屉 -->
    <el-drawer
      v-model="docDrawerVisible"
      title="文档详情"
      size="70%"
      direction="rtl"
      destroy-on-close
    >
      <MarkdownView :content="currentDoc" :theme="'light'" />
    </el-drawer>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import type { RoleUrl, RoleUrlDTO } from '@/types/repeater/roleurl.ts'
import type { PageInfo } from '@/types/common'
import { listRoleUrl, deleteRoleUrl } from '@/api/repeater/roleurl'
import RoleUrlCompoenrs from '@/components/repeater/RoleUrlCompoenrs.vue'
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
// 角色URL列表
const roleUrlList = ref<RoleUrl[]>([])
// 编辑的角色URL对象
const editRoleUrl = ref<RoleUrlDTO>({
  id: '',
  roleName: '',
  project: {
    id: '',
    projectName: '',
    doc: ''
  },
  routers: []
})
// 角色URL表单组件引用
const roleUrlFormRef = ref()

// 分页参数
const pageInfo = reactive<PageInfo<RoleUrl>>({
  pageNum: 1,
  pageSize: 10,
  total: 0,
  item: {
    id: '',
    project: {
      id: '',
      projectName: '',
      doc: ''
    }
  }
})

// 查询参数
const queryParams = reactive<RoleUrl>({
  id: '',
  roleName: '',
  project: {
    id: '',
    projectName: ''
  }
})

// 文档相关
const docDrawerVisible = ref(false)
const currentDoc = ref('')

/**
 * 获取角色URL列表
 */
const getRoleUrlList = async () => {
  loading.value = true
  try {
    // 设置查询条件
    pageInfo.item = {
      id: queryParams.id,
      roleName: queryParams.roleName,
      project: {
        id: queryParams.project?.id || '',
        projectName: queryParams.project?.projectName || ''
      }
    }

    const res = await listRoleUrl(pageInfo)
    roleUrlList.value = res.items || []
    pageInfo.total = res.total || 0
  } catch (error) {
    console.error('获取角色URL列表失败', error)
    ElMessage.error('获取角色URL列表失败')
  } finally {
    loading.value = false
  }
}

/**
 * 处理查询
 */
const handleQuery = () => {
  pageInfo.pageNum = 1
  getRoleUrlList()
}

/**
 * 重置查询
 */
const resetQuery = () => {
  queryParams.roleName = ''
  if (queryParams.project) {
    queryParams.project.projectName = ''
  }
  handleQuery()
}

/**
 * 处理每页条数变化
 */
const handleSizeChange = (size: number) => {
  pageInfo.pageSize = size
  getRoleUrlList()
}

/**
 * 处理页码变化
 */
const handleCurrentChange = (page: number) => {
  pageInfo.pageNum = page
  getRoleUrlList()
}

/**
 * 处理添加
 */
const handleAdd = () => {
  editRoleUrl.value = {
    id: '',
    roleName: '',
    project: {
      id: '',
      projectName: '',
      doc: ''
    },
    routers: []
  }
  roleUrlFormRef.value.open()
}

/**
 * 处理编辑
 */
const handleEdit = (row: RoleUrlDTO) => {
  editRoleUrl.value = { ...row }
  roleUrlFormRef.value.open()
}

/**
 * 处理删除
 */
const handleDelete = (row: RoleUrl) => {
  ElMessageBox.confirm(
    `确认删除角色"${row.roleName}"吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteRoleUrl({ id: row.id })
      ElMessage.success('删除成功')
      getRoleUrlList()
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
  return doc.length > maxLength ? doc.substring(0, maxLength) + '...' : doc;
}

/**
 * 查看完整文档
 * @param doc 文档内容
 */
const viewFullDoc = (doc: string) => {
  currentDoc.value = doc;
  docDrawerVisible.value = true;
}

// 组件挂载时获取数据
onMounted(() => {
  getRoleUrlList()
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
</style>
