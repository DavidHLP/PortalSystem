package com.david.hlp.SpringBootWork.system.entity.router;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Meta implements Serializable {
    private String type;
    private String component;     // 组件路径
    private String redirect;      // 重定向路径
    private Boolean alwaysShow;   // 是否总是显示
    private String metaTitle;     // 元标题
    private String metaIcon;      // 元图标
    private Boolean metaHidden;   // 是否在菜单中隐藏
    private String metaRoles;     // 存储角色名称数组的 JSON
    private Boolean metaKeepAlive;// 是否缓存页面
    private Boolean hidden;       // 是否整体隐藏
}
