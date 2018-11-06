package io.sdb.dao;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import io.sdb.common.entity.Filter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SysUserDaoTest {

    @Autowired
    SysUserDao sysUserDao;

    @Test
    public void queryAllPerms() {
        List<String> perms = sysUserDao.queryAllPerms(2L);
        log.info("{}", perms);
    }

    @Test
    public void test() {
        List<Filter> filterList = new ArrayList<>();
        Filter filter = new Filter();
        filter.setProperty("aaa");
        filter.setOperator(Filter.Operator.eq);
        filter.setValue("123");
        filterList.add(filter);

        filter = new Filter();
        filter.setProperty(null);
        filter.setOperator(Filter.Operator.eq);
        filter.setValue("111");
        filterList.add(filter);

        filter = new Filter();
        filter.setProperty("bbb");
        filter.setOperator(Filter.Operator.eq);
        filter.setValue("321");
        filterList.add(filter);

        filter = new Filter();
        filter.setProperty("intest");
        filter.setOperator(Filter.Operator.in);
        filter.setValue(new ArrayList<String>() {
            {add("aaa");add("bbb");}
        });
        filterList.add(filter);
        SqlPara sqlPara = Db.getSqlPara("common.findList", Kv.by("tableName", "aaa").set("filters", filterList));
        List<Record> record = Db.find(sqlPara);
        log.info("{}", record);
    }
}