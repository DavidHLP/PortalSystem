import request from '@/utils/request/request'
import type { PageInfo } from '@/types/common'
import type { Project } from '@/types/repeater/project'

/**
 * 分页查询项目列表
 * @param pageInfo 分页参数
 * @returns 分页结果
 */
export function listProject(pageInfo: PageInfo<Project>): Promise<PageInfo<Project>> {
  return request({
    url: '/api/project/list',
    method: 'post',
    data: pageInfo
  })
}

/**
 * 获取所有项目列表（不分页）
 * @returns 项目列表
 */
export function listAllProject(): Promise<Project[]> {
  return request({
    url: '/api/project/listAll',
    method: 'get'
  })
}

/**
 * 添加项目
 * @param project 项目信息
 * @returns 结果
 */
export function addProject(project: Project): Promise<void> {
  return request({
    url: '/api/project/add',
    method: 'post',
    data: project
  })
}

/**
 * 更新项目
 * @param project 项目信息
 * @returns 结果
 */
export function updateProject(project: Project): Promise<void> {
  return request({
    url: '/api/project/update',
    method: 'post',
    data: project
  })
}

/**
 * 删除项目
 * @param project 项目信息
 * @returns 结果
 */
export function deleteProject(project: Project): Promise<void> {
  return request({
    url: '/api/project/delete',
    method: 'post',
    data: project
  })
}
