package io.sdb.service.impl;

import io.sdb.dao.OrderDetailDao;;
import io.sdb.model.OrderDetail;
import io.sdb.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends BaseServiceImpl<OrderDetailDao, OrderDetail> implements OrderDetailService {
}
