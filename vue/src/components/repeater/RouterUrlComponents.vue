<template>
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
      label-width="100px"
      class="drawer-form"
    >
      <el-form-item label="主机地址" prop="host">
        <el-input v-model="form.host" placeholder="请输入主机地址" />
      </el-form-item>
      <el-form-item label="端口号" prop="port">
        <el-input v-model="form.port" placeholder="请输入端口号" />
      </el-form-item>
      <el-form-item label="路由路径" prop="router">
        <el-input v-model="form.router" placeholder="请输入路由路径" />
      </el-form-item>
      <el-form-item label="协议类型" prop="protocol">
        <el-select v-model="form.protocol" placeholder="请选择协议类型">
          <el-option label="HTTP" value="HTTP" />
          <el-option label="HTTPS" value="HTTPS" />
          <el-option label="TCP" value="TCP" />
          <el-option label="UDP" value="UDP" />
        </el-select>
      </el-form-item>
      <el-form-item label="唯一标识" prop="uniqueId">
        <el-input v-model="form.uniqueId" placeholder="请输入唯一标识" />
      </el-form-item>
      <el-form-item label="路由类型" prop="type">
        <el-radio-group v-model="form.type">
          <el-radio :label="0">内部</el-radio>
          <el-radio :label="1">外部</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="文档说明" prop="doc">
        <md-editor-element
          v-model="form.doc"
          :height="300"
          :min-height="200"
          placeholder="请输入文档说明"
        />
      </el-form-item>
      <div class="drawer-footer">
        <el-button @click="cancel">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-form>
  </el-drawer>
</template>

<script lang="ts" setup>
import { ref, reactive, computed, watchEffect } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance } from 'element-plus'
import type { RouterUrl } from '@/types/repeater/routerurl'
import { addRouterUrl, updateRouterUrl } from '@/api/repeater/routerurl'
import MdEditorElement from '@/components/markdown/MdEditorElement.vue'

// 表单引用
const formRef = ref<FormInstance>()
// 对话框可见性
const visible = ref(false)
// 表单数据
const form = reactive<RouterUrl>({
  id: undefined,
  host: '',
  port: "",
  router: '',
  protocol: 'HTTP',
  uniqueId: '',
  type: "",
  doc: ''
})
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
  ]
}

// 定义props
const props = defineProps<{
  // 当前编辑的路由URL对象
  routerUrl?: RouterUrl
}>()

// 定义事件
const emit = defineEmits<{
  // 提交成功事件
  (e: 'success'): void
}>()

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
    Object.assign(form, routerUrlCopy)
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
  form.id = undefined
  form.host = ''
  form.port = ''
  form.router = ''
  form.protocol = 'HTTP'
  form.uniqueId = ''
  form.type = ''
  form.doc = ''
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

// 向外部暴露方法
defineExpose({
  open
})
</script>

<style scoped>
.drawer-form {
  padding: 0 20px;
}

.drawer-footer {
  padding-top: 20px;
  text-align: right;
  margin-bottom: 20px;
}

:deep(.el-form-item__content) {
  display: flex;
}

:deep(.markdown-editor-container) {
  width: 100%;
}
</style>
