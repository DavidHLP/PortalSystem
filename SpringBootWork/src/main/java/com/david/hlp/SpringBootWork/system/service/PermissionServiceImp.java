package com.david.hlp.SpringBootWork.system.service;

import com.david.hlp.SpringBootWork.system.mapper.PermissionMapper;
import com.david.hlp.SpringBootWork.system.mapper.UserMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
@Service
@RequiredArgsConstructor
public class PermissionServiceImp {
    private final PermissionMapper permissionMapper;
    private final UserMapper userMapper;

    public List<String> getUserPrivateInformationByUserId(Long userId) {
        Long roleId = userMapper.findByUserIdToUser(userId).getRoleId();
        return permissionMapper.getPermissionByRoleId(roleId);
    }
}
