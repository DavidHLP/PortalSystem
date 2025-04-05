import request from '@/utils/request/request'
import type { PageInfo } from '@/types/common'
import type { RoleUrl } from '@/types/repeater/roleurl.ts'

/**
 * 分页查询角色URL列表
 * @param pageInfo 分页参数
 * @returns 分页结果
 */
export function listRoleUrl(pageInfo: PageInfo<RoleUrl>): Promise<PageInfo<RoleUrl>> {
  return request({
    url: '/api/roleUrl/list',
    method: 'post',
    data: pageInfo
  })
}

/**
 * 添加角色URL
 * @param roleUrl 角色URL信息
 * @returns 结果
 */
export function addRoleUrl(roleUrl: RoleUrl): Promise<void> {
  return request({
    url: '/api/roleUrl/add',
    method: 'post',
    data: roleUrl
  })
}

/**
 * 更新角色URL
 * @param roleUrl 角色URL信息
 * @returns 结果
 */
export function updateRoleUrl(roleUrl: RoleUrl): Promise<void> {
  return request({
    url: '/api/roleUrl/update',
    method: 'post',
    data: roleUrl
  })
}

/**
 * 删除角色URL
 * @param roleUrl 角色URL信息
 * @returns 结果
 */
export function deleteRoleUrl(roleUrl: RoleUrl): Promise<void> {
  return request({
    url: '/api/roleUrl/delete',
    method: 'post',
    data: roleUrl
  })
}

export function getRoleList(): Promise<RoleUrl[]> {
  return request({
    url: '/api/roleUrl/getRoleList',
    method: 'post',
  })
}

export function disableRoleUrl(id: number): Promise<void> {
  return request({
    url: '/api/roleUrl/disable',
    method: 'post',
    data: { id: id }
  })
}

export function enableRoleUrl(id: number): Promise<void> {
  return request({
    url: '/api/roleUrl/enable',
    method: 'post',
    data: { id: id }
  })
}