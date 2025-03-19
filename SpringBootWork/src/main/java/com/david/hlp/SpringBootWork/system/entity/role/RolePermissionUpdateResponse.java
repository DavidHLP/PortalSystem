package com.david.hlp.SpringBootWork.system.entity.role;

import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RolePermissionUpdateResponse {
    private Long roleId;
    private List<Integer> routerIds;
}
