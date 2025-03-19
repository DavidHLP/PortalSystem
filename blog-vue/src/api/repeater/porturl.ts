import request from '@/utils/request/request'

export interface PortUrl {
  id?: number | null
  number: string
  description: string
  createdAt?: string
}

export interface QueryParams {
  page: number
  limit: number
  number?: string
}

export interface PortListResponse {
  items: PortUrl[]
  total: number
}

/**
 * 获取端口列表
 * @param params 查询参数
 */
export function getPortList(params: QueryParams): Promise<PortListResponse> {
  return request({
    url: '/api/repeater/port-url/list',
    method: 'get',
    params
  })
}

/**
 * 获取单个端口信息
 * @param id 端口ID
 */
export function getPortById(id: number): Promise<PortUrl> {
  return request({
    url: `/api/repeater/port-url/${id}`,
    method: 'get'
  })
}

/**
 * 创建新端口
 * @param data 端口信息
 */
export function createPort(data: PortUrl): Promise<PortUrl> {
  return request({
    url: '/api/repeater/port-url',
    method: 'post',
    data
  })
}

/**
 * 更新端口信息
 * @param id 端口ID
 * @param data 端口信息
 */
export function updatePort(id: number, data: PortUrl): Promise<null> {
  return request({
    url: `/api/repeater/port-url/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除端口
 * @param id 端口ID
 */
export function deletePort(id: number): Promise<null> {
  return request({
    url: `/api/repeater/port-url/${id}`,
    method: 'delete'
  })
}
