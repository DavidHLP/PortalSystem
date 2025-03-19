package com.david.hlp.SpringBootWork.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.david.hlp.SpringBootWork.system.entity.role.Role;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import com.david.hlp.SpringBootWork.system.entity.role.RolePermission;
@Mapper
public interface RoleMapper extends BaseMapper<Role>{
    @Select("SELECT * FROM role WHERE id = #{roleId}")
    Role findRoleByRoleId(Long roleId);

    @Select("SELECT id FROM role WHERE role_name = 'USER' LIMIT 1")
    Long findDefaultRoleId();

    @Select(
    "<script> " +
    "SELECT * FROM role " +
    "<where> " +
        "<if test='roleName != null'> " +
            "AND role_name LIKE '%${roleName}%' " +
        "</if> " +
    "</where> " +
    "</script>")
    List<Role> findAll(@Param("roleName") String roleName);

    /**
     * 删除角色的所有权限
     * @param roleId 角色ID
     */
    @Delete("DELETE FROM role_permission WHERE role_id = #{roleId}")
    void deleteRolePermissions(Long roleId);

    /**
     * 删除角色的单个权限
     * @param roleId 角色ID
     * @param permissionId 权限ID
     */
    @Delete("DELETE FROM role_permission WHERE role_id = #{roleId} AND permission_id = #{permissionId}")
    void deleteRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    /**
     * 批量插入角色权限关系
     * @param roleId 角色ID
     * @param permissions 权限标识列表
     */
    @Insert("<script>INSERT INTO role_permission (role_id, permission_id) VALUES " +
           "<foreach collection='permissions' item='permission' separator=','>" +
           "(#{roleId}, #{permission})" +
           "</foreach>" +
           "</script>")
    void insertRolePermissions(@Param("roleId") Long roleId, @Param("permissions") List<Long> permissions);

    @Insert("INSERT INTO role (role_name, remark , status) VALUES (#{roleName}, #{remark}, #{status})")
    void insertRole(Role role);

    @Update("UPDATE role SET role_name = #{roleName}, remark = #{remark}  , status = #{status} WHERE id = #{id}")
    void updateRole(Role role);

    @Select("SELECT * FROM role_permission WHERE role_id = #{roleId}")
    List<RolePermission> findRolePermissions(Long roleId);

    @Delete("DELETE FROM role WHERE id = #{roleId}")
    void deleteRoleByRoleId(Long roleId);

    @Delete("DELETE FROM role_permission WHERE role_id = #{roleId}")
    void deleteRolePermissionByRoleId(Long roleId);
}
