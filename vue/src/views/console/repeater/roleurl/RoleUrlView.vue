<template>
  <div class="role-url-view">
    <div class="page-header">
      <h2>角色URL配置管理</h2>
      <p class="description">在此页面可以管理系统中使用的所有角色URL配置，支持添加、编辑、删除及状态控制。</p>
    </div>
    <RoleUrlComponent />
  </div>
</template>

<script setup lang="ts" name="RoleUrlView">
import RoleUrlComponent from '@/components/repeater/RoleUrlComponent.vue'
</script>

<style scoped lang="scss">
.role-url-view {
  padding: 24px;
  background-color: var(--bg-color-container);
  min-height: 100vh;

  .page-header {
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid var(--border-color-light);

    h2 {
      font-size: 24px;
      font-weight: 600;
      color: var(--text-color-primary);
      margin: 0 0 8px;
    }

    .description {
      font-size: 14px;
      color: var(--text-color-secondary);
      line-height: 1.5;
      margin: 0;
    }
  }
}
</style>
