package io.sdb.modules.app.utils;

import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import io.sdb.model.Product;
import io.sdb.service.ProductService;
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
public class RecordUtilsTest {

    @Autowired
    ProductService productService;

    @Test
    public void converModel() {
        Record record = Db.findFirst("select * from product p where p.sn ='20180715210707'");
        String str = JSON.toJSONString(record.getColumns());
        Product product = JSON.parseObject(str, Product.class);
//        Product product = RecordUtils.converModel(Product.class, record);
        log.info("{}", product);
    }
}