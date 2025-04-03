import request from '@/utils/request/request'
import type { PageInfo } from '@/utils/types/auth'

export interface UserRoleProject {
  id?: number;
  username?: string;
  email?: string;
  status?: number;
  roleName?: string;
  roleId?: number;
  projectId?: number;
  projectName?: string;
  projectDoc?: string;
  roleDoc?: string;
}

interface UserRoleProjectDTO extends UserRoleProject {
  password?: string;
}

/**
 * 分页查询用户角色项目关联信息
 * @param pageInfo 分页查询条件
 */
export function listUserRoleProjectByPage(pageInfo: PageInfo<UserRoleProject>): Promise<PageInfo<UserRoleProject>> {
  return request({
    url: '/api/userUrl/listUserRoleProjectByPage',
    method: 'post',
    data: pageInfo
  })
}

/**
 * 添加用户角色项目关联信息
 * @param userRoleProject 用户角色项目关联信息
 */
export function addUserRoleProject(userRoleProject: UserRoleProjectDTO): Promise<void> {
  return request({
    url: '/api/userUrl/addUserRoleProject',
    method: 'post',
    data: userRoleProject
  })
}

/**
 * 删除用户角色项目关联信息
 * @param userRoleProject 用户角色项目关联信息
 */
export function deleteUserRoleProject(userRoleProject: UserRoleProjectDTO): Promise<void> {
  return request({
    url: '/api/userUrl/deleteUserRoleProject',
    method: 'post',
    data: userRoleProject
  })
}

/**
 * 更新用户角色项目关联信息
 * @param userRoleProject 用户角色项目关联信息
 */
export function updateUserRoleProject(userRoleProject: UserRoleProjectDTO): Promise<void> {
  return request({
    url: '/api/userUrl/updateUserRoleProject',
    method: 'post',
    data: userRoleProject
  })
}

/**
 * 更新用户密码
 * @param userRoleProject 用户角色项目关联信息
 */
export function updateUserPassword(userRoleProject: UserRoleProjectDTO): Promise<void> {
  return request({
    url: '/api/userUrl/updateUserPassword',
    method: 'post',
    data: userRoleProject
  })
}
