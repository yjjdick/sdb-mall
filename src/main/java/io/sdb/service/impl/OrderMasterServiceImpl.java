package io.sdb.service.impl;

import cn.hutool.core.date.DateUtil;
import com.jfinal.plugin.activerecord.Page;
import io.sdb.common.annotation.JFinalTx;
import io.sdb.common.entity.Filter;
import io.sdb.common.entity.Order;
import io.sdb.common.exception.RRException;
import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.Query;
import io.sdb.config.OrderProperties;
import io.sdb.dao.OrderMasterDao;
import io.sdb.dao.SnDao;
import io.sdb.dto.CartDTO;
import io.sdb.dto.OrderDTO;
import io.sdb.enums.*;
import io.sdb.model.*;
import io.sdb.service.LogisticsService;
import io.sdb.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderMasterServiceImpl extends BaseServiceImpl<OrderMasterDao, OrderMaster> implements OrderMasterService {

    private final Integer GROUPON_EXPIRE_DAY = 2;

    @Autowired
    ProductService productService;

    @Autowired
    SnService snService;

    @Autowired
    PayService payService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    OrderMasterService orderMasterService;

    @Autowired
    LogisticsService sysLogisticsService;

    @Autowired
    OrderProperties orderProperties;

    @Autowired
    GrouponService grouponService;

    @Autowired
    GrouponTeamService grouponTeamService;


    @Autowired
    GoodsService goodsService;

    @Autowired
    SnDao snDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String searchKey = (String)params.get("searchKey");
        Query<Goods> query = new Query<Goods>(params);
        List<Filter> filters = new ArrayList<>();

        if (!StringUtils.isBlank(searchKey)) {
            Filter filter = new Filter();
            filter.setProperty("order_id");
            filter.setValue(searchKey);
            filter.setOperator(Filter.Operator.like);
            filters.add(filter);
        }

        Order order = new Order();
        order.setProperty("create_date");
        order.setDirection(Order.Direction.desc);

        Page<OrderMaster> pr = this.paginate(query.getCurrPage(), query.getLimit(), filters, order);

        return new PageUtils(pr);
    }

    @Override
    @JFinalTx
    public OrderDTO create(OrderDTO orderDTO) {
        if (orderDTO.getGroupon() == GeneralEnum.TRUE.getCode() &&  !StringUtils.isBlank(orderDTO.getGrouponId())) {
            Groupon groupon = grouponService.findById(orderDTO.getGrouponId());
            Integer total = groupon.getCount();
            Filter filter = new Filter();
            filter.setProperty("groupon_id");
            filter.setOperator(Filter.Operator.eq);
            filter.setValue(orderDTO.getGrouponId());

            List<GrouponTeam> grouponTeamList = grouponTeamService.findByFilter(filter);
            Integer joinCount = grouponTeamList.size();
            if (total <= joinCount) {
                throw new RRException(ResultEnum.GROUPON_NOT_ENOUGH);
            }
        }

        String orderId = snService.generate(SnEnum.ORDER_MASTER);
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);

        //1. 查询商品（数量, 价格）
        for (OrderDetail orderDetail: orderDTO.getOrderDetailList()) {
//            Product ProductInfo =  productService.findById(orderDetail.getProductId());
//            if (ProductInfo == null) {
//                throw new RRException(ResultEnum.PRODUCT_NOT_EXIST);
//            }

            //2. 计算订单总价
            if (orderDTO.getGroupon() == GeneralEnum.TRUE.getCode()) {
                orderAmount = orderDetail.getGroupPrice()
                        .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                        .add(orderAmount);
            } else{
                orderAmount = orderDetail.getProductPrice()
                        .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                        .add(orderAmount);
            }


            //订单详情入库
            String orderDetailId = snService.generate(SnEnum.ORDER_DETAIL);
            orderDetail.setDetailId(orderDetailId);
            orderDetail.setOrderId(orderId);
            orderDetail.save();

        }

        //3. 写入订单数据库（orderMaster和orderDetail）
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMaster.setRemark(orderDTO.getRemark());
        orderMaster.save();

        orderDTO.setOrderAmount(orderAmount);
        orderDTO.setCreateTime(new Date());
        orderDTO.setUpdateTime(new Date());
        //4. 扣库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
        ).collect(Collectors.toList());
        productService.decreaseStock(cartDTOList);

        return orderDTO;
    }

    @Override
    @JFinalTx
    public OrderMaster cancel(String orderId) {
        OrderMaster orderMaster = orderMasterService.findById(orderId);
        if(orderMaster == null) {
            throw new RRException(ResultEnum.ORDER_NOT_EXIST);
        }
        orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        orderMaster.setPayStatus(PayStatusEnum.REFUND.getCode());
        OrderDetail query = new OrderDetail();
        query.setOrderId(orderMaster.getOrderId());

        List<OrderDetail> orderDetails = orderDetailService.findByModel(query);
        List<CartDTO> cartDTOList = orderDetails.stream().map(item -> {
            CartDTO cartDTO = new CartDTO(item.getProductId(), item.getProductQuantity());
            return cartDTO;
        }).collect(Collectors.toList());
        productService.creaseStock(cartDTOList);

        orderMaster.update();
        if (orderProperties.getRefund()) {
            payService.refund(orderId);
        }

        return orderMaster;
    }

    @Override
    @JFinalTx
    public OrderMaster paid(OrderMaster orderMaster) {
        //判断订单状态
        if (!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", orderMaster.getOrderId(), orderMaster.getOrderStatus());
            throw new RRException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //判断支付状态
        if (!orderMaster.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            return orderMaster;
        }

        boolean grouponSucc = false;

        if (orderMaster.getGroupon() == GeneralEnum.TRUE.getCode()) {
            orderMaster.setOrderStatus(OrderStatusEnum.GROUPON_PENDING.getCode());

            if (StringUtils.isBlank(orderMaster.getGrouponId())) {
                String sn = snDao.generate(SnEnum.GROUPON);
                Groupon groupon = new Groupon();
                OrderDetail queryOrderDetail = new OrderDetail();
                queryOrderDetail.setOrderId(orderMaster.getOrderId());
                OrderDetail orderDetail = orderDetailService.findFirstByModel(queryOrderDetail);
                String productId = orderDetail.getProductId();
                Product product = productService.findById(productId);
                groupon.setGoodsId(product.getGoodsSn());
                groupon.setId(sn);
                groupon.setCreateDate(new Date());
                groupon.setUpdateDate(new Date());
                groupon.setCount(orderMaster.getGrouponCount());
                groupon.setStatus(GrouponStatusEnum.PENDING.getCode());
                Date expireTime = DateUtil.offsetDay(new Date(), GROUPON_EXPIRE_DAY);
                groupon.setExpireDate(expireTime);
                grouponService.insert(groupon);

                GrouponTeam grouponTeam = new GrouponTeam();
                grouponTeam.setCaptain(GeneralEnum.TRUE.getCode());
                grouponTeam.setGrouponId(sn);
                grouponTeam.setCreateDate(new Date());
                grouponTeam.setUpdateDate(new Date());
                grouponTeam.setUserId(orderMaster.getBuyerId());
                grouponTeamService.insert(grouponTeam);

                orderMaster.setGrouponId(sn);
            }else {
                GrouponTeam grouponTeam = new GrouponTeam();
                grouponTeam.setCaptain(GeneralEnum.FALSE.getCode());
                grouponTeam.setGrouponId(orderMaster.getGrouponId());
                grouponTeam.setUpdateDate(new Date());
                grouponTeam.setUserId(orderMaster.getBuyerId());
                grouponTeamService.insert(grouponTeam);

                Filter filter = new Filter();
                filter.setProperty("group_id");
                filter.setValue(orderMaster.getGrouponId());
                filter.setOperator(Filter.Operator.eq);

                List<GrouponTeam> grouponTeamList = grouponTeamService.findByFilter(filter);
                if (grouponTeamList.size() >= orderMaster.getGrouponCount()) {
                    Groupon groupon = new Groupon();
                    groupon.setId(orderMaster.getGrouponId());
                    groupon.setStatus(GrouponStatusEnum.OPEN.getCode());
                    groupon.setUpdateDate(new Date());
                    groupon.update();

                    orderMaster.setOrderStatus(OrderStatusEnum.GROUPON_SUCC.getCode());
                    grouponSucc = true;
                }
            }


        }

        //修改支付状态
        orderMaster.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        orderMaster.setUpdateDate(new Date());
        if (!orderMaster.update()) {
            log.error("【订单支付完成】更新失败, orderMaster={}", orderMaster);
            throw new RRException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        if (grouponSucc) {
            OrderMaster uptOrder = new OrderMaster();
            uptOrder.setGrouponId(orderMaster.getGrouponId());
            uptOrder.setOrderStatus(OrderStatusEnum.GROUPON_SUCC.getCode());
            uptOrder.update();
        }

        return orderMaster;
    }

    @Override
    @JFinalTx
    public Boolean shipping(OrderDTO orderDTO, String deliveryCode) {
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId(orderDTO.getOrderId());
        orderMaster.setTrackingNumber(orderDTO.getTrackingNumber());
        orderMaster.setDeliveryCode(orderDTO.getDeliveryCode());
        orderMaster.setOrderStatus(OrderStatusEnum.SHIPPING.getCode());
        orderMaster.update();
        if(deliveryCode != null) {
            sysLogisticsService.subscribe(orderMaster.getTrackingNumber(), deliveryCode);
        }

        return true;
    }

    @Override
    @JFinalTx
    public Boolean delete(String orderId) {
        OrderMaster mpara = new OrderMaster();
        mpara.setOrderId(orderId);
        this.deleteByModel(mpara);
        OrderDetail para = new OrderDetail();
        para.setOrderId(orderId);
        orderDetailService.deleteByModel(para);
        return true;
    }

    @Override
    public BigDecimal getTodayTotalSale() {
        List<OrderMaster> orderMasterList = this.getTodayTotalOrders();
        BigDecimal totalSale = BigDecimal.ZERO;
        for (OrderMaster orderMaster:orderMasterList
                ) {
            totalSale.add(orderMaster.getOrderAmount());
        }

        return totalSale;
    }

    @Override
    public List<OrderMaster> getTodayTotalOrders() {
        List<Filter> filterList = new ArrayList<>();

        Filter start = new Filter();
        start.setOperator(Filter.Operator.ge);
        start.setProperty("create_date");
        Date todayBegin = DateUtil.beginOfDay(new Date());
        start.setValue(todayBegin);
        filterList.add(start);

        Filter end = new Filter();
        end.setOperator(Filter.Operator.le);
        end.setProperty("create_date");
        Date todayEnd = DateUtil.endOfDay(new Date());
        end.setValue(todayEnd);
        filterList.add(end);

        Filter payFilter = new Filter();
        payFilter.setOperator(Filter.Operator.eq);
        payFilter.setProperty("pay_status");
        payFilter.setValue(PayStatusEnum.SUCCESS);
        filterList.add(payFilter);

        List<OrderMaster> orderMasterList = this.findByFilters(filterList);

        return orderMasterList;
    }

    @Override
    public List<OrderMaster> getTodayOrders() {
        List<Filter> filterList = new ArrayList<>();

        Filter start = new Filter();
        start.setOperator(Filter.Operator.ge);
        start.setProperty("create_date");
        Date todayBegin = DateUtil.beginOfDay(new Date());
        start.setValue(todayBegin);
        filterList.add(start);

        Filter end = new Filter();
        end.setOperator(Filter.Operator.le);
        end.setProperty("create_date");
        Date todayEnd = DateUtil.endOfDay(new Date());
        end.setValue(todayEnd);
        filterList.add(end);

        Filter orderFilter = new Filter();
        orderFilter.setOperator(Filter.Operator.ne);
        orderFilter.setProperty("order_status");
        orderFilter.setValue(OrderStatusEnum.CANCEL);
        filterList.add(orderFilter);

        List<OrderMaster> orderMasterList = this.findByFilters(filterList);

        return orderMasterList;
    }


}
