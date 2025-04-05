<template>
  <el-drawer
    :title="roleUrl.id ? '编辑角色URL' : '新增角色URL'"
    v-model="drawerVisible"
    size="600px"
    destroy-on-close
    @close="handleClose"
    direction="rtl"
  >
    <div class="drawer-container">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        status-icon
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="所属项目" prop="projectId">
          <el-select
            v-model="form.projectId"
            placeholder="请选择所属项目"
            clearable
            style="width: 100%"
            @change="handleProjectChange"
          >
            <el-option
              v-for="item in projectList"
              :key="item.id"
              :label="item.projectName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="路由选择" prop="routerIds">
          <el-checkbox-group v-model="routerIds">
            <el-scrollbar height="300px">
              <el-checkbox
                v-for="router in routerList"
                :key="router.id"
                :label="router.id">
                {{router.router}} ({{`${router.protocol}://${router.host}:${router.port}${router.router}`}})
              </el-checkbox>
            </el-scrollbar>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="文档说明" prop="doc">
          <md-editor-element
            v-model="form.doc"
            :height="300"
            :min-height="200"
            placeholder="请输入项目文档说明"
          />
      </el-form-item>
      </el-form>
      <div class="drawer-footer">
        <el-button @click="drawerVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </div>
  </el-drawer>
</template>

<script lang="ts" setup>
import { ref, reactive, defineProps, defineEmits, watch } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import type { RoleUrlDTO } from '@/types/repeater/roleurl'
import type { Project } from '@/types/repeater/project'
import type { RouterUrl } from '@/types/repeater/routerurl'
import { addRoleUrl, updateRoleUrl } from '@/api/repeater/roleurl'
import { listAllProject } from '@/api/repeater/project'
import MdEditorElement from '@/components/markdown/MdEditorElement.vue'
import { listRouterUrlByProjectId } from '@/api/repeater/routerurl'
const props = defineProps<{
  roleUrl: RoleUrlDTO
}>()

const emit = defineEmits<{
  (e: 'success'): void
}>()

// 表单引用
const formRef = ref<FormInstance>()
// 抽屉是否可见
const drawerVisible = ref(false)
// 用于存储选中的路由ID
const routerIds = ref<number[]>([])
// 表单数据
const form = reactive<Omit<RoleUrlDTO, 'routers'> & { routers: RouterUrl[] }>({
  id: 0,
  roleName: '',
  projectId: undefined,
  doc: '',
  routers: [],
})
// 表单校验规则
const rules = reactive<FormRules>({
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在2到50个字符之间', trigger: 'blur' }
  ],
  projectId: [
    { required: true, message: '请选择所属项目', trigger: 'change' }
  ]
})
// 项目列表
const projectList = ref<Project[]>([])
// 路由列表
const routerList = ref<RouterUrl[]>([])

/**
 * 打开抽屉
 */
const open = () => {
  drawerVisible.value = true
  // 获取项目列表
  getProjectList()
}

/**
 * 获取项目列表
 */
const getProjectList = async () => {
  try {
    const res = await listAllProject()
    projectList.value = res || []
  } catch (error) {
    console.error('获取项目列表失败', error)
    ElMessage.error('获取项目列表失败')
  }
}

/**
 * 根据项目ID获取路由列表
 */
const getRouterList = async (projectId: number) => {
  if (!projectId) {
    routerList.value = []
    return
  }
  try {
    const res = await listRouterUrlByProjectId(projectId)
    routerList.value = res || []
  } catch (error) {
    console.error('获取路由列表失败', error)
    ElMessage.error('获取路由列表失败')
  }
}

/**
 * 处理项目变更
 */
const handleProjectChange = (val: number) => {
  if (val) {
    getRouterList(val)
    routerIds.value = [] // 切换项目时清空已选路由
  } else {
    routerList.value = []
    routerIds.value = []
  }
}

/**
 * 处理抽屉关闭
 */
const handleClose = () => {
  resetForm()
}

/**
 * 重置表单
 */
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    roleName: '',
    projectId: undefined,
    doc: '',
    routers: [],
  })
  routerIds.value = []
  routerList.value = []
}

/**
 * 提交表单
 */
const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 转换选中的路由ID为路由对象数组
        form.routers = routerList.value.filter(router =>
          router.id !== undefined && routerIds.value.includes(router.id)
        )

        if (props.roleUrl.id) {
          // 编辑
          form.id = props.roleUrl.id
          await updateRoleUrl(form)
          ElMessage.success('更新成功')
        } else {
          // 新增
          await addRoleUrl(form)
          ElMessage.success('添加成功')
        }
        drawerVisible.value = false
        emit('success')
      } catch (error) {
        console.error('提交失败', error)
        ElMessage.error('提交失败，请稍后重试')
      }
    }
  })
}

// 监听props.roleUrl变化，更新表单数据
watch(
  () => props.roleUrl,
  (val) => {
    if (val && val.id) {
      Object.assign(form, {
        roleName: val.roleName,
        projectId: val.projectId,
        doc: val.doc,
        routers: val.routers || [],
      })
      // 提取路由ID到routerIds
      routerIds.value = (val.routers || [])
        .filter(router => router.id !== undefined)
        .map(router => router.id as number)

      // 加载项目对应的路由列表
      if (val.projectId) {
        getRouterList(val.projectId)
      }
    }
  },
  { immediate: true, deep: true }
)

// 对外暴露方法
defineExpose({
  open
})
</script>

<style lang="scss" scoped>
.drawer-container {
  padding: 20px;
}
.drawer-footer {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
