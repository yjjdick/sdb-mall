package io.sdb.dao;

import io.sdb.model.OrderDetail;
import io.sdb.model.OrderMaster;
import org.springframework.stereotype.Component;

@Component
public class OrderDetailDao extends BaseDao<OrderDetail> {
    public OrderDetailDao() {
        super(OrderDetail.class);
    }
}
