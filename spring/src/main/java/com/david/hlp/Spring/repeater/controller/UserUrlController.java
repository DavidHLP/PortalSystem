package com.david.hlp.Spring.repeater.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.david.hlp.Spring.repeater.entity.UserUrl;
import com.david.hlp.Spring.repeater.entity.TokenUrl;
import com.david.hlp.Spring.repeater.service.UserUrlService;
import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.common.result.PageInfo;
import com.david.hlp.Spring.system.service.AuthService;
import com.david.hlp.Spring.system.entity.auth.LoginDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;


@Slf4j
@RestController
@RequestMapping("/api/repeater/userUrl")
@RequiredArgsConstructor
public class UserUrlController {
    private final UserUrlService userUrlService;
    @Qualifier("repeaterAuthServiceImp")
    private final AuthService<LoginDTO,UserUrl,TokenUrl> authService;

    /**
     * 获取所有用户URL
     *
     * @return 用户URL列表
     */
    @GetMapping
    public Result<List<UserUrl>> listAll() {
        log.info("获取所有用户URL");
        List<UserUrl> userUrls = userUrlService.listAll();
        return Result.success(userUrls);
    }

    /**
     * 根据ID获取用户URL
     *
     * @param id 用户URL ID
     * @return 用户URL信息
     */
    @GetMapping("/{id}")
    public Result<UserUrl> getById(@PathVariable Integer id) {
        log.info("获取用户URL, id: {}", id);
        UserUrl userUrl = userUrlService.getById(id);
        return Result.success(userUrl);
    }

    /**
     * 创建用户URL
     *
     * @param userUrl 用户URL对象
     * @return 创建后的用户URL信息
     */
    @PostMapping
    public Result<UserUrl> create(@RequestBody UserUrl userUrl) {
        log.info("创建用户URL: {}", userUrl);
        authService.addUser(userUrl);
        return Result.success("创建成功");
    }

    /**
     * 更新用户URL
     *
     * @param id 用户URL ID
     * @param userUrl 用户URL对象
     * @return 更新后的用户URL信息
     */
    @PutMapping("/{id}")
    public Result<UserUrl> update(@PathVariable Integer id, @RequestBody UserUrl userUrl) {
        log.info("更新用户URL, id: {}, userUrl: {}", id, userUrl);
        userUrl.setId(id);
        UserUrl updatedUserUrl = userUrlService.update(userUrl);
        return Result.success(updatedUserUrl);
    }

    /**
     * 删除用户URL
     *
     * @param id 用户URL ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        log.info("删除用户URL, id: {}", id);
        userUrlService.deleteById(id);
        return Result.success("删除成功");
    }

    /**
     * 分页查询用户URL
     *
     * @param page 页码
     * @param limit 每页大小
     * @param userUrl 查询条件
     * @return 分页查询结果
     */
    @GetMapping("/page")
    public Result<PageInfo<UserUrl>> getPage(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            UserUrl userUrl) {
        log.info("分页查询用户URL, page: {}, limit: {}, condition: {}", page, limit, userUrl);
        PageInfo<UserUrl> pageInfo = userUrlService.getPage(page, limit, userUrl);
        return Result.success(pageInfo);
    }
}
