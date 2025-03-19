<template>
  <div class="router-url-component">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>路由URL管理</span>
          <el-button type="primary" @click="handleAdd">添加路由</el-button>
        </div>
      </template>

      <el-table :data="routerUrls" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="path" label="路径" />
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '添加路由' : '编辑路由'"
      width="500px"
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="路径">
          <el-input v-model="form.path" placeholder="请输入路由路径" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRouterUrls, addRouterUrl, updateRouterUrl, deleteRouterUrl } from '@/api/repeater/routerurl'

const routerUrls = ref([])
const dialogVisible = ref(false)
const dialogType = ref('add')
const form = ref({
  path: ''
})

const fetchData = async () => {
  try {
    const response = await getRouterUrls()
    routerUrls.value = response.items
  } catch (error) {
    ElMessage.error('获取数据失败')
  }
}

const handleAdd = () => {
  dialogType.value = 'add'
  form.value = { path: '' }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  form.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该路由吗？', '提示', {
      type: 'warning'
    })
    await deleteRouterUrl(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSubmit = async () => {
  try {
    if (dialogType.value === 'add') {
      await addRouterUrl(form.value)
      ElMessage.success('添加成功')
    } else {
      await updateRouterUrl(form.value.id, form.value)
      ElMessage.success('更新成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    ElMessage.error(dialogType.value === 'add' ? '添加失败' : '更新失败')
  }
}

const formatDate = (date) => {
  return new Date(date).toLocaleString()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.router-url-component {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
