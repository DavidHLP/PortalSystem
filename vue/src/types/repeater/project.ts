import type { RoleUrl } from './roleurl'

/**
 * 项目信息接口
 */
export interface Project {
  /**
   * 主键ID
   */
  id: number | string

  /**
   * 项目名称
   */
  projectName?: string

  /**
   * 文档说明
   */
  doc?: string

  /**
   * 创建时间
   */
  gmtCreate?: string

  /**
   * 修改时间
   */
  gmtModified?: string

  /**
   * 是否删除:0-未删除,1-已删除
   */
  isDeleted?: number
}

/**
 * 项目角色DTO
 */
export interface ProjectRoleDTO extends Project {
  roleUrls?: RoleUrl[]
}