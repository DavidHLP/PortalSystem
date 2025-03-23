import request from '@/utils/request/request';
import type { PageInfo } from '@/utils/types/common';

export interface UserUrl {
  id?: number;
  username: string;
  passwordHash?: string;
  email?: string;
  roleId?: number;
  projectId?: number;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

/**
 * 获取所有用户URL
 */
export const listAllUserUrls = () => {
  return request({
    url: '/api/repeater/userUrl',
    method: 'get',
  });
};

/**
 * 根据ID获取用户URL
 * @param id 用户URL ID
 */
export const getUserUrlById = (id: number) => {
  return request({
    url: `/api/repeater/userUrl/${id}`,
    method: 'get',
  });
};

/**
 * 创建用户URL
 * @param data 用户URL数据
 */
export const createUserUrl = (data: UserUrl) => {
  return request({
    url: '/api/repeater/userUrl',
    method: 'post',
    data,
  });
};

/**
 * 更新用户URL
 * @param id 用户URL ID
 * @param data 用户URL数据
 */
export const updateUserUrl = (id: number, data: UserUrl) => {
  return request({
    url: `/api/repeater/userUrl/${id}`,
    method: 'put',
    data,
  });
};

/**
 * 删除用户URL
 * @param id 用户URL ID
 */
export const deleteUserUrl = (id: number) => {
  return request({
    url: `/api/repeater/userUrl/${id}`,
    method: 'delete',
  });
};

/**
 * 分页获取用户URL列表
 * @param params 查询参数
 */
export const getUserUrls = (params: {
  page: number;
  limit: number;
  username?: string;
  email?: string;
  roleId?: number;
  projectId?: number;
  isActive?: boolean;
}) => {
  return request({
    url: '/api/repeater/userUrl/page',
    method: 'get',
    params,
  }) as Promise<PageInfo<UserUrl>>;
};