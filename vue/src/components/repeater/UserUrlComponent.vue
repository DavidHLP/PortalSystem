<template>
  <div class="user-url-container">
    <!-- 顶部卡片 -->
    <el-card class="user-url-card">
      <!-- 搜索区域 -->
      <div class="search-box">
        <el-form :inline="true" :model="searchForm" class="demo-form-inline">
          <el-row :gutter="20" class="search-row">
            <el-col :xs="24" :sm="12" :md="8" :lg="6">
              <el-form-item label="用户名">
                <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable prefix-icon="User"></el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="12" :md="8" :lg="6">
              <el-form-item label="邮箱">
                <el-input v-model="searchForm.email" placeholder="请输入邮箱" clearable prefix-icon="Message"></el-input>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="8" :md="6" :lg="4">
              <el-form-item label="状态">
                <el-select v-model="searchForm.isActive" placeholder="请选择状态" clearable class="form-item equal-width">
                  <el-option label="启用" :value="true"></el-option>
                  <el-option label="禁用" :value="false"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="8" :md="6" :lg="4">
              <el-form-item label="角色">
                <el-select v-model="searchForm.roleId" placeholder="请选择角色" clearable class="form-item equal-width">
                  <el-option v-for="role in roleOptions" :key="role.id" :label="role.roleName" :value="role.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="8" :md="6" :lg="4">
              <el-form-item label="项目">
                <el-select v-model="searchForm.projectId" placeholder="请选择项目" clearable class="form-item equal-width">
                  <el-option v-for="project in projectOptions" :key="project.id" :label="project.projectName" :value="project.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="24" :md="14" :lg="6">
              <el-form-item class="action-buttons">
                <el-button type="primary" @click="handleSearch" icon="Search">查询</el-button>
                <el-button @click="resetSearch" icon="RefreshRight">重置</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>

      <!-- 表格工具栏 -->
      <div class="table-toolbar">
        <div class="left-actions">
          <el-button-group>
            <el-button type="primary" plain icon="RefreshLeft" @click="fetchData">刷新</el-button>
            <el-button type="primary" plain icon="Plus" @click="handleAdd">新增用户</el-button>
            <el-button type="danger" plain icon="Delete" :disabled="!selectedRows.length" @click="handleBatchDelete">批量删除</el-button>
          </el-button-group>
          <span class="selected-count" v-if="selectedRows.length > 0">
            已选择 <el-tag type="info">{{ selectedRows.length }}</el-tag> 项
          </span>
        </div>
        <div class="right-actions">
          <el-tooltip content="密度" placement="top">
            <el-dropdown trigger="click">
              <el-button icon="Grid" circle plain></el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="tableSize = 'large'">宽松</el-dropdown-item>
                  <el-dropdown-item @click="tableSize = 'default'">默认</el-dropdown-item>
                  <el-dropdown-item @click="tableSize = 'small'">紧凑</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-tooltip>
        </div>
      </div>

      <!-- 数据表格 -->
      <el-table
        :data="tableData"
        v-loading="tableLoading"
        border
        style="width: 100%"
        :size="tableSize"
        @selection-change="handleSelectionChange"
        highlight-current-row
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="ID" width="80" align="center" sortable></el-table-column>
        <el-table-column prop="username" label="用户名" min-width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="roleId" label="角色" width="120" align="center">
          <template #default="{ row }">
            <el-tag size="small" type="info" effect="plain">{{ getRoleName(row.roleId) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="projectId" label="项目" width="120" align="center">
          <template #default="{ row }">
            <el-tag size="small" type="success" effect="plain">{{ getProjectName(row.projectId) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isActive" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.isActive ? 'success' : 'danger'" effect="dark">
              {{ row.isActive ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="180" align="center">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button-group>
              <el-tooltip content="编辑" placement="top">
                <el-button
                  size="small"
                  @click="handleEdit(row)"
                  type="primary"
                  icon="Edit"
                  circle
                ></el-button>
              </el-tooltip>
              <el-tooltip :content="row.isActive ? '禁用' : '启用'" placement="top">
                <el-button
                  size="small"
                  @click="handleToggleStatus(row)"
                  :type="row.isActive ? 'warning' : 'success'"
                  :icon="row.isActive ? 'TurnOff' : 'Open'"
                  circle
                ></el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button
                  size="small"
                  @click="handleDelete(row)"
                  type="danger"
                  icon="Delete"
                  circle
                ></el-button>
              </el-tooltip>
            </el-button-group>
          </template>
        </el-table-column>
        <template #empty>
          <div class="empty-data">
            <el-empty description="暂无数据" :image-size="100">
              <el-button type="primary" @click="handleAdd">添加用户</el-button>
            </el-empty>
          </div>
        </template>
      </el-table>

      <!-- 分页区域 -->
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          :current-page="currentPage"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </el-card>

    <!-- 新增/编辑抽屉 -->
    <el-drawer
      v-model="drawerVisible"
      :title="dialogType === 'add' ? '新增用户' : '编辑用户'"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      size="60%"
      destroy-on-close
    >
      <div class="drawer-content">
        <el-form
          ref="formRef"
          :model="form"
          :rules="formRules"
          label-width="100px"
          class="drawer-form"
          :status-icon="true"
        >
          <el-alert
            v-if="dialogType === 'edit'"
            title="您正在编辑现有用户，请谨慎操作。"
            type="warning"
            :closable="false"
            show-icon
            style="margin-bottom: 20px;"
          ></el-alert>

          <el-form-item label="用户名" prop="username">
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              prefix-icon="User"
            ></el-input>
          </el-form-item>

          <el-form-item
            label="密码"
            prop="passwordHash"
            v-if="dialogType === 'add' || showPasswordField"
          >
            <el-input
              v-model="form.passwordHash"
              type="password"
              placeholder="请输入密码"
              show-password
              prefix-icon="Lock"
            ></el-input>
            <template v-if="dialogType === 'edit'">
              <el-checkbox v-model="showPasswordField" class="password-toggle">
                修改密码
              </el-checkbox>
            </template>
          </el-form-item>

          <el-form-item label="邮箱" prop="email">
            <el-input
              v-model="form.email"
              placeholder="请输入邮箱"
              prefix-icon="Message"
            ></el-input>
          </el-form-item>

          <el-form-item label="角色" prop="roleId">
            <el-select v-model="form.roleId" placeholder="请选择角色" filterable class="form-item equal-width">
              <el-option v-for="role in roleOptions" :key="role.id" :label="role.roleName" :value="role.id" />
            </el-select>
          </el-form-item>

          <el-form-item label="项目" prop="projectId">
            <el-select v-model="form.projectId" placeholder="请选择项目" filterable class="form-item equal-width">
              <el-option v-for="project in projectOptions" :key="project.id" :label="project.projectName" :value="project.id" />
            </el-select>
          </el-form-item>

          <el-form-item label="状态" prop="isActive">
            <el-switch
              v-model="form.isActive"
              :active-value="true"
              :inactive-value="false"
              active-text="启用"
              inactive-text="禁用"
              inline-prompt
            ></el-switch>
          </el-form-item>
        </el-form>

        <div class="drawer-footer">
          <el-button @click="drawerVisible = false" icon="Close">取 消</el-button>
          <el-button type="primary" @click="submitForm" icon="Check" :loading="submitLoading">确 定</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
import {
  getUserUrls,
  createUserUrl,
  updateUserUrl,
  deleteUserUrl,
  type UserUrl
} from '@/api/repeater/userurl';
import { listAll as listAllRoles } from '@/api/repeater/roleurl';
import { listAll as listAllProjects } from '@/api/repeater/projecturl';
import type { RoleUrl } from '@/types/repeater/roleurl';
import type { ProjectUrl } from '@/api/repeater/projecturl';
import { formatDate } from '@/utils/date';

// 角色和项目选项
const roleOptions = ref<RoleUrl[]>([]);
const projectOptions = ref<ProjectUrl[]>([]);

// 表格数据
const tableData = ref<UserUrl[]>([]);
const tableLoading = ref(false);
const selectedRows = ref<UserUrl[]>([]);
const tableSize = ref('default');
const submitLoading = ref(false);

// 分页参数
const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

// 搜索表单
const searchForm = reactive({
  username: '',
  email: '',
  roleId: undefined as number | undefined,
  projectId: undefined as number | undefined,
  isActive: undefined as boolean | undefined
});

// 表单相关
const drawerVisible = ref(false);
const dialogType = ref<'add' | 'edit'>('add');
const formRef = ref<FormInstance>();
const showPasswordField = ref(false);
const form = reactive<UserUrl>({
  id: undefined,
  username: '',
  passwordHash: '',
  email: '',
  roleId: undefined,
  projectId: undefined,
  isActive: true
});

// 表单校验规则
const formRules: FormRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '长度在 3 到 50 个字符', trigger: 'blur' }
  ],
  passwordHash: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: false, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  roleId: [
    { type: 'number', message: '角色ID必须为数字', trigger: 'blur' }
  ],
  projectId: [
    { type: 'number', message: '项目ID必须为数字', trigger: 'blur' }
  ]
};

// 初始化
onMounted(() => {
  fetchData();
  fetchRoles();
  fetchProjects();
});

// 获取表格数据
const fetchData = async () => {
  tableLoading.value = true;
  try {
    const params = {
      page: currentPage.value,
      limit: pageSize.value,
      ...searchForm
    };
    const response = await getUserUrls(params);
    tableData.value = response.items || [];
    total.value = response.total || 0;
  } catch (error) {
    console.error('获取用户列表失败', error);
    ElMessage.error('获取数据失败');
  } finally {
    tableLoading.value = false;
  }
};

// 获取所有角色
const fetchRoles = async () => {
  try {
    const res = await listAllRoles();
    roleOptions.value = res || [];
  } catch (error) {
    console.error('获取角色列表失败', error);
  }
};

// 获取所有项目
const fetchProjects = async () => {
  try {
    const res = await listAllProjects();
    projectOptions.value = res || [];
  } catch (error) {
    console.error('获取项目列表失败', error);
  }
};

// 根据ID获取角色名称
const getRoleName = (roleId?: number) => {
  if (!roleId) return '-';
  const role = roleOptions.value.find((r) => r.id === roleId);
  return role ? role.roleName : roleId;
};

// 根据ID获取项目名称
const getProjectName = (projectId?: number) => {
  if (!projectId) return '-';
  const project = projectOptions.value.find((p) => p.id === projectId);
  return project ? project.projectName : projectId;
};

// 处理表格选择变化
const handleSelectionChange = (rows: UserUrl[]) => {
  selectedRows.value = rows;
};

// 搜索
const handleSearch = () => {
  currentPage.value = 1;
  fetchData();
};

// 重置搜索
const resetSearch = () => {
  searchForm.username = '';
  searchForm.email = '';
  searchForm.roleId = undefined;
  searchForm.projectId = undefined;
  searchForm.isActive = undefined;
  handleSearch();
};

// 切换用户状态
const handleToggleStatus = async (row: UserUrl) => {
  if (!row.id) return;

  try {
    const updatedUser = { ...row, isActive: !row.isActive };
    await updateUserUrl(row.id, updatedUser);
    ElMessage.success(`已${row.isActive ? '禁用' : '启用'}用户`);
    fetchData();
  } catch (error) {
    console.error('更新状态失败', error);
    ElMessage.error('更新状态失败');
  }
};

// 新增
const handleAdd = () => {
  dialogType.value = 'add';
  resetForm();
  drawerVisible.value = true;
  showPasswordField.value = true;
};

// 编辑
const handleEdit = (row: UserUrl) => {
  dialogType.value = 'edit';
  resetForm();
  nextTick(() => {
    Object.assign(form, { ...row });
    // 清空密码字段
    form.passwordHash = '';
    showPasswordField.value = false;
  });
  drawerVisible.value = true;
};

// 删除
const handleDelete = (row: UserUrl) => {
  ElMessageBox.confirm(`确定要删除用户 "${row.username}" 吗？`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      if (row.id) {
        await deleteUserUrl(row.id);
        ElMessage.success('删除成功');
        fetchData();
      }
    } catch (error) {
      console.error('删除失败', error);
      ElMessage.error('删除失败');
    }
  }).catch(() => {});
};

// 批量删除
const handleBatchDelete = () => {
  if (selectedRows.value.length === 0) return;

  ElMessageBox.confirm(`确认删除选中的 ${selectedRows.value.length} 个用户吗？`, '批量删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const promises = selectedRows.value
        .filter(row => row.id) // 过滤有ID的行
        .map(row => deleteUserUrl(row.id as number));

      await Promise.all(promises);
      ElMessage.success(`成功删除 ${selectedRows.value.length} 个用户`);
      fetchData();
    } catch (error) {
      console.error('批量删除失败', error);
      ElMessage.error('批量删除失败');
    }
  }).catch(() => {});
};

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return;

  await formRef.value.validate(async (valid) => {
    if (!valid) return;

    submitLoading.value = true;
    try {
      if (dialogType.value === 'add') {
        await createUserUrl(form);
        ElMessage.success('添加成功');
      } else {
        // 如果是编辑模式且未选择修改密码，则不传递密码字段
        const submitData = { ...form };
        if (!showPasswordField.value) {
          delete submitData.passwordHash;
        }
        await updateUserUrl(form.id as number, submitData);
        ElMessage.success('更新成功');
      }
      drawerVisible.value = false;
      fetchData();
    } catch (error) {
      console.error('提交失败', error);
      ElMessage.error('提交失败');
    } finally {
      submitLoading.value = false;
    }
  });
};

// 重置表单
const resetForm = () => {
  Object.assign(form, {
    id: undefined,
    username: '',
    passwordHash: '',
    email: '',
    roleId: undefined,
    projectId: undefined,
    isActive: true
  });
  if (formRef.value) {
    formRef.value.resetFields();
  }
};

// 分页大小变化
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  fetchData();
};

// 页码变化
const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  fetchData();
};
</script>

<style scoped lang="scss">
.user-url-container {
  padding: 20px;

  .user-url-card {
    margin-bottom: 20px;
    transition: all 0.3s;
    border-radius: 8px;
    overflow: hidden;
  }

  .search-box {
    margin-bottom: 20px;
    background-color: var(--vue-color-primary-light-9);
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
    border: 1px solid var(--vue-color-primary-light-7);
    transition: all 0.3s;

    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      transform: translateY(-2px);
    }

    .search-row {
      width: 100%;
    }

    .action-buttons {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      margin-bottom: 0;
    }

    .el-form-item {
      margin-bottom: 0;
      width: 100%;
    }

    .el-button {
      margin-left: 8px;
      font-weight: var(--font-weight-medium);
    }
  }

  .table-toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    .left-actions, .right-actions {
      display: flex;
      align-items: center;
      gap: 12px;
    }

    .selected-count {
      margin-left: 16px;
      font-size: 14px;
    }
  }

  .el-table {
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
    margin-bottom: 15px;

    :deep(th.el-table__cell) {
      background-color: var(--vue-color-primary-light-9);
      color: var(--text-color-primary);
      font-weight: var(--font-weight-semibold);
      padding: 14px 12px;
    }

    :deep(td.el-table__cell) {
      color: var(--text-color-secondary);
      padding: 14px 12px;
    }

    .empty-data {
      padding: 40px 0;
    }
  }

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;

    .el-pagination {
      padding: 0;
      font-weight: normal;
    }
  }

  // 抽屉样式
  :deep(.el-drawer) {
    .el-drawer__header {
      margin-bottom: 0;
      padding: 16px 20px;
      background-color: var(--vue-color-primary-light-9);
      border-bottom: 1px solid var(--vue-color-primary-light-7);

      .el-drawer__title {
        color: var(--text-color-primary);
        font-weight: var(--font-weight-semibold);
        font-size: var(--font-size-large);
      }
    }

    .el-drawer__body {
      padding: 0;
    }
  }

  .drawer-content {
    height: 100%;
    display: flex;
    flex-direction: column;
  }

  .drawer-form {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
  }

  .drawer-footer {
    padding: 15px 20px;
    text-align: right;
    border-top: 1px solid var(--vue-color-secondary-light-8);
    background-color: var(--vue-color-secondary-light-9);

    .el-button {
      margin-left: 8px;
      min-width: 90px;
      transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
      position: relative;
      overflow: hidden;

      &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: linear-gradient(120deg, rgba(255,255,255,0) 30%, rgba(255,255,255,0.4) 50%, rgba(255,255,255,0) 70%);
        transform: translateX(-100%);
        transition: all 0.6s ease;
      }

      &--primary {
        background: linear-gradient(135deg, var(--vue-color-primary), var(--vue-color-primary-light-1));
        border: none;
        color: white;
        box-shadow: 0 4px 10px rgba(66, 184, 131, 0.3);

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 6px 15px rgba(66, 184, 131, 0.4);

          &::before {
            transform: translateX(100%);
          }
        }

        &:active {
          transform: translateY(0);
          box-shadow: 0 2px 8px rgba(66, 184, 131, 0.3);
        }
      }

      &:not(.el-button--primary) {
        border: 1px solid var(--el-border-color);
        background: white;

        &:hover {
          border-color: var(--vue-color-primary-light-5);
          color: var(--vue-color-primary);
          transform: translateY(-2px);
          box-shadow: 0 6px 15px rgba(0, 0, 0, 0.05);

          &::before {
            transform: translateX(100%);
          }
        }

        &:active {
          transform: translateY(0);
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
        }
      }
    }
  }

  .password-toggle {
    margin-top: 5px;
  }

  .form-item {
    &.equal-width {
      width: 100%;
    }
  }
}

// 暗色模式适配
@media (prefers-color-scheme: dark) {
  .user-url-container {
    .search-box {
      background-color: rgba(66, 184, 131, 0.1);
      border-color: rgba(66, 184, 131, 0.2);
      box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
    }

    .el-table {
      background-color: var(--el-bg-color);
      color: var(--el-text-color-primary);
      border: 1px solid rgba(66, 184, 131, 0.2);

      :deep(th.el-table__cell) {
        background-color: rgba(66, 184, 131, 0.15);
        border-bottom: 2px solid rgba(66, 184, 131, 0.3);
        color: #ffffff;
      }

      :deep(td.el-table__cell) {
        border-bottom: 1px solid rgba(255, 255, 255, 0.05);
      }
    }

    .drawer-footer {
      background-color: rgba(35, 35, 35, 0.8);
      border-top-color: rgba(66, 184, 131, 0.2);

      .el-button--primary {
        background: linear-gradient(135deg, #3ba676, #42b883);
        box-shadow: 0 4px 10px rgba(45, 125, 89, 0.4);

        &:hover {
          background: linear-gradient(135deg, #42b883, #3ba676);
          box-shadow: 0 6px 15px rgba(45, 125, 89, 0.5);
        }
      }

      .el-button:not(.el-button--primary) {
        background: rgba(40, 40, 40, 0.8);
        border-color: rgba(80, 80, 80, 0.5);
        color: #e0e0e0;

        &:hover {
          background: rgba(50, 50, 50, 0.9);
          border-color: rgba(66, 184, 131, 0.5);
          color: #5ccb9c;
        }
      }
    }
  }
}

@media screen and (max-width: 768px) {
  .user-url-container {
    padding: 10px;

    .search-box {
      padding: 15px;

      .action-buttons {
        justify-content: center;
        margin-top: 10px;
      }
    }

    .table-toolbar {
      flex-direction: column;
      gap: 10px;

      .left-actions, .right-actions {
        width: 100%;
        justify-content: space-between;
      }
    }

    .drawer-form {
      padding: 15px;
    }
  }
}
</style>
