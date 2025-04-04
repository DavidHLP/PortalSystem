<template>
  <!-- 项目编辑抽屉 -->
  <el-drawer
    v-model="visible"
    :title="title"
    size="700px"
    direction="rtl"
    append-to-body
    destroy-on-close
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="80px"
      class="drawer-form"
    >
      <!-- 基本信息区域 -->
      <div class="form-section">
        <div class="section-header">
          <h3>基本信息</h3>
        </div>
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="form.projectName" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="文档说明" prop="doc">
          <md-editor-element
            v-model="form.doc"
            :height="300"
            :min-height="200"
            placeholder="请输入项目文档说明"
          />
        </el-form-item>
      </div>

      <!-- 角色管理区域，仅在编辑模式下显示 -->
      <div v-if="form.id && form.roleUrls && form.roleUrls.length > 0" class="form-section">
        <div class="section-header">
          <h3>角色管理</h3>
        </div>
        <el-table :data="form.roleUrls" border class="role-table">
          <el-table-column prop="id" label="角色ID" width="80" />
          <el-table-column prop="roleName" label="角色名称" />
          <el-table-column label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.isDeleted === 1 ? 'danger' : 'success'">
                {{ scope.row.isDeleted === 1 ? '已禁用' : '已启用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button
                v-if="scope.row.isDeleted === 0"
                type="danger"
                link
                @click="handleDisableRole(scope.row)"
              >
                禁用
              </el-button>
              <el-button
                v-if="scope.row.isDeleted === 1"
                type="success"
                link
                @click="handleEnableRole(scope.row)"
              >
                启用
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="drawer-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-form>
  </el-drawer>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, watchEffect } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { FormInstance } from 'element-plus'
import type { ProjectRoleDTO } from '@/types/repeater/project'
import type { RoleUrl } from '@/types/repeater/roleurl'
import { addProject, updateProject } from '@/api/repeater/project'
import { disableRoleUrl, enableRoleUrl } from '@/api/repeater/roleurl'
import MdEditorElement from '@/components/markdown/MdEditorElement.vue'

// 表单引用
const formRef = ref<FormInstance>()
// 对话框可见性
const visible = ref(false)
// 表单数据
const form = reactive<ProjectRoleDTO>({
  id: 0,
  projectName: '',
  doc: '',
  roleUrls: []
})
// 表单校验规则
const rules = {
  projectName: [
    { required: true, message: '项目名称不能为空', trigger: 'blur' },
    { max: 50, message: '项目名称长度不能超过50个字符', trigger: 'blur' }
  ]
}

// 定义props
const props = defineProps<{
  // 当前编辑的项目对象
  project?: ProjectRoleDTO
}>()

// 定义事件
const emit = defineEmits<{
  // 提交成功事件
  (e: 'success'): void
}>()

/**
 * 处理禁用角色
 * @param role 角色对象
 */
const handleDisableRole = async (role: RoleUrl) => {
  if (!role.id) {
    ElMessage.error('角色ID不能为空')
    return
  }
  try {
    await disableRoleUrl(role.id)
      ElMessage.success('禁用成功')

      // 更新本地数据状态
      if (form.roleUrls) {
        const targetRole = form.roleUrls.find(item => item.id === role.id)
        if (targetRole) {
          targetRole.isDeleted = 1
        }
      }
    } catch (error) {
      console.error('禁用失败', error)
      ElMessage.error('禁用失败，请稍后重试')
    }
}

/**
 * 处理启用角色
 * @param role 角色对象
 */
const handleEnableRole = async (role: RoleUrl) => {
  if (!role.id) {
    ElMessage.error('角色ID不能为空')
    return
  }
  try {
    await enableRoleUrl(role.id)
      ElMessage.success('启用成功')

      // 更新本地数据状态
      if (form.roleUrls) {
        const targetRole = form.roleUrls.find(item => item.id === role.id)
        if (targetRole) {
          targetRole.isDeleted = 0
        }
      }
    } catch (error) {
      console.error('启用失败', error)
    ElMessage.error('启用失败，请稍后重试')
  }
}

// 对话框标题
const title = computed(() => {
  return form.id ? '编辑项目' : '添加项目'
})

// 监听project变化，更新表单数据
watchEffect(() => {
  if (props.project) {
    Object.assign(form, props.project)
  }
})

// 打开对话框
const open = () => {
  visible.value = true
}

// 取消操作
const cancel = () => {
  reset()
  visible.value = false
}

// 重置表单
const reset = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  form.id = 0
  form.projectName = ''
  form.doc = ''
  form.roleUrls = []
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const submitForm = { ...form }

        if (submitForm.id) {
          // 更新
          await updateProject(submitForm)
          ElMessage.success('修改成功')
        } else {
          // 新增
          await addProject(submitForm)
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

// 向外部暴露方法
defineExpose({
  open
})
</script>

<style scoped>
.drawer-form {
  padding: 0 20px;
}

.form-section {
  margin-bottom: 24px;
}

.section-header {
  margin-bottom: 16px;
  border-bottom: 1px solid var(--el-border-color-light);
  padding-bottom: 10px;
}

.section-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
  color: var(--el-text-color-primary);
}

.role-table {
  width: 100%;
}

.drawer-footer {
  margin-top: 24px;
  padding-top: 16px;
  text-align: right;
  border-top: 1px solid var(--el-border-color-light);
}

:deep(.el-form-item__content) {
  display: flex;
}

:deep(.markdown-editor-container) {
  width: 100%;
}
</style>
