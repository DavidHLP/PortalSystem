package com.david.hlp.SpringBootWork.system.entity.router;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;
import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Router implements Serializable {
    private Long id;            // BIGINT 主键
    private Long pid;           // 父路由 ID
    private Integer menuOrder;  // 菜单顺序
    private Integer status;     // 状态字段
    private String remark;      // 备注信息
    private String permission;  // 关联的权限标识
    private String path;        // 路由路径
    private String name;        // 路由名称
    private String icon;        // 图标
    /**
     * 原表中 type 是 ENUM('C','M','F')，可用字符串或自定义枚举类型
     * 若想用字符串对应数据库的 'C','M','F'，可这样声明：
     */
    private Meta meta;
    @Default
    private List<Router> children = new ArrayList<>(); // 子路由
    // 如需记录创建/更新时间，可在表结构或实体里继续添加
    // private LocalDateTime createTime;
    // private LocalDateTime updateTime;
}