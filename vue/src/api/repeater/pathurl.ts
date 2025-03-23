import request from '@/utils/request/request'
import type { ProjectUrl } from '@/api/repeater/projecturl'
import type { RouterUrl } from './routerurl'
import type { PortUrl } from './porturl'
import type { HostUrl } from './hosturl'
import type { PageInfo } from '@/utils/types/common'

export interface GroupedProject {
  projectId: number
  projectName: string
  projectInterfaceName: string
  projectDescription: string | null
  postCount: number
  getCount: number
  putCount: number
  deleteCount: number
  httpCount: number
  httpsCount: number
  urls: Url[]
}

export interface Url {
  id?: number
  protocol: string
  hostId: number | undefined
  portId: number | undefined
  routerId: number | undefined
  projectId: number | undefined
  method: string
  isActive: boolean
  description?: string
  createdAt?: string
  host?: HostUrl
  port?: PortUrl
  router?: RouterUrl
  project?: ProjectUrl
}

export interface QueryParams {
  protocol: string
  method: string
  isActive: boolean | undefined
  hostId?: number
  portId?: number
  routerId?: number
  projectId?: number
  page: number
  limit: number
  sortField?: string
  sortOrder?: 'asc' | 'desc'
}

export const getUrlList = (queryParams: QueryParams):Promise<PageInfo<GroupedProject>> => {
  return request({
    url: '/api/repeater/path-urls',
    method: 'get',
    params: {
      ...queryParams,
      page: queryParams.page || 1,
      limit: queryParams.limit || 10
    }
  })
}

export const getUrlById = (id: number) => {
  return request({
    url: `/api/repeater/path-urls/${id}`,
    method: 'get'
  })
}

export const createUrl = (data: Url) => {
  return request({
    url: '/api/repeater/path-urls',
    method: 'post',
    data
  })
}

export const updateUrl = (id: number, data: Url) => {
  return request({
    url: `/api/repeater/path-urls/${id}`,
    method: 'put',
    data
  })
}

export const deleteUrl = (id: number) => {
  return request({
    url: `/api/repeater/path-urls/${id}`,
    method: 'delete'
  })
}

export const listAllHost = ():Promise<HostUrl[]> => {
  return request({
    url: '/api/repeater/path-urls/listAllHost',
    method: 'get'
  })
}

export const listAllPort = ():Promise<PortUrl[]> => {
  return request({
    url: '/api/repeater/path-urls/listAllPort',
    method: 'get'
  })
}

export const listAllRouter = ():Promise<RouterUrl[]> => {
  return request({
    url: '/api/repeater/path-urls/listAllRouter',
    method: 'get'
  })
}

export const listAllProject = ():Promise<ProjectUrl[]> => {
  return request({
    url: '/api/repeater/path-urls/listAllProject',
    method: 'get'
  })
}

export const getByProjectId = (projectId: number):Promise<Url[]> => {
  return request({
    url: `/api/repeater/path-urls/getByProjectId?projectId=${projectId}`,
    method: 'get'
  })
}

export const listAll = ():Promise<Url[]> => {
  return request({
    url: '/api/repeater/path-urls/listAll',
    method: 'get'
  })
}
