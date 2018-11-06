package io.sdb.service.impl;

import io.sdb.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CartServiceImplTest {

    @Autowired
    CartService cartService;

    @Test
    public void saveOrUpdate() {
        cartService.saveOrUpdate("1", "2", 1);
    }
}