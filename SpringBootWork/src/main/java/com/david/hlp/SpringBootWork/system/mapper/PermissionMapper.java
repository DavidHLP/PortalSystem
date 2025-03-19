package com.david.hlp.SpringBootWork.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.david.hlp.SpringBootWork.system.entity.permission.Permission;
import org.apache.ibatis.annotations.Delete;
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    @Select("SELECT permission FROM router WHERE role_id = #{roleId} AND status = 1 AND permission IS NOT NULL")
    List<String> getPermissionByRoleId(Long roleId);

    @Select("SELECT CASE WHEN COUNT(1) > 0 THEN true ELSE false END FROM permission WHERE permission = #{permission}")
    boolean checkPermission(String permission);

    @Insert("INSERT INTO permission (permission) VALUES (#{permission})")
    void insertPermission(String permission);

    @Delete("DELETE FROM permission WHERE permission = #{permission}")
    void deletePermission(String permission);

    @Select("SELECT * FROM permission WHERE id IN (SELECT permission_id FROM role_permission WHERE role_id = #{roleId})")
    List<Permission> findAllByRoleId(Long roleId);

    @Select("SELECT id FROM permission WHERE permission = #{permission}")
    Long findPermissionIdByName(String permission);
}
