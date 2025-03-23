package com.david.hlp.Spring.repeater.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.david.hlp.Spring.repeater.entity.UserUrl;
import com.david.hlp.Spring.repeater.service.UserUrlService;
import com.david.hlp.Spring.common.result.Result;
import com.david.hlp.Spring.common.result.PageInfo;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/repeater/userUrl")
@RequiredArgsConstructor
public class UserUrlController {
    private final UserUrlService userUrlService;

    /**
     * 获取所有用户URL
     *
     * @return 用户URL列表
     */
    @GetMapping
    public ResponseEntity<Result<List<UserUrl>>> listAll() {
        log.info("获取所有用户URL");
        List<UserUrl> userUrls = userUrlService.listAll();
        return ResponseEntity.ok(Result.success(userUrls));
    }

    /**
     * 根据ID获取用户URL
     *
     * @param id 用户URL ID
     * @return 用户URL信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Result<UserUrl>> getById(@PathVariable Integer id) {
        log.info("获取用户URL, id: {}", id);
        UserUrl userUrl = userUrlService.getById(id);
        return ResponseEntity.ok(Result.success(userUrl));
    }

    /**
     * 创建用户URL
     *
     * @param userUrl 用户URL对象
     * @return 创建后的用户URL信息
     */
    @PostMapping
    public ResponseEntity<Result<UserUrl>> create(@RequestBody UserUrl userUrl) {
        log.info("创建用户URL: {}", userUrl);
        UserUrl createdUserUrl = userUrlService.create(userUrl);
        return ResponseEntity.status(HttpStatus.CREATED).body(Result.success(createdUserUrl));
    }

    /**
     * 更新用户URL
     *
     * @param id 用户URL ID
     * @param userUrl 用户URL对象
     * @return 更新后的用户URL信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<Result<UserUrl>> update(@PathVariable Integer id, @RequestBody UserUrl userUrl) {
        log.info("更新用户URL, id: {}, userUrl: {}", id, userUrl);
        userUrl.setId(id);
        UserUrl updatedUserUrl = userUrlService.update(userUrl);
        return ResponseEntity.ok(Result.success(updatedUserUrl));
    }

    /**
     * 删除用户URL
     *
     * @param id 用户URL ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> delete(@PathVariable Integer id) {
        log.info("删除用户URL, id: {}", id);
        userUrlService.deleteById(id);
        return ResponseEntity.ok(Result.success("删除成功"));
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
    public ResponseEntity<Result<PageInfo<UserUrl>>> getPage(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            UserUrl userUrl) {
        log.info("分页查询用户URL, page: {}, limit: {}, condition: {}", page, limit, userUrl);
        PageInfo<UserUrl> pageInfo = userUrlService.getPage(page, limit, userUrl);
        return ResponseEntity.ok(Result.success(pageInfo));
    }
}
