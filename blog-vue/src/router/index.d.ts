export interface BackendRoute {
  component: string
  id: number
  menuName: string
  menuType: string
  orderNum: number
  path: string
  permission: string
  pid: number
  remark: string
  status: number
  requiresAuth?: boolean
  permissions?: string[]
  children?: BackendRoute[]
}

// 新架构接口

export interface Meta {
  lwaysShow: boolean;
  component: string;
  hidden: boolean;
  metaHidden: boolean;
  metaIcon: null;
  metaKeepAlive: boolean;
  metaRoles: null;
  metaTitle: string;
  redirect: null;
  type: string | 'M' | 'C' | 'F';
  [property: string];
}

export interface Router {
  children: Router[];
  icon: string;
  id: number;
  menuOrder: number;
  meta: Meta;
  name: string;
  path: string;
  permission: string;
  pid: null;
  remark: string;
  status: number;
  [property: string];
}
