import request from '@/utils/request/request'
import type { PageInfo } from '@/utils/types/common'

export interface ProjectUrl {
  id?: number
  projectName: string
  projectInterfaceName: string
  description?: string | null
  createdAt?: string | null
}

/**
 * 分页查询项目URL列表
 */
export const getProjectUrlList = (
  pageNum: number,
  pageSize: number,
  params?: Partial<ProjectUrl>,
): Promise<PageInfo<ProjectUrl>> =>
  request({
    url: `/api/repeater/project-url/list`,
    method: 'POST',
    data: {
      pageNum,
      pageSize,
      query: {
        ...params,
      },
    },
  })

/**
 * 获取项目URL详情
 */
export const getProjectUrl = (id: number): Promise<ProjectUrl> =>
  request({
    url: `/api/repeater/project-url/get`,
    method: 'POST',
    data: {
      id,
    },
  })

/**
 * 新增项目URL
 */
export const createProjectUrl = (data: ProjectUrl): Promise<void> =>
  request({
    url: `/api/repeater/project-url/add`,
    method: 'POST',
    data,
  })

/**
 * 更新项目URL
 */
export const updateProjectUrl = (data: ProjectUrl): Promise<void> =>
  request({
    url: `/api/repeater/project-url/update`,
    method: 'POST',
    data,
  })

/**
 * 删除项目URL
 */
export const deleteProjectUrl = (id: number): Promise<void> =>
  request({
    url: `/api/repeater/project-url/delete`,
    method: 'POST',
    data: {
      id,
    },
  })

/**
 * 获取所有项目URL
 */
export const listAll = (): Promise<ProjectUrl[]> =>
  request({
    url: `/api/repeater/project-url/listAll`,
    method: 'POST',
  })
