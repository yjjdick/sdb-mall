package io.sdb.dao;

import io.sdb.model.Cart;
import io.sdb.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDao extends BaseDao<Product> {
    public ProductDao() {
        super(Product.class);
    }
}
