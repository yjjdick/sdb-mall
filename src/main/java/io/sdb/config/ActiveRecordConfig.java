package io.sdb.config;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.template.source.ClassPathSourceFactory;
import io.sdb.model._MappingKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class ActiveRecordConfig {

    // 由于spring中已经注入了DruidDataSource这里直接拿
    @Autowired
    private DataSource ds;

    @Bean(initMethod="start", destroyMethod="stop")
    public ActiveRecordPlugin init() {
        ActiveRecordPlugin arp = new ActiveRecordPlugin(ds);
        arp.addSqlTemplate("sql/all.sql");
//        arp.addMapping("user", UserModel.class);
        arp.getEngine().setSourceFactory(new ClassPathSourceFactory());
        _MappingKit.mapping(arp);
        return arp;
    }
}
