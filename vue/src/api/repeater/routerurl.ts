import request from '@/utils/request/request'
import type { RouterUrl, RouterProjectDTO } from '@/types/repeater/routerurl'
import type { PageInfo } from '@/types/common'

// 添加路由URL
export function addRouterUrl(data: RouterUrl): Promise<void> {
  return request({
    url: '/api/routerUrl/add',
    method: 'post',
    data
  })
}

// 更新路由URL
export function updateRouterUrl(data: RouterProjectDTO): Promise<void> {
  return request({
    url: '/api/routerUrl/update',
    method: 'post',
    data
  })
}

// 删除路由URL
export function deleteRouterUrl(data: RouterUrl): Promise<void> {
  return request({
    url: '/api/routerUrl/delete',
    method: 'post',
    data
  })
}

// 分页查询路由URL
export function listRouterUrl(data: PageInfo<RouterUrl>): Promise<PageInfo<RouterUrl>> {
  return request({
    url: '/api/routerUrl/page',
    method: 'post',
    data
  })
}

// 查询所有路由URL
export function listAllRouterUrl(): Promise<RouterUrl[]> {
  return request({
    url: '/api/routerUrl/list',
    method: 'get'
  })
}

export function listRouterUrlByProjectId(id: number): Promise<RouterUrl[]> {
  return request({
    url: '/api/routerUrl/listByProjectId',
    method: 'get',
    params: { id: id }
  })
}
