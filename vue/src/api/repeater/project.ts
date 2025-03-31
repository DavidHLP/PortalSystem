import request from '@/utils/request/request'
import type { PageInfo } from '@/types/common.ts'
import type { Project } from '@/types/repeater/project.ts'

/**
 * 查询项目列表
 * @param data 查询参数
 * @returns 分页结果
 */
export function listProject(data: PageInfo<Project>): Promise<PageInfo<Project>> {
  return request({
    url: '/api/project/list',
    method: 'post',
    data
  })
}

/**
 * 添加项目
 * @param data 项目信息
 * @returns 处理结果
 */
export function addProject(data: Project): Promise<void> {
  return request({
    url: '/api/project/add',
    method: 'post',
    data
  })
}

/**
 * 更新项目
 * @param data 项目信息
 * @returns 处理结果
 */
export function updateProject(data: Project): Promise<void> {
  return request({
    url: '/api/project/update',
    method: 'post',
    data
  })
}

/**
 * 删除项目
 * @param data 项目信息
 * @returns 处理结果
 */
export function deleteProject(data: Project): Promise<void> {
  return request({
    url: '/api/project/delete',
    method: 'post',
    data
  })
}
