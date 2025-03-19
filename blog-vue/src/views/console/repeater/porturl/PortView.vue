<template>
  <div class="port-view-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>端口管理</span>
          <div class="description">管理服务器端口及其用途说明</div>
        </div>
      </template>

      <div class="feature-description">
        <h3>功能亮点</h3>
        <ul class="feature-list">
          <li>
            <el-icon><Document /></el-icon>
            <span>使用原生Markdown编辑器，编写格式清晰的文档</span>
          </li>
          <li>
            <el-icon><PictureFilled /></el-icon>
            <span>支持图片、表格、代码块等富文本内容</span>
          </li>
          <li>
            <el-icon><Reading /></el-icon>
            <span>标准文档结构，自动生成文档目录</span>
          </li>
          <li>
            <el-icon><Brush /></el-icon>
            <span>原生深色/浅色模式支持，无额外样式覆盖</span>
          </li>
        </ul>
      </div>
    </el-card>
    <port-url-component></port-url-component>
  </div>
</template>

<script setup lang="ts">
import PortUrlComponent from '@/components/repeater/PortUrlComponent.vue'
import { Document, PictureFilled, Reading, Brush } from '@element-plus/icons-vue'
</script>

<style scoped lang="scss">
.port-view-container {
  padding: 20px;
}

.card-header {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.box-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  border: 1px solid var(--vue-color-primary-light-7, #c1e7d5);
  transition: all 0.3s;

  &:hover {
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
    transform: translateY(-2px);
  }

  :deep(.el-card__header) {
    background-color: var(--vue-color-primary-light-9, #f0faf5);
    border-bottom: 1px solid var(--vue-color-primary-light-7, #c1e7d5);
    padding: 15px 20px;
  }
}

.description {
  font-size: 14px;
  color: var(--text-color-secondary, #476582);
}

.feature-description {
  padding: 10px 0;

  h3 {
    font-size: 16px;
    margin-top: 0;
    margin-bottom: 16px;
    color: var(--text-color-primary, #273849);
    font-weight: var(--font-weight-medium, 500);
  }
}

.feature-list {
  list-style: none;
  padding: 0;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  grid-gap: 16px;

  li {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 12px 16px;
    background-color: var(--vue-color-primary-light-9, #f0faf5);
    border-radius: 8px;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
      background-color: var(--vue-color-primary-light-8, #e0f3eb);
    }

    .el-icon {
      color: var(--vue-color-primary, #42b883);
      font-size: 20px;
    }

    span {
      color: var(--text-color-secondary, #476582);
      font-size: 14px;
    }
  }
}

@media (prefers-color-scheme: dark) {
  .box-card {
    background-color: rgba(30, 30, 30, 0.6);
    border-color: rgba(66, 184, 131, 0.2);

    :deep(.el-card__header) {
      background-color: rgba(35, 35, 35, 0.8);
      border-bottom-color: rgba(66, 184, 131, 0.2);
    }
  }

  .description {
    color: #aaaaaa;
  }

  .feature-description {
    h3 {
      color: #e0e0e0;
    }
  }

  .feature-list {
    li {
      background-color: rgba(35, 35, 35, 0.5);
      border: 1px solid rgba(66, 184, 131, 0.2);

      &:hover {
        background-color: rgba(66, 184, 131, 0.15);
      }

      .el-icon {
        color: #5ccb9c;
      }

      span {
        color: #cccccc;
      }
    }
  }
}

@media screen and (max-width: 768px) {
  .feature-list {
    grid-template-columns: 1fr;
  }
}
</style>
