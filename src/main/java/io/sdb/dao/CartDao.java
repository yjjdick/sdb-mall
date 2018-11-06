package io.sdb.dao;

import io.sdb.model.Cart;
import io.sdb.model.Goods;
import org.springframework.stereotype.Component;

@Component
public class CartDao extends BaseDao<Cart> {
    public CartDao() {
        super(Cart.class);
    }
}
