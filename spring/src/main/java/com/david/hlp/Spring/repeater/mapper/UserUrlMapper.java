package com.david.hlp.Spring.repeater.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.david.hlp.Spring.repeater.entity.UserUrl;
import java.util.List;

@Mapper
public interface UserUrlMapper {
    /**
     * 插入用户URL记录
     * @param userUrl 用户URL对象
     * @return 插入后的用户URL对象
     */
    UserUrl insert(UserUrl userUrl);

    /**
     * 根据ID查询用户URL
     * @param id 用户URL ID
     * @return 用户URL对象
     */
    UserUrl getById(Integer id);

    /**
     * 更新用户URL
     * @param userUrl 用户URL对象
     * @return 更新后的用户URL对象
     */
    int update(UserUrl userUrl);

    /**
     * 根据ID删除用户URL
     * @param id 用户URL ID
     * @return 影响的行数
     */
    int deleteById(Integer id);

    /**
     * 根据条件分页查询用户URL列表
     * @param userUrl 查询条件
     * @param offset 偏移量
     * @param limit 每页条数
     * @return 用户URL列表
     */
    List<UserUrl> listByCondition(@Param("entity") UserUrl userUrl, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 根据条件统计用户URL数量
     * @param userUrl 查询条件
     * @return 用户URL数量
     */
    long countByCondition(@Param("entity") UserUrl userUrl);

    /**
     * 查询所有用户URL
     * @return 用户URL列表
     */
    List<UserUrl> listAll();
}