package com.david.hlp.Spring.repeater.module.entity;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouterRole {
    private Long id;
    private Long routerId;
    private Long roleId;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RouterRole that = (RouterRole) obj;
        return routerId.equals(that.routerId) && roleId.equals(that.roleId);
    }

    @Override
    public int hashCode() {
        int result = routerId != null ? routerId.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        return result;
    }
}