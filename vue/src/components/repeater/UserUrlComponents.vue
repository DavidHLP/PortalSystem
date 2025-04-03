<template>
  <div class="user-url-components">
    <!-- 添加/编辑用户对话框 -->
    <el-drawer
      :title="dialogTitle"
      v-model="dialogVisible"
      size="500px"
      :close-on-click-modal="false"
      direction="rtl"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        label-position="right"
        style="padding: 0 20px;"
      >
        <el-form-item label="用户名" prop="username" v-if="dialogStatus !== 'password'">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email" v-if="dialogStatus !== 'password'">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="dialogStatus === 'add' || dialogStatus === 'password'">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="角色" prop="roleId" v-if="dialogStatus !== 'password'">
          <el-select
            v-model="form.roleId"
            placeholder="请选择角色"
            filterable
            remote
            :remote-method="remoteSearch"
            :loading="loading">
            <el-option
              v-for="role in roleOptions"
              :key="role.id"
              :label="role.roleName"
              :value="role.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status" v-if="dialogStatus !== 'password'">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">正常</el-radio>
            <el-radio :label="1">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div style="flex: auto; display: flex; justify-content: flex-end; padding-right: 20px;">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认</el-button>
        </div>
      </template>
    </el-drawer>

    <!-- 删除确认对话框 -->
    <el-drawer
      title="删除确认"
      v-model="deleteDialogVisible"
      size="400px"
      :close-on-click-modal="false"
      direction="rtl"
    >
      <div style="padding: 0 20px;">
        <p>确定要删除用户 "{{ deleteUsername }}" 吗？</p>
      </div>
      <template #footer>
        <div style="flex: auto; display: flex; justify-content: flex-end; padding-right: 20px;">
          <el-button @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="confirmDelete">确认删除</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, defineEmits, defineExpose, onMounted } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'
import {
  addUserRoleProject,
  updateUserRoleProject,
  deleteUserRoleProject,
  updateUserPassword
} from '@/api/repeater/userurl'
import { getRoleList } from '@/api/repeater/roleurl'

// 定义发射事件
const emit = defineEmits(['refresh'])

// 表单引用
const formRef = ref<FormInstance>()

// 对话框状态
const dialogVisible = ref(false)
const dialogStatus = ref<'add' | 'edit' | 'password'>('add')
const dialogTitle = ref('添加用户')

// 删除对话框
const deleteDialogVisible = ref(false)
const deleteUsername = ref('')
const deleteForm = reactive({
  id: undefined as number | undefined,
  roleId: undefined as number | undefined
})

// 表单数据
const form = reactive({
  id: undefined as number | undefined,
  username: '',
  email: '',
  password: '',
  roleId: undefined as number | undefined,
  status: 0
})

// 表单验证规则
const rules = reactive<FormRules>({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  roleId: [{ required: true, message: '请选择角色', trigger: 'change' }]
})

// 角色选项（实际使用时需要从后端获取）
const roleOptions = ref<{ id: number; roleName: string }[]>([])
const loading = ref(false)

// 获取所有角色选项
const getRoleOptions = async () => {
  loading.value = true
  try {
    const res = await getRoleList()
    roleOptions.value = res.map((item: any) => ({
      id: item.id,
      roleName: item.roleName
    }))
  } catch (error) {
    ElMessage.error('获取角色列表失败')
  } finally {
    loading.value = false
  }
}

// 远程搜索方法
const remoteSearch = async (query: string) => {
  if (query !== '') {
    loading.value = true
    try {
      const res = await getRoleList()
      // 前端过滤搜索结果
      roleOptions.value = res
        .filter((item: any) => item.roleName.toLowerCase().includes(query.toLowerCase()))
        .map((item: any) => ({
          id: item.id,
          roleName: item.roleName
        }))
    } catch (error) {
      ElMessage.error('搜索角色失败')
    } finally {
      loading.value = false
    }
  } else {
    // 如果查询为空，则获取所有角色
    getRoleOptions()
  }
}

// 打开添加对话框
const handleAdd = () => {
  resetForm()
  dialogStatus.value = 'add'
  dialogTitle.value = '添加用户'
  dialogVisible.value = true
}

// 打开编辑对话框
const handleEdit = (row: any) => {
  resetForm()
  dialogStatus.value = 'edit'
  dialogTitle.value = '编辑用户'
  Object.assign(form, {
    id: row.id,
    username: row.username,
    email: row.email,
    roleId: row.roleId,
    status: row.status
  })
  dialogVisible.value = true
}

// 打开修改密码对话框
const handlePassword = (row: any) => {
  resetForm()
  dialogStatus.value = 'password'
  dialogTitle.value = '修改密码'
  form.id = row.id
  dialogVisible.value = true
}

// 打开删除对话框
const handleDelete = (row: any) => {
  deleteUsername.value = row.username
  deleteForm.id = row.id
  deleteForm.roleId = row.roleId
  deleteDialogVisible.value = true
}

// 确认删除
const confirmDelete = async () => {
  try {
    await deleteUserRoleProject(deleteForm)
    ElMessage.success('删除成功')
    deleteDialogVisible.value = false
    emit('refresh')
  } catch (error: any) {
    ElMessage.error(error.message || '删除失败')
  }
}

// 表单提交
const submitForm = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        if (dialogStatus.value === 'add') {
          await addUserRoleProject(form)
          ElMessage.success('添加成功')
        } else if (dialogStatus.value === 'edit') {
          await updateUserRoleProject(form)
          ElMessage.success('更新成功')
        } else if (dialogStatus.value === 'password') {
          console.log('form', form)
          await updateUserPassword(form)
          ElMessage.success('密码修改成功')
        }
        dialogVisible.value = false
        emit('refresh')
      } catch (error: any) {
        ElMessage.error(error.message || '操作失败')
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(form, {
    id: undefined,
    username: '',
    email: '',
    password: '',
    roleId: undefined,
    status: 0
  })
}

// 公开方法
defineExpose({
  handleAdd,
  handleEdit,
  handlePassword,
  handleDelete
})

onMounted(() => {
  getRoleOptions()
})
</script>

<style scoped lang="scss">
.user-url-components {
  .el-dialog-footer {
    display: flex;
    justify-content: flex-end;
    margin-top: 20px;
  }
}
</style>
