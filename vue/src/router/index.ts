import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user/userStore'
import { useRouterStore } from '@/stores/router/routerStore'
import type { Router } from '@/router/index.d'

// 添加token过期时间检查
const TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000 // 24小时
const TOKEN_CHECK_INTERVAL = 5 * 60 * 1000 // 5分钟检查一次

// 基础路由（无需权限）
const baseRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'home',
    meta: { type: 'M' },
    component: () => import('@/views/front/home/HomeView.vue'),
  },
  {
    path: '/login',
    name: 'login',
    meta: { type: 'M' },
    component: () => import('@/views/front/login/LoginView.vue'),
  },
  {
    path: '/about',
    name: 'about',
    meta: { type: 'M' },
    component: () => import('@/views/front/about/AboutView.vue'),
  }
]

const Module = import.meta.glob('@/views/**/*.vue')
function lazyLoadRoute(view: string) {
  return Module[`/src/views/${view}.vue`]
}
// 修改路由转换方法以匹配后端数据结构
export function transformRoutes(backendRoutes: Router[]): RouteRecordRaw[] {
  const data: RouteRecordRaw[] = []
  backendRoutes.forEach((route) => {
    const path = route.path || ''
    if (!route.meta.component && route.meta.type !== 'M') return

    data.push({
      path,
      name: route.name,
      component: lazyLoadRoute(route.meta.component),
      redirect: route.redirect || undefined,
      meta: {
        ...route.meta,
      },
      children: route.children && route.children.length > 0
        ? transformRoutes(route.children)
        : undefined,
    })
  })
  return data
}

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: baseRoutes,
})

// 动态添加路由
export const addRoutes = (routes: RouteRecordRaw[]) => {
  routes.forEach((route) => {
    if (!router.hasRoute(route.name!)) {
      router.addRoute(route)
    }
  })
}

// 更新setupAsyncRoutes方法以处理新的路由格式
export const setupAsyncRoutes = async () => {
  const routerStore = useRouterStore()
  try {
    const dynamicRoutes = await routerStore.fetchRoutes()
    const userStore = useUserStore()
    // 将permissions转换为数组格式
    const permissionsArray = Array.isArray(userStore.permissions)
      ? userStore.permissions
      : Object.values(userStore.permissions || {})

    // 根据用户权限过滤路由
    const filteredRoutes = filterRoutes(dynamicRoutes, permissionsArray)

    // 添加路由
    addRoutes(filteredRoutes)
    return filteredRoutes
  } catch (error) {
    console.error('路由初始化失败', error)
    return []
  }
}

export const refreshUserBaseInfo = async (): Promise<boolean> => {
  const userStore = useUserStore()
  await userStore.getUserPermissions()
  return true
}

// 为路由添加刷新权限的功能
export const refreshPermissions = async (): Promise<boolean> => {
  try {
    const userStore = useUserStore()
    await userStore.getUserPermissions()
    return true
  } catch (error) {
    console.error('刷新权限失败', error)
    return false
  }
}

// 检查token是否过期
export const checkTokenValidity = (): boolean => {
  const token = localStorage.getItem('token')
  if (!token) return false

  const tokenTimestamp = localStorage.getItem('token_timestamp')
  if (!tokenTimestamp) return false

  const timestamp = parseInt(tokenTimestamp)
  const now = new Date().getTime()

  // 如果token超过有效期，则视为失效
  if (now - timestamp > TOKEN_EXPIRE_TIME) {
    localStorage.removeItem('token')
    localStorage.removeItem('token_timestamp')
    return false
  }

  return true
}

// 完整的路由守卫
router.beforeEach(async (to, from, next) => {
  // 1. 检查是否为公开路径
  const publicPaths = ['/login', '/register'];
  const isPublicPath = publicPaths.includes(to.path);

  // 2. 获取token
  const token = localStorage.getItem('token');

  // 3. 如果已有token且尝试访问登录页，重定向到首页
  if (to.path === '/login' && token) {
    next('/');
    return;
  }

  // 4. 如果不是公开路径且没有token，重定向到登录页
  if (!isPublicPath && !token) {
    next(`/login?redirect=${encodeURIComponent(to.fullPath)}`);
    return;
  }

  // 5. 如果需要权限检查，走原有的权限检查逻辑
  if (to.meta.requiresAuth) {
    const userStore = useUserStore()
    await refreshUserBaseInfo()

    // 检查token是否有效
    if (!checkTokenValidity()) {
      next(`/login?redirect=${encodeURIComponent(to.fullPath)}&expired=true`)
      return
    }

    try {
      // 将permissions转换为数组格式
      const permissionsArray = Array.isArray(userStore.permissions)
        ? userStore.permissions
        : Object.values(userStore.permissions || {})

      // 如果权限列表为空，获取用户权限并加载动态路由
      if (!permissionsArray.length) {
        await userStore.getUserPermissions()
        await setupAsyncRoutes()

        // 如果当前路由不存在于路由表中，重定向到当前路由（触发路由重新匹配）
        next({ ...to, replace: true })
        return
      }

      // 验证当前路由权限
      if (
        to.meta.permissions &&
        !hasPermission(permissionsArray, to.meta.permissions as string[])
      ) {
        next('/403')
        return
      }
    } catch (error) {
      // 发生错误，清除token并跳转到登录页
      localStorage.removeItem('token')
      localStorage.removeItem('token_timestamp')
      next(`/login?redirect=${encodeURIComponent(to.fullPath)}`)
      console.error('路由守卫错误', error)
      return
    }
  }

  // 6. 其它情况放行
  next();
})

// 启动定期检查token有效性和权限
export const startPermissionMonitor = () => {
  // 定期检查token和权限
  setInterval(async () => {
    if (!checkTokenValidity()) {
      // Token失效，触发未授权事件
      window.dispatchEvent(new CustomEvent('unauthorized'))
      return
    }

    // 每隔一段时间刷新权限
    const userStore = useUserStore()
    const permissionsArray = Object.values(userStore.permissions || {})

    if (permissionsArray.length > 0) {
      // 静默刷新权限
      await refreshPermissions()
    }
  }, TOKEN_CHECK_INTERVAL)
}

// 更新路由过滤方法，支持递归处理子路由
function filterRoutes(routes: RouteRecordRaw[], permissions: string[]): RouteRecordRaw[] {
  return routes.filter((route) => {
    let hasAuth = true

    // 检查当前路由是否需要权限验证
    if (route.meta?.permissions) {
      hasAuth = hasPermission(permissions, route.meta.permissions as string[])
    }

    // 如果当前路由有权限访问且有子路由，递归过滤子路由
    if (hasAuth && route.children && route.children.length) {
      route.children = filterRoutes(route.children, permissions)
    }

    return hasAuth
  })
}

// 更新权限验证方法，支持多种权限验证模式
function hasPermission(userPermissions: string[], requiredPermissions: string[]): boolean {
  if (!requiredPermissions || requiredPermissions.length === 0) return true
  if (!userPermissions || userPermissions.length === 0) return false

  // 只要有一个权限符合即可访问
  return requiredPermissions.some((permission) => userPermissions.includes(permission))
}

export default router
