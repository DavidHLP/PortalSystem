package com.david.hlp.Spring.repeater.service.impl;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.david.hlp.Spring.repeater.entity.UserUrl;
import com.david.hlp.Spring.repeater.mapper.UserUrlMapper;
import com.david.hlp.Spring.repeater.service.UserUrlService;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.common.exception.BusinessException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户URL服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserUrlServiceImpl implements UserUrlService {
    private final UserUrlMapper userUrlMapper;

    @Override
    public UserUrl getById(Integer id) {
        log.info("获取用户URL信息, id: {}", id);
        if (id == null) {
            throw new BusinessException("用户URL ID不能为空");
        }
        return userUrlMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserUrl create(UserUrl entity) {
        log.info("创建用户URL: {}", entity);
        if (entity == null) {
            throw new BusinessException("用户URL信息不能为空");
        }
        userUrlMapper.insert(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserUrl update(UserUrl entity) {
        log.info("更新用户URL: {}", entity);
        if (entity == null) {
            throw new BusinessException("用户URL信息不能为空");
        }
        if (entity.getId() == null) {
            throw new BusinessException("用户URL ID不能为空");
        }
        userUrlMapper.update(entity);
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        log.info("删除用户URL, id: {}", id);
        if (id == null) {
            throw new BusinessException("用户URL ID不能为空");
        }
        userUrlMapper.deleteById(id);
    }

    @Override
    public PageInfo<UserUrl> getPage(Integer page, Integer limit, UserUrl entity) {
        log.info("分页查询用户URL, page: {}, limit: {}", page, limit);
        if (page == null || page < 1) {
            throw new BusinessException("页码必须大于等于1");
        }
        if (limit == null || limit < 1) {
            throw new BusinessException("每页大小必须大于等于1");
        }

        int offset = (page - 1) * limit;
        limit = page * limit;
        List<UserUrl> userUrls = userUrlMapper.listByCondition(entity, offset, limit);
        long total = userUrlMapper.countByCondition(entity);

        int pages = (int) Math.ceil((double) total / limit);

        return PageInfo.<UserUrl>builder()
            .items(userUrls)
            .query(entity)
            .pageNum(page)
            .pageSize(limit)
            .total(total)
            .pages(pages)
            .hasNext(page < pages)
            .hasPrevious(page > 1)
            .isFirst(page == 1)
            .isLast(page >= pages)
            .build();
    }

    @Override
    public List<UserUrl> listAll() {
        log.info("获取所有用户URL列表");
        return userUrlMapper.listAll();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserUrl save(UserUrl userUrl) {
        log.info("保存用户URL: {}", userUrl);
        if (userUrl == null) {
            throw new BusinessException("用户URL信息不能为空");
        }
        userUrlMapper.insert(userUrl);
        return userUrl;
    }
}
