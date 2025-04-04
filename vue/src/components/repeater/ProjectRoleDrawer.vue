<template>
  <el-drawer v-model="drawerVisible" title="项目角色列表" size="70%" direction="rtl" destroy-on-close>
    <div v-if="projectData && projectData.roleUrls && projectData.roleUrls.length > 0" class="role-cards-container">
      <el-card v-for="(role, index) in projectData.roleUrls" :key="index" class="role-card">
        <template #header>
          <div class="card-header">
            <span class="role-name">{{ role.roleName }}</span>
            <span class="role-id">角色ID: {{ role.id }}</span>
          </div>
        </template>

        <div class="role-content">
          <div v-if="role.doc" class="markdown-container">
            <div class="markdown-header">
              <h3>角色文档</h3>
              <el-button type="primary" size="small" @click="openFullScreen(role.roleName || '', role.doc || '')">
                <el-icon><FullScreen /></el-icon> 全屏查看
              </el-button>
            </div>
            <div class="markdown-preview">
              <MarkdownView :content="truncateDoc(role.doc)" :theme="'light'" :showCatalog="false" />
            </div>
          </div>
          <el-empty v-else description="暂无角色文档" />
        </div>
      </el-card>
    </div>
    <el-empty v-else description="暂无角色数据" />

    <!-- 全屏文档查看抽屉 -->
    <el-drawer
      v-model="fullDocDrawerVisible"
      :title="fullDocTitle"
      size="90%"
      direction="ttb"
      destroy-on-close
      :append-to-body="true"
      :before-close="closeFullDoc"
    >
      <div class="full-doc-container">
        <MarkdownView :content="fullDocContent" :theme="'light'" :showCatalog="true" />
      </div>
    </el-drawer>
  </el-drawer>
</template>

<script lang="ts" setup>
import { ref, defineProps, defineExpose, watch } from 'vue'
import type { ProjectRoleDTO } from '@/types/repeater/project'
import MarkdownView from '@/components/markdown/MarkdownView.vue'
import { FullScreen } from '@element-plus/icons-vue'

const props = defineProps<{
  project: ProjectRoleDTO | undefined
}>()

const drawerVisible = ref(false)
const projectData = ref<ProjectRoleDTO>()

// 全屏文档相关
const fullDocDrawerVisible = ref(false)
const fullDocContent = ref('')
const fullDocTitle = ref('')

/**
 * 打开抽屉
 */
const open = () => {
  projectData.value = props.project
  drawerVisible.value = true
}

/**
 * 关闭抽屉
 */
const close = () => {
  drawerVisible.value = false
}

/**
 * 截取文档内容
 * @param doc 文档内容
 * @returns 截取后的文档内容
 */
const truncateDoc = (doc: string | undefined): string => {
  if (!doc) return '';
  // 截取前500个字符，保留合适的预览内容
  const maxLength = 500;
  if (doc.length <= maxLength) return doc;

  return doc.substring(0, maxLength) + '\n\n... 点击上方按钮查看完整内容';
}

/**
 * 打开全屏文档
 * @param roleName 角色名称
 * @param doc 文档内容
 */
const openFullScreen = (roleName: string, doc: string) => {
  fullDocTitle.value = `"${roleName}" 角色文档`;
  fullDocContent.value = doc;
  fullDocDrawerVisible.value = true;
}

/**
 * 关闭全屏文档
 */
const closeFullDoc = () => {
  fullDocDrawerVisible.value = false;
}

// 监听project属性变化
watch(() => props.project, (newVal) => {
  projectData.value = newVal
})

// 暴露方法给父组件
defineExpose({
  open,
  close
})
</script>

<style lang="scss" scoped>
.role-cards-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 10px;
}

.role-card {
  transition: all 0.3s;
  height: 100%;

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.role-name {
  font-size: 16px;
  font-weight: bold;
  color: var(--el-color-primary);
}

.role-id {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.role-content {
  min-height: 100px;
}

.markdown-container {
  .markdown-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    border-bottom: 1px solid var(--el-border-color-light);
    padding-bottom: 10px;

    h3 {
      margin: 0;
      color: var(--el-text-color-primary);
    }
  }

  .markdown-preview {
    max-height: 300px;
    overflow-y: auto;
    border: 1px solid var(--el-border-color-lighter);
    border-radius: 4px;
    padding: 10px;
  }
}

.full-doc-container {
  height: 95vh;
  padding: 20px;
}
</style>
