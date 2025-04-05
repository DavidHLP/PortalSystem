<template>
  <el-drawer v-model="visible" :title="title" size="700px" direction="rtl" append-to-body destroy-on-close>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="120px" class="router-url-form">
      <div class="form-content">
        <div class="form-group">
          <el-form-item label="主机地址" prop="host">
            <el-input v-model="form.host" placeholder="请输入主机地址" />
          </el-form-item>
          <el-form-item label="端口号" prop="port">
            <el-input v-model="form.port" placeholder="请输入端口号" />
          </el-form-item>
        </div>

        <div class="form-group">
          <el-form-item label="路由路径" prop="router">
            <el-input v-model="form.router" placeholder="请输入路由路径" />
          </el-form-item>
          <el-form-item label="协议类型" prop="protocol">
            <el-select v-model="form.protocol" placeholder="请选择协议类型" class="full-width">
              <el-option label="HTTP" value="HTTP" />
              <el-option label="HTTPS" value="HTTPS" />
              <el-option label="TCP" value="TCP" />
              <el-option label="UDP" value="UDP" />
            </el-select>
          </el-form-item>
        </div>

        <el-form-item label="HTTP方法" prop="httpMethod" v-if="isWebProtocol(form.protocol)">
          <el-select v-model="form.httpMethod" placeholder="请选择HTTP方法" class="full-width">
            <el-option :label="'GET'" :value="0" />
            <el-option :label="'POST'" :value="1" />
            <el-option :label="'PUT'" :value="2" />
            <el-option :label="'DELETE'" :value="3" />
          </el-select>
        </el-form-item>

        <div class="form-group">
          <el-form-item label="唯一标识" prop="uniqueId">
            <el-input v-model="form.uniqueId" placeholder="请输入唯一标识" />
          </el-form-item>
          <el-form-item label="路由类型" prop="type">
            <el-radio-group v-model="form.type">
              <el-radio :label="0">内部</el-radio>
              <el-radio :label="1">外部</el-radio>
            </el-radio-group>
          </el-form-item>
        </div>

        <el-form-item label="文档说明" prop="doc">
          <md-editor-element v-model="form.doc" :height="300" :min-height="200" placeholder="请输入文档说明" />
        </el-form-item>

        <el-form-item label="项目" prop="projects">
          <div class="project-selection-container">
            <div class="project-selection">
              <el-select v-model="selectedProjectId" placeholder="请选择项目" class="project-select" filterable remote
                :remote-method="remoteMethod" :loading="loading" reserve-keyword>
                <el-option v-for="project in searchResults" :key="project.id" :label="project.projectName"
                  :value="project.id" />
              </el-select>
              <el-button type="primary" @click="addProject" :disabled="!selectedProjectId">添加</el-button>
            </div>
            <el-card class="project-table-card" v-if="form.projects && form.projects.length > 0" shadow="hover">
              <el-table :data="form.projects" style="width: 100%" border stripe>
                <el-table-column label="项目名称" prop="projectName" />
                <el-table-column label="项目描述" show-overflow-tooltip>
                  <template #default="scope">
                    <div class="markdown-preview-wrapper">
                      {{ scope.row.doc != null && scope.row.doc.length > 20 ? scope.row.doc.slice(0, 20) + '...' :
                        scope.row.doc == null ? '' : scope.row.doc }}
                    </div>
                    <el-button type="primary" link size="small" @click="viewProjectDetail(scope.row)"
                      class="view-detail-btn">
                      查看详细
                    </el-button>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="100" align="center">
                  <template #default="scope">
                    <el-button type="danger" size="small" @click="removeProject(scope.$index)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-form-item>
      </div>
      <div class="drawer-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-form>
  </el-drawer>

  <!-- 项目详情对话框 -->
  <el-dialog v-model="projectDetailVisible" :title="currentProject?.projectName || '项目详情'" width="60%" destroy-on-close
    append-to-body>
    <div class="project-detail-content">
      <MarkdownView :content="currentProject?.doc || ''" :showCatalog="true" theme="light" />
    </div>
  </el-dialog>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, watchEffect } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import type { RouterProjectDTO } from '@/types/repeater/routerurl'
import { HttpMethodType } from '@/types/repeater/routerurl'
import { addRouterUrl, updateRouterUrl } from '@/api/repeater/routerurl'
import MdEditorElement from '@/components/markdown/MdEditorElement.vue'
import MarkdownView from '@/components/markdown/MarkdownView.vue'
import type { Project } from '@/types/repeater/project'
// 表单引用
const formRef = ref<FormInstance>()
// 对话框可见性
const visible = ref(false)
// 选中的项目ID
const selectedProjectId = ref<number | string>('')
// 加载状态
const loading = ref(false)
// 搜索关键词
const searchQuery = ref('')
// 项目详情对话框可见性
const projectDetailVisible = ref(false)
// 当前查看的项目
const currentProject = ref<Project | null>(null)
// 表单数据
const form = reactive<RouterProjectDTO>({
  id: undefined,
  host: '',
  port: "",
  router: '',
  protocol: 'HTTP',
  uniqueId: '',
  type: "",
  doc: '',
  httpMethod: HttpMethodType.GET,
  projects: []
})

// 存储原始数据，用于取消操作时恢复
const originalData = ref<RouterProjectDTO | null>(null)

// 表单校验规则
const rules = {
  host: [
    { required: true, message: '主机地址不能为空', trigger: 'blur' },
    { max: 100, message: '主机地址长度不能超过100个字符', trigger: 'blur' }
  ],
  port: [
    { required: true, message: '端口号不能为空', trigger: 'blur' },
    { pattern: /^([1-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/, message: '端口号必须是1-65535之间的数字', trigger: 'blur' }
  ],
  router: [
    { required: true, message: '路由路径不能为空', trigger: 'blur' },
    { max: 200, message: '路由路径长度不能超过200个字符', trigger: 'blur' }
  ],
  protocol: [
    { required: true, message: '协议类型不能为空', trigger: 'change' }
  ],
  uniqueId: [
    { required: true, message: '唯一标识不能为空', trigger: 'blur' },
    { max: 50, message: '唯一标识长度不能超过50个字符', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '路由类型不能为空', trigger: 'change' }
  ],
  httpMethod: [
    { required: true, message: 'HTTP方法不能为空', trigger: 'change' }
  ]
}

// 定义props
const props = defineProps<{
  // 当前编辑的路由URL对象
  routerUrl?: RouterProjectDTO
  // 项目列表
  projectList: Project[]
}>()

// 过滤掉已经添加的项目
const filteredProjectList = computed(() => {
  const selectedIds = form.projects.map(project => project.id);
  return props.projectList.filter(project => !selectedIds.includes(project.id));
});

// 搜索结果
const searchResults = ref<Project[]>([]);

/**
 * 远程搜索方法
 * @param query 搜索关键词
 */
const remoteMethod = (query: string) => {
  if (query) {
    loading.value = true;
    setTimeout(() => {
      loading.value = false;
      // 获取未选择的项目
      const selectedIds = form.projects.map(project => project.id);
      const availableProjects = props.projectList.filter(project => !selectedIds.includes(project.id));
      // 根据关键词过滤项目
      searchResults.value = availableProjects.filter(project => {
        return project.projectName && project.projectName.toLowerCase().includes(query.toLowerCase());
      });
    }, 200);
  } else {
    // 查询词为空时，展示所有未选择的项目
    const selectedIds = form.projects.map(project => project.id);
    searchResults.value = props.projectList.filter(project => !selectedIds.includes(project.id));
  }
}

// 定义事件
const emit = defineEmits<{
  // 提交成功事件
  (e: 'success'): void
}>()

/**
 * 判断是否为Web协议
 * @param protocol 协议类型
 * @returns 是否为Web协议
 */
const isWebProtocol = (protocol: string): boolean => {
  return protocol === 'HTTP' || protocol === 'HTTPS';
}

// 对话框标题
const title = computed(() => {
  return form.id ? '编辑路由URL' : '添加路由URL'
})

// 监听routerUrl变化，更新表单数据
watchEffect(() => {
  if (props.routerUrl) {
    // 深拷贝对象，避免直接引用
    const routerUrlCopy = { ...props.routerUrl }
    // 确保doc是字符串类型
    routerUrlCopy.doc = routerUrlCopy.doc || ''
    // 确保httpMethod有值
    if (routerUrlCopy.httpMethod === undefined && isWebProtocol(routerUrlCopy.protocol)) {
      routerUrlCopy.httpMethod = HttpMethodType.GET
    }

    // 深拷贝projects数组
    if (routerUrlCopy.projects) {
      routerUrlCopy.projects = JSON.parse(JSON.stringify(routerUrlCopy.projects))
    } else {
      routerUrlCopy.projects = []
    }

    // 保存原始数据用于取消操作
    originalData.value = JSON.parse(JSON.stringify(routerUrlCopy))

    Object.assign(form, routerUrlCopy)
  }
})

// 打开对话框
const open = () => {
  visible.value = true;
  // 初始化searchResults显示所有未选择的项目
  const selectedIds = form.projects.map(project => project.id);
  searchResults.value = props.projectList.filter(project => !selectedIds.includes(project.id));
}

// 取消操作
const cancel = () => {
  // 如果有原始数据，则恢复原始数据
  if (originalData.value) {
    Object.assign(form, JSON.parse(JSON.stringify(originalData.value)))
  } else {
    reset()
  }
  visible.value = false
}

// 重置表单
const reset = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  form.id = undefined
  form.host = ''
  form.port = ''
  form.router = ''
  form.protocol = 'HTTP'
  form.uniqueId = ''
  form.type = ''
  form.doc = ''
  form.httpMethod = HttpMethodType.GET
  form.projects = [] as Project[]
  selectedProjectId.value = ''
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const submitForm = { ...form }

        if (submitForm.id != 0) {
          // 更新
          await updateRouterUrl(submitForm)
          ElMessage.success('修改成功')
        } else {
          // 新增
          await addRouterUrl(submitForm)
          ElMessage.success('添加成功')
        }

        // 发射成功事件
        emit('success')
        // 清除表单数据
        reset()
        // 关闭对话框
        visible.value = false
      } catch (error) {
        console.error('提交失败', error)
        ElMessage.error('操作失败，请稍后重试')
      }
    }
  })
}

/**
 * 添加项目到已选列表
 */
const addProject = () => {
  if (!selectedProjectId.value) {
    return
  }

  // 查找选中的项目
  const selectedProject = props.projectList.find(item => item.id === selectedProjectId.value)

  if (!selectedProject) {
    return
  }

  // 检查是否已经存在该项目
  const existingIndex = form.projects.findIndex(item => item.id === selectedProject.id)
  if (existingIndex !== -1) {
    ElMessage.warning('该项目已添加')
    return
  }

  // 添加到已选项目列表
  form.projects.push({
    id: selectedProject.id,
    projectName: selectedProject.projectName,
    doc: selectedProject.doc || ''
  })
  console.log('添加后的projects:', form.projects)
  // 清空选择
  selectedProjectId.value = ''
}

/**
 * 从已选列表移除项目
 * @param index 项目索引
 */
const removeProject = (index: number) => {
  form.projects.splice(index, 1)
}

/**
 * 查看项目详情
 * @param project 项目信息
 */
const viewProjectDetail = (project: Project) => {
  currentProject.value = project;
  projectDetailVisible.value = true;
}

// 向外部暴露方法
defineExpose({
  open
})
</script>

<style scoped>
.router-url-form {
  padding: 0 20px;
}

.form-content {
  max-height: calc(100vh - 180px);
  overflow-y: auto;
  padding-right: 10px;
}

.form-group {
  display: flex;
  gap: 20px;
}

.form-group .el-form-item {
  flex: 1;
  margin-bottom: 18px;
}

.drawer-footer {
  position: sticky;
  bottom: 0;
  padding: 15px 0;
  margin-top: 10px;
  text-align: right;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
}

.full-width {
  width: 100%;
}

.project-selection-container {
  width: 100%;
}

.project-selection {
  display: flex;
  margin-bottom: 15px;
}

.project-select {
  flex: 1;
  margin-right: 10px;
}

.project-table-card {
  margin-top: 10px;
  border-radius: 4px;
}

:deep(.el-table) {
  --el-table-header-bg-color: #f5f7fa;
}

:deep(.markdown-editor-container) {
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

@media (max-width: 768px) {
  .form-group {
    flex-direction: column;
    gap: 0;
  }

  .label-width {
    width: 90px;
  }
}

.markdown-preview-wrapper {
  max-height: 80px;
  overflow: hidden;
  position: relative;
  mask-image: linear-gradient(to bottom, black 60%, transparent 100%);
  -webkit-mask-image: linear-gradient(to bottom, black 60%, transparent 100%);
}

:deep(.markdown-preview-wrapper .md-editor-preview-wrapper) {
  padding: 8px;
  font-size: 12px;
  background: transparent;
}

.view-detail-btn {
  margin-top: 4px;
  display: block;
}

.project-detail-content {
  max-height: 70vh;
  overflow-y: auto;
  padding: 16px;
  border-radius: 8px;
  background-color: var(--el-bg-color-overlay, #fff);
}
</style>
