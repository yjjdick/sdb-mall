package io.sdb.dao;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import io.sdb.model.SysUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SysUserDao extends BaseDao<SysUser> {
    public SysUserDao() {
        super(SysUser.class);
    }

    public List<String> queryAllPerms(Long userId) {
        SqlPara sqlPara = Db.getSqlPara("sysUser.queryAllPerms", Kv.by("userId", userId));
        List<Record> sysUserList = Db.find(sqlPara);
        List<String> perms = new ArrayList<>();
        for (Record r:sysUserList
             ) {
            if(r == null || r.get("perms") == null) {
                continue;
            }
            perms.add(r.get("perms"));
        }

        return perms;
    }

    public List<Long> queryAllMenuId(Long userId) {
        SqlPara sqlPara = Db.getSqlPara("sysUser.queryAllMenuId", Kv.by("userId", userId));
        List<Record> sysMenuList = Db.find(sqlPara);
        List<Long> menuIds = new ArrayList<>();
        for (Record r:sysMenuList
             ) {
            if(r == null || r.get("menu_id") == null) {
                continue;
            }
            menuIds.add(r.get("menu_id"));
        }

        return menuIds;
    }

    public SysUser queryByUserName(String name) {
        SqlPara sqlPara = Db.getSqlPara("sysUser.queryByUserName", Kv.by("username", name));
        SysUser sysUserList = this.findFirst(sqlPara);
        return sysUserList;
    }
}
