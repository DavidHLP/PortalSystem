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
import type { Project } from '@/types/repeater/project'
import { addProject, updateProject } from '@/api/repeater/project'
import MdEditorElement from '@/components/markdown/MdEditorElement.vue'

// 表单引用
const formRef = ref<FormInstance>()
// 对话框可见性
const visible = ref(false)
// 表单数据
const form = reactive<Project>({
  id: undefined,
  projectName: '',
  doc: ''
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
  project?: Project
}>()

// 定义事件
const emit = defineEmits<{
  // 提交成功事件
  (e: 'success'): void
}>()

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
  form.id = undefined
  form.projectName = ''
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
