package io.sdb.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import io.sdb.model.SysMenu;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SysMenuDao extends BaseDao<SysMenu> {
    public SysMenuDao() {
        super(SysMenu.class);
    }

    public List<SysMenu> queryNotButtonList() {
        SqlPara sqlPara = Db.getSqlPara("sysMenu.queryNotButtonList");
        return this.find(sqlPara);
    }
}
