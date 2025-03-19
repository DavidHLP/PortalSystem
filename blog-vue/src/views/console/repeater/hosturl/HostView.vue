<template>
  <div class="host-view">
    <div class="top-section">
      <h2>主机配置管理</h2>
      <div class="description">
        在此页面可以管理系统中使用的所有主机地址，支持添加、编辑、删除及状态控制。
      </div>
    </div>
      <HostUrlComponent />
  </div>
</template>

<script setup lang="ts" name="HostView">
import HostUrlComponent from '@/components/repeater/HostUrlComponent.vue'
</script>

<style scoped lang="scss">
.host-view {
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
