package com.david.hlp.SpringBootWork.system.mapper;

import com.david.hlp.SpringBootWork.system.entity.auth.AuthUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.springframework.cache.annotation.Cacheable;
import java.util.List;
import com.david.hlp.SpringBootWork.system.entity.user.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT CONCAT('ROLE_', r.role_name) AS authority " +
            "FROM role r WHERE r.id = #{roleId} " +
            "UNION ALL " +
            "SELECT p.permission AS authority " +
            "FROM permission p " +
            "JOIN role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role_id = #{roleId}")
    @Cacheable(value = "userAuthorities", key = "#roleId")
    List<String> findAuthoritiesByRoleId(Long roleId);

    @Select("SELECT id, name, email, password, status, role_id as roleId FROM user WHERE name = #{username} AND status = 1")
    AuthUser findByUsername(String username);

    @Insert("INSERT INTO user (name, email, password, status, role_id) " +
            "VALUES(#{name}, #{email}, #{password}, #{status}, #{roleId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("SELECT id as user_id, name as username, password, status, role_id ,email FROM user WHERE email = #{email} AND status = 1")
    @Results({
        @Result(property = "userId", column = "user_id"),
        @Result(property = "username", column = "username"),
        @Result(property = "password", column = "password"),
        @Result(property = "status", column = "status"),
        @Result(property = "roleId", column = "role_id"),
        @Result(property = "email", column = "email")
    })
    AuthUser findByEmailToAuthUser(String email);

    @Select("SELECT id, name, email, password, status, role_id as roleId FROM user WHERE email = #{email} AND status = 1")
    User findByEmailToUser(String email);

    @Select("SELECT id, name, email, password, status, role_id as roleId FROM user WHERE id = #{userId} AND status = 1")
    User findByUserIdToUser(Long userId);

    @Select("<script>" +
            "SELECT user.id, user.name, user.email, user.status, user.role_id, user.avatar, role.role_name, user.address, user.create_time " +
            "FROM `user` " +
            "LEFT JOIN role ON user.role_id = role.id " +
            "<where> " +
            "<if test='query.status != null'>AND user.status = #{query.status} </if> " +
            "<if test='query.roleId != null'>AND user.role_id = #{query.roleId} </if> " +
            "<if test='query.name != null'>AND user.name LIKE CONCAT('%', #{query.name}, '%') </if> " +
            "</where> " +
            "LIMIT #{pageNum}, #{pageSize}" +
            "</script>")
    List<User> findAllLimit(@Param("pageNum") int pageNum, @Param("pageSize") int pageSize, @Param("query") User query);

    @Select("<script>" +
            "SELECT COUNT(*) " +
            "FROM user " +
            "<where> " +
            "<if test='query.status != null'>AND user.status = #{query.status} </if> " +
            "<if test='query.roleId != null'>AND user.role_id = #{query.roleId} </if> " +
            "<if test='query.name != null'>AND user.name LIKE CONCAT('%', #{query.name}, '%') </if> " +
            "</where>" +
            "</script>")
    Long countAll(@Param("query") User query);

    @Delete("DELETE FROM user WHERE id = #{id}")
    void deleteUser(@Param("id") Long id);

    @Select("SELECT password FROM user WHERE id = #{id}")
    String findPasswordById(@Param("id") Long id);

    @Update("<script>" +
            "UPDATE user " +
            "<set> " +
            "<if test='user.name != null'>name = #{user.name}, </if> " +
            "<if test='user.email != null'>email = #{user.email}, </if> " +
            "<if test='user.password != null'>password = #{user.password}, </if> " +
            "<if test='user.roleId != null'>role_id = #{user.roleId}, </if> " +
            "<if test='user.address != null'>address = #{user.address}, </if> " +
            "<if test='user.introduction != null'>introduction = #{user.introduction}, </if> " +
            "<if test='user.avatar != null'>avatar = #{user.avatar}, </if> " +
            "</set> " +
            "WHERE id = #{user.id}" +
            "</script>")
    void updateUser(@Param("user") User user);
}