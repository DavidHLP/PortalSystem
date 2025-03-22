import request from '@/utils/request/request'
import type { RoleUrl } from '@/types/repeater/roleurl'
import type { PageInfo } from '@/utils/types/common'

export const getRoleUrlPage = (params: {
  page: number
  limit: number
  roleName?: string
  projectId?: number
}) => {
  return request.get<PageInfo<RoleUrl>>('/api/repeater/roleurl/page', { params })
}

export const getRoleUrlById = (id: number) => {
  return request.get<RoleUrl>(`/api/repeater/roleurl/${id}`)
}

export const createRoleUrl = (data: RoleUrl) => {
  return request.post<RoleUrl>('/api/repeater/roleurl', data)
}

export const updateRoleUrl = (data: RoleUrl) => {
  return request.put<RoleUrl>('/api/repeater/roleurl', data)
}

export const deleteRoleUrl = (id: number) => {
  return request.delete(`/api/repeater/roleurl/${id}`)
}

export const getRoleUrlListByRoleId = (roleId: number):Promise<RoleUrl[]> => {
  return request({
    url: `/api/repeater/roleurl/getRoleUrlListByRoleId?roleId=${roleId}`,
    method: 'get'
  })
}

export const batchAddUrls = (roleId: number, urlIds: number[]) => {
  return request({
    url: `/api/repeater/roleurl/batchAddUrls?roleId=${roleId}`,
    method: 'post',
    data: urlIds
  })
}

export const deleteRoleUrlRelation = (roleId: number, urlId: number) => {
  return request({
    url: `/api/repeater/roleurl/deleteRoleUrl?roleId=${roleId}&urlId=${urlId}`,
    method: 'delete'
  })
}
