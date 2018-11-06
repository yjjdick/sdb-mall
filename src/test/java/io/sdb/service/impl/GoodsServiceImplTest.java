package io.sdb.service.impl;

import io.sdb.common.utils.PageUtils;
import io.sdb.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.nio.cs.ext.MacArabic;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GoodsServiceImplTest {

    @Autowired
    GoodsService goodsService;

    @Test
    public void queryPage() {
        Map<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("limit", 10);
        PageUtils page = goodsService.queryPage(map);
    }
}