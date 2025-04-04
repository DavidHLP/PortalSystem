import type { Project } from './project'

/**
 * 角色URL实体类型定义
 */
export interface RoleUrl {
  /**
   * 主键ID
   */
  id: number

  /**
   * 角色名称
   */
  roleName?: string

  /**
   * 项目ID
   */
  projectId?: number

  /**
   * 文档说明
   */
  doc?: string

  /**
   * 创建时间
   */
  gmtCreate?: Date

  /**
   * 修改时间
   */
  gmtModified?: Date

  /**
   * 是否删除:0-未删除,1-已删除
   */
  isDeleted?: number

  /**
   * 项目信息
   */
  project?: Project
}
