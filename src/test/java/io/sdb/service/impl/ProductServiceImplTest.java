package io.sdb.service.impl;

import io.sdb.dto.ProductDTO;
import io.sdb.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ProductServiceImplTest {

    @Autowired
    ProductService productService;

    @Test
    public void listDetailByProductIds() {
        List<ProductDTO> productDTOList = productService.listDetailByProductIds("20180715210707,20180715210708");
        log.info("{}", productDTOList);
    }
}