package com.david.hlp.SpringBootWork.system.service;

import com.david.hlp.SpringBootWork.system.entity.user.User;
import com.david.hlp.SpringBootWork.common.result.PageInfo;
import com.david.hlp.SpringBootWork.system.entity.user.DelUser;
import com.david.hlp.SpringBootWork.system.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp {
    private final UserMapper userMapper;

    public User getUserBaseInfo(Long userId) {
        User user = userMapper.findByUserIdToUser(userId);
        User res = User.builder()
                .id(user.getUserId())
                .name(user.getUsername())
                .email(user.getEmail())
                .build();
        return res;
    }

    public PageInfo<User> getUserManageInfo(int pageNum, int pageSize, User query){
        List<User> users = userMapper.findAllLimit(pageNum-1, pageSize, query);
        Long total = userMapper.countAll(query); // 获取总记录数
        PageInfo<User> pageInfo = PageInfo.<User>builder()
                .items(users)
                .query(query)
                .pageNum(pageNum)
                .pageSize(pageSize)
                .total(total)
                .pages((int)Math.ceil((double)total / pageSize))
                .build();
        return pageInfo;
    }

    public void deleteUser(DelUser user) {
        userMapper.deleteUser(user.getId());
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }
}
