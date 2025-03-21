<template>
  <div class="project-view-container">
    <div class="top-section">
      <h2>项目管理</h2>
      <div class="description">
        在此页面可以管理系统中使用的所有项目，支持添加、编辑、删除及状态控制。
      </div>
    </div>
    <ProjectUrlComponent />
  </div>
</template>

<script setup lang="ts" name="ProjectView">
import ProjectUrlComponent from '@/components/repeater/ProjectUrlComponent.vue'
</script>

<style scoped lang="scss">
.project-view-container {
  padding: 20px;
}

.top-section {
  margin-bottom: 20px;

  h2 {
    font-size: var(--font-size-extra-large);
    margin-bottom: 10px;
    color: var(--text-color-primary);
    font-weight: var(--font-weight-semibold);
  }

  .description {
    font-size: var(--font-size-base);
    color: var(--text-color-secondary);
    line-height: var(--line-height-base);
  }
}
</style>
