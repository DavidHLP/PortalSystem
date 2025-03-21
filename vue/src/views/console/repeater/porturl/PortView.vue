<template>
  <div class="port-view-container">
    <div class="top-section">
      <h2>端口管理</h2>
      <div class="description">
        在此页面可以管理系统中使用的所有端口，支持添加、编辑、删除及状态控制。
      </div>
      <port-url-component></port-url-component>
    </div>
  </div>
</template>

<script setup lang="ts">
import PortUrlComponent from '@/components/repeater/PortUrlComponent.vue'
</script>

<style scoped lang="scss">

.port-view-container {
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
