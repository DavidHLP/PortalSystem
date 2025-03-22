import request from '@/utils/request/request'

export interface RouterUrl {
  id: number
  path: string
  createdAt: string
}

export interface PageParams {
  page: number
  limit: number
  path?: string
}

export interface PageResult<T> {
  items: T[]
  pageNum: number
  pageSize: number
  total: number
}

// 获取路由URL列表(分页)
export const getRouterUrls = (params: PageParams) => {
  return request<PageResult<RouterUrl>>({
    url: '/api/repeater/router-urls',
    method: 'get',
    params
  })
}

// 添加路由URL
export const addRouterUrl = (data: { path: string }) => {
  return request<RouterUrl>({
    url: '/api/repeater/router-urls',
    method: 'post',
    data
  })
}

// 更新路由URL
export const updateRouterUrl = (id: number, data: { path: string }) => {
  return request<RouterUrl>({
    url: `/api/repeater/router-urls/${id}`,
    method: 'put',
    data
  })
}

// 删除路由URL
export const deleteRouterUrl = (id: number) => {
  return request<void>({
    url: `/api/repeater/router-urls/${id}`,
    method: 'delete'
  })
}
