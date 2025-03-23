package com.david.hlp.Spring.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.david.hlp.Spring.system.entity.role.Role;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;
import com.david.hlp.Spring.system.entity.role.RolePermission;

/**
 * 角色数据访问层
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色ID获取角色信息
     * @param roleId 角色ID
     * @return 角色信息
     */
    @Select("SELECT id, role_name, remark, status FROM role WHERE id = #{roleId}")
    Role getRoleById(@Param("roleId") Long roleId);

    /**
     * 获取默认用户角色ID
     * @return 默认用户角色ID
     */
    @Select("SELECT id FROM role WHERE role_name = 'USER' LIMIT 1")
    Long getDefaultRoleId();

    /**
     * 根据条件查询所有角色
     * @param roleName 角色名称
     * @return 角色列表
     */
    @Select(
    "<script> " +
    "SELECT id, role_name, remark, status FROM role " +
    "<where> " +
        "<if test='roleName != null and roleName != \"\"'> " +
            "AND role_name LIKE CONCAT('%', #{roleName}, '%') " +
        "</if> " +
    "</where> " +
    "</script>")
    List<Role> listRoles(@Param("roleName") String roleName);

    /**
     * 删除角色的所有权限
     * @param roleId 角色ID
     */
    @Delete("DELETE FROM role_permission WHERE role_id = #{roleId}")
    void deleteRolePermissions(@Param("roleId") Long roleId);

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
     * @param permissions 权限ID列表
     */
    @Insert("<script>INSERT INTO role_permission (role_id, permission_id) VALUES " +
           "<foreach collection='permissions' item='permission' separator=','>" +
           "(#{roleId}, #{permission})" +
           "</foreach>" +
           "</script>")
    void insertRolePermissions(@Param("roleId") Long roleId, @Param("permissions") List<Long> permissions);

    /**
     * 新增角色
     * @param role 角色信息
     */
    @Insert("INSERT INTO role (role_name, remark, status) VALUES (#{roleName}, #{remark}, #{status})")
    void insertRole(Role role);

    /**
     * 更新角色信息
     * @param role 角色信息
     */
    @Update("UPDATE role SET role_name = #{roleName}, remark = #{remark}, status = #{status} WHERE id = #{id}")
    void updateRole(Role role);

    /**
     * 获取角色的所有权限
     * @param roleId 角色ID
     * @return 角色权限列表
     */
    @Select("SELECT role_id, permission_id FROM role_permission WHERE role_id = #{roleId}")
    List<RolePermission> listRolePermissions(@Param("roleId") Long roleId);

    /**
     * 根据角色ID删除角色
     * @param roleId 角色ID
     */
    @Delete("DELETE FROM role WHERE id = #{roleId}")
    void deleteRole(@Param("roleId") Long roleId);

    /**
     * 根据角色ID删除角色权限关系
     * @param roleId 角色ID
     */
    @Delete("DELETE FROM role_permission WHERE role_id = #{roleId}")
    void deleteRolePermissionByRoleId(@Param("roleId") Long roleId);
}
