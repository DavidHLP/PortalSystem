import request from '@/utils/request/request'
import type { PageInfo } from '@/utils/types/common'

export interface ProjectUrl {
  id?: number
  projectName: string
  projectInterfaceName: string
  description?: string | null
}

export const getProjectUrlList = (
  pageNum: number,
  pageSize: number,
  params?: Partial<ProjectUrl>,
): Promise<PageInfo<ProjectUrl>> =>
  request({
    url: `/api/repeater/project-url/findAll`,
    method: 'POST',
    data: {
      pageNum,
      pageSize,
      query: {
        ...params,
      },
    },
  })

export const createProjectUrl = (data: ProjectUrl) =>
  request({
    url: `/api/repeater/project-url/add`,
    method: 'POST',
    data,
  })

export const updateProjectUrl = (data: ProjectUrl) =>
  request({
    url: `/api/repeater/project-url/update`,
    method: 'POST',
    data,
  })

export const deleteProjectUrl = (id: number) =>
  request({
    url: `/api/repeater/project-url/delete`,
    method: 'POST',
    data: {
      id: id,
    },
  })
