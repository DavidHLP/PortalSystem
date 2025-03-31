import request from '@/utils/request/request'
import type { RolePath } from '@/types/repeater/rolepath'
import type { PageInfo } from '@/utils/types/common'

export const getRolePathPage = (params: {
  page: number
  limit: number
  roleId: number
}) => {
  return request.get<PageInfo<RolePath>>('/api/repeater/rolepath/page', { params })
}

export const getRolePathById = (id: number) => {
  return request.get<RolePath>(`/api/repeater/rolepath/${id}`)
}

export const createRolePath = (data: RolePath) => {
  return request.post<RolePath>('/api/repeater/rolepath', data)
}

export const updateRolePath = (data: RolePath) => {
  return request.put<RolePath>('/api/repeater/rolepath', data)
}

export const deleteRolePath = (id: number) => {
  return request.delete(`/api/repeater/rolepath/${id}`)
}

export const getPathListByProjectId = (projectId: number) => {
  return request.get<RolePath[]>('/api/repeater/path/list', { params: { projectId } })
}
