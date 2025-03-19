import request from '@/utils/request/request'

export interface HostUrl {
  id?: number | null
  address: string
  description: string
  isActive: boolean
  createdAt?: string
}

export interface QueryParams {
  page: number
  limit: number
  address?: string
  isActive?: boolean | null
}

export interface HostListResponse {
  items: HostUrl[]
  total: number
}

/**
 * 获取主机列表
 * @param params 查询参数
 */
export function getHostList(params: QueryParams): Promise<HostListResponse> {
  return request({
    url: '/api/repeater/host-url/list',
    method: 'get',
    params
  })
}

/**
 * 获取单个主机信息
 * @param id 主机ID
 */
export function getHostById(id: number): Promise<HostUrl> {
  return request({
    url: `/api/repeater/host-url/${id}`,
    method: 'get'
  })
}

/**
 * 创建新主机
 * @param data 主机信息
 */
export function createHost(data: HostUrl): Promise<HostUrl> {
  return request({
    url: '/api/repeater/host-url',
    method: 'post',
    data
  })
}

/**
 * 更新主机信息
 * @param id 主机ID
 * @param data 主机信息
 */
export function updateHost(id: number, data: HostUrl): Promise<null> {
  return request({
    url: `/api/repeater/host-url/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除主机
 * @param id 主机ID
 */
export function deleteHost(id: number): Promise<null> {
  return request({
    url: `/api/repeater/host-url/${id}`,
    method: 'delete'
  })
}

/**
 * 更新主机状态
 * @param id 主机ID
 * @param isActive 状态
 */
export function updateHostStatus(id: number, isActive: boolean): Promise<null> {
  return request({
    url: `/api/repeater/host-url/${id}/status`,
    method: 'patch',
    params: { isActive }
  })
}
