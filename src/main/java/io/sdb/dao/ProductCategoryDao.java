package io.sdb.dao;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import io.sdb.model.Goods;
import io.sdb.model.ProductCategory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductCategoryDao extends BaseDao<ProductCategory> {
    public ProductCategoryDao() {
        super(ProductCategory.class);
    }

    public List<ProductCategory> queryListOrder(){
        SqlPara sqlPara = Db.getSqlPara("productCategory.queryListOrder");
        return this.find(sqlPara);
    }
}
