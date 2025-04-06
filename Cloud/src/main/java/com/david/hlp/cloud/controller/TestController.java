package com.david.hlp.cloud.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.david.hlp.cloud.entity.User;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class TestController {
    @PostMapping("/test")
    public User postMethodName(@RequestBody User entity) {
        entity.getData().setPassword(entity.getData().getPassword()*7);
        return entity;
    }
}
