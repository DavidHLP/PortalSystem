import request from '@/utils/request/request'

export interface RouterUrl {
  id: number
  path: string
  createdAt: string
}

// 获取所有路由URL
export const getRouterUrls = () => {
  return request({
    url: '/api/repeater/router-urls',
    method: 'get'
  })
}

// 添加路由URL
export const addRouterUrl = (data: { path: string }) => {
  return request({
    url: '/api/repeater/router-urls',
    method: 'post',
    data
  })
}

// 更新路由URL
export const updateRouterUrl = (id: number, data: { path: string }) => {
  return request({
    url: `/api/repeater/router-urls/${id}`,
    method: 'put',
    data
  })
}

// 删除路由URL
export const deleteRouterUrl = (id: number) => {
  return request({
    url: `/api/repeater/router-urls/${id}`,
    method: 'delete'
  })
}
