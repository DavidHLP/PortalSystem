export interface RouterUrl {
  id?: number
  host: string
  port: string
  router: string
  protocol: string
  uniqueId: string
  type: string
  doc: string
  gmtCreate?: string
  gmtModified?: string
  isDeleted?: number
  httpMethod?: HttpMethodType
}

export enum HttpMethodType {
  GET = 0,
  POST = 1,
  PUT = 2,
  DELETE = 3,
}