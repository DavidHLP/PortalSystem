package com.david.hlp.Spring.common.result;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageInfo<T> {
    // 分页数据
    private List<T> items;
    // 查询条件
    private T query;
    // 当前页码
    private int pageNum;
    // 每页条数
    private int pageSize;
    // 总条数
    private Long total;
    // 总页数
    private int pages;
}
