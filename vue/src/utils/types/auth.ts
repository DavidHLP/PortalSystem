export interface Request<T> {
  code: number
  data: T
  message: string
  timestamp: number
  [property: string]: unknown
}

export interface Result<T> {
  code: number
  message: string
  data: T
}

export interface PageInfo<T> {
  pageNum: number
  pageSize: number
  total: number
  pages: number
  items?: T[]
  item?: T
}
