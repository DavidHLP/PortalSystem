package com.david.hlp.Spring.repeater.service.impl;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import com.david.hlp.Spring.repeater.service.UserUrlService;
import java.util.Arrays;
@SpringBootTest
public class UserUrlImplTest {

    @Autowired
    private UserUrlService userUrlService;

    @Test
    public void listAll() {
        System.out.println(Arrays.toString(userUrlService.listAll().toArray()));
    }

    @Test
    public void getById() {
        System.out.println(userUrlService.getById(1));
    }
}
