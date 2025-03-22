import type { HostUrl } from './host_url'
import type { PortUrl } from './porturl'
import type { RouterUrl } from './routerurl'
import type { ProjectUrl } from './projectUrl'

export interface RoleUrl {
  id?: number
  originalId?: number
  roleName: string
  protocol: string
  hostId: number
  host?: HostUrl
  portId: number
  port?: PortUrl
  routerId: number
  router?: RouterUrl
  projectId: number
  project?: ProjectUrl
  method: string
  isActive: boolean
  description?: string
  createdAt?: string
}
