package com.david.hlp.Spring.repeater.service.impl;

import com.david.hlp.Spring.repeater.entity.RoleUrl;
import com.david.hlp.Spring.repeater.mapper.RoleUrlMapper;
import com.david.hlp.Spring.repeater.service.RoleUrlService;
import com.david.hlp.Spring.common.result.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import com.david.hlp.Spring.repeater.service.PathUrlService;
import com.david.hlp.Spring.repeater.entity.Url;
import com.david.hlp.Spring.repeater.entity.RoleUrlUrl;
import com.david.hlp.Spring.repeater.mapper.RoleUrlUrlMapper;
/**
 * 角色URL服务实现类
 */
@Service
@RequiredArgsConstructor
public class RoleUrlServiceImpl implements RoleUrlService {

    private final RoleUrlMapper roleUrlMapper;
    private final PathUrlService pathUrlService;
    private final RoleUrlUrlMapper roleUrlUrlMapper;

    @Override
    public RoleUrl create(RoleUrl entity) {
        roleUrlMapper.insert(entity);
        return entity;
    }

    @Override
    public RoleUrl getById(Integer id) {
        return roleUrlMapper.selectById(id);
    }

    @Override
    public List<RoleUrl> listAll() {
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        roleUrlMapper.deleteById(id);
    }

    @Override
    public RoleUrl update(RoleUrl entity) {
        roleUrlMapper.updateById(entity);
        return entity;
    }

    @Override
    public RoleUrl updateByIdAndDescription(RoleUrl entity) {
        roleUrlMapper.updateByIdAndDescription(entity);
        return entity;
    }

    @Override
    public PageInfo<RoleUrl> getPage(Integer pageNum, Integer pageSize, RoleUrl entity) {
         // 计算偏移量
         int offset = (pageNum - 1) * pageSize;
         int limit = pageSize * pageNum;
         // 获取总记录数
         Integer total = roleUrlMapper.getTotalCount(entity);
         // 获取分页数据
         List<RoleUrl> list = roleUrlMapper.listAll(entity, limit, offset);

         // 构建分页结果
         return PageInfo.<RoleUrl>builder()
             .items(list)
             .pageNum(pageNum)
             .pageSize(pageSize)
             .total((long)total)
             .build();
    }

    @Override
    public List<Url> getRoleUrlListByRoleId(Integer roleId) {
        List<Integer> urlIds = roleUrlMapper.getRoleUrlListByRoleId(roleId);
        return pathUrlService.getByListUrlId(urlIds);
    }

    @Override
    public void batchAddUrls(Integer roleId, List<Integer> urlIds) {
        List<RoleUrlUrl> roleUrlUrls = new ArrayList<>();
        for (Integer urlId : urlIds) {
            RoleUrlUrl roleUrlUrl = RoleUrlUrl.builder()
                .roleId(roleId)
                .urlId(urlId)
                .build();
            roleUrlUrls.add(roleUrlUrl);
        }
        roleUrlUrlMapper.batchInsert(roleUrlUrls);
    }

    @Override
    public void deleteRoleUrl(Integer roleId, Integer urlId) {
        roleUrlUrlMapper.deleteByRoleIdAndUrlId(roleId, urlId);
    }
}