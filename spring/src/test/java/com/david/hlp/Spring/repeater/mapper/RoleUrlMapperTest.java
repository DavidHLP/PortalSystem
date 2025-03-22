package com.david.hlp.Spring.repeater.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.david.hlp.Spring.repeater.entity.RoleUrl;
import com.david.hlp.Spring.SpringBootStarter;
import java.util.List;
import java.util.Arrays;

@SpringBootTest(classes = SpringBootStarter.class)
public class RoleUrlMapperTest {

    @Autowired
    private RoleUrlMapper roleUrlMapper;

    @Test
    public void testListAll() {
        List<RoleUrl> roleUrls = roleUrlMapper.selectList(null);
        System.out.println("roleUrls: " + Arrays.toString(roleUrls.toArray()));
    }
}
