package io.sdb.dao;

import io.sdb.model.Cart;
import io.sdb.model.OrderMaster;
import org.springframework.stereotype.Component;

@Component
public class OrderMasterDao extends BaseDao<OrderMaster> {
    public OrderMasterDao() {
        super(OrderMaster.class);
    }
}
