package io.sdb.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SysLogisticsServiceImplTest {

    @Autowired
    LogisticsServiceImpl sysLogisticsService;

    @Test
    public void subscribe() {
        sysLogisticsService.subscribe("800325920738721358", "yuantong");
    }
}