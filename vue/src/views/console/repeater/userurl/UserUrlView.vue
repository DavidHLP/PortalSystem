<template>
  <div class="user-url-view">
    <el-card class="page-header">
      <div class="page-title">用户URL管理</div>
      <div class="page-desc">管理系统用户URL配置，支持增删改查操作</div>
    </el-card>

    <UserUrlComponent />
  </div>
</template>

<script lang="ts" setup>
import UserUrlComponent from '@/components/repeater/UserUrlComponent.vue';
</script>

<style scoped>
.user-url-view {
  padding: 15px;
}

.page-header {
  margin-bottom: 20px;
}

.page-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 8px;
}

.page-desc {
  font-size: 14px;
  color: #606266;
}
</style>
