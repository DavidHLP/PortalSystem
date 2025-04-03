package com.david.hlp.Spring.repeater.service.impl;

import org.springframework.stereotype.Service;

import com.david.hlp.Spring.repeater.mapper.UserUrlMapper;
import com.david.hlp.Spring.repeater.mapper.RoleUrlMapper;
import com.david.hlp.Spring.repeater.module.entity.UserRoleProject;
import com.david.hlp.Spring.common.result.PageInfo;
import java.util.List;
import com.david.hlp.Spring.repeater.module.dto.UserRoleProjectDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.david.hlp.Spring.repeater.module.entity.UserRole;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserUrlServiceImpl {
    private final UserUrlMapper userUrlMapper;
    private final RoleUrlMapper roleUrlMapper;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 分页查询用户角色项目关联信息
     *
     * @param pageInfo 分页查询条件
     * @return 分页结果
     */
    public PageInfo<UserRoleProject> listUserRoleProjectByPage(PageInfo<UserRoleProject> pageInfo) {
        Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        // 计算偏移量
        Integer offset = pageSize * (pageNum - 1);

        // 查询总数
        Long total = userUrlMapper.countUserRoleProject(pageInfo.getItem());

        // 查询数据列表
        List<UserRoleProject> userRoleProjectList = userUrlMapper.listUserRoleProject(pageInfo.getItem(), pageSize, offset);

        // 计算总页数
        Integer pages = (int)Math.ceil((double)total / pageSize);

        // 构建返回结果
        return PageInfo.<UserRoleProject>builder()
                .items(userRoleProjectList)
                .item(pageInfo.getItem())
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .pages(pages)
                .build();
    }
    /**
     * 添加用户角色项目关联信息
     *
     * @param userRoleProjectDTO 用户角色项目关联信息
     */
    @Transactional
    public void addUserRoleProject(UserRoleProjectDTO userRoleProjectDTO) {
        userRoleProjectDTO.setPassword(passwordEncoder.encode(userRoleProjectDTO.getPassword()));
        userUrlMapper.insertUserRoleProject(userRoleProjectDTO);
        Long userId = userUrlMapper.selectUserId(userRoleProjectDTO);
        UserRole userRole = UserRole.builder()
                .userId(userId)
                .roleId(userRoleProjectDTO.getRoleId())
                .build();
        roleUrlMapper.insertUserRole(userRole);
    }

    /**
     * 删除用户角色项目关联信息
     *
     * @param userRoleProjectDTO 用户角色项目关联信息
     */
    @Transactional
    public void deleteUserRoleProject(UserRoleProject userRoleProjectDTO) {
        UserRole userRole = UserRole.builder()
            .userId(userRoleProjectDTO.getId())
            .roleId(userRoleProjectDTO.getRoleId())
            .build();
        roleUrlMapper.deleteUserRole(userRole);
        userUrlMapper.deleteUser(userRoleProjectDTO.getId());
    }

    @Transactional
    public void updateUserRoleProject(UserRoleProject userRoleProjectDTO) {
        userUrlMapper.updateUser(userRoleProjectDTO);
        UserRole userRole = UserRole.builder()
            .userId(userRoleProjectDTO.getId())
            .roleId(userRoleProjectDTO.getRoleId())
            .build();
        roleUrlMapper.updateUserRole(userRole);
    }

    @Transactional
    public void updateUserPassword(UserRoleProjectDTO userRoleProjectDTO) {
        userRoleProjectDTO.setPassword(passwordEncoder.encode(userRoleProjectDTO.getPassword()));
        userUrlMapper.updateUserPassword(userRoleProjectDTO);
    }
}
