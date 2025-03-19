package com.david.hlp.SpringBootWork.system.entity.permission;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    private Long id;            // 对应 BIGINT 主键
    private Integer status;     // TINYINT(1) 状态字段
    private String remark;      // 备注信息
    private String permission;  // 权限标识
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}