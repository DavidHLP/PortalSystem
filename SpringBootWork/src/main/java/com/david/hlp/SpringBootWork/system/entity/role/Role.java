package com.david.hlp.SpringBootWork.system.entity.role;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import com.david.hlp.SpringBootWork.system.entity.permission.Permission;
import com.david.hlp.SpringBootWork.system.entity.router.Router;
/**
 * 对应数据库中的 role 表
 */
@Data
public class Role {
    private Long id;         // BIGINT 主键
    private Long userId;     // BIGINT 用户ID
    private String roleName; // 角色名称
    private Integer status;  // 状态
    private String remark;   // 备注信息
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private List<Permission> permissions; // 权限列表
    private List<Router> routers; // 路由列表
}