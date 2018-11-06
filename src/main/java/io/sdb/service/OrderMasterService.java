package io.sdb.service;

import io.sdb.common.utils.PageUtils;
import io.sdb.dto.OrderDTO;
import io.sdb.model.OrderMaster;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderMasterService extends BaseService<OrderMaster> {
    PageUtils queryPage(Map<String, Object> params);

    OrderDTO create(OrderDTO orderDTO);


    OrderMaster cancel(String orderId);

    /** 支付订单. */
    OrderMaster paid(OrderMaster orderMaster);

    Boolean shipping(OrderDTO orderDTO, String deliveryCode);

    Boolean delete(String orderId);

    BigDecimal getTodayTotalSale();

    List<OrderMaster> getTodayTotalOrders();

    List<OrderMaster> getTodayOrders();

}