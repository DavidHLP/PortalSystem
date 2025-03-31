/**
 * 分页信息包装类
 */
export interface PageInfo<T> {
  /**
   * 分页数据列表
   */
  items?: T[]

  /**
   * 查询条件对象
   */
  item?: T

  /**
   * 当前页码，从1开始
   */
  pageNum?: number

  /**
   * 每页显示条数
   */
  pageSize?: number

  /**
   * 总记录数
   */
  total?: number

  /**
   * 总页数
   */
  pages?: number
}
