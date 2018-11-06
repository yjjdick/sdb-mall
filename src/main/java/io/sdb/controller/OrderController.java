package io.sdb.controller;

import com.jfinal.plugin.activerecord.Page;
import com.lly835.bestpay.model.PayResponse;
import io.sdb.common.entity.Filter;
import io.sdb.common.entity.Order;
import io.sdb.common.exception.RRException;
import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.R;
import io.sdb.dto.OrderDTO;
import io.sdb.dto.ProductDTO;
import io.sdb.enums.*;
import io.sdb.form.*;
import io.sdb.model.*;
import io.sdb.common.annotation.Login;
import io.sdb.common.annotation.LoginUser;
import io.sdb.service.LogisticsService;
import io.sdb.service.*;
import io.sdb.vo.OrderDetailVO;
import io.sdb.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wechat/order")
public class OrderController {

    @Autowired
    ReceiverService receiverService;

    @Autowired
    SnService snService;

    @Autowired
    CartService cartService;

    @Autowired
    OrderMasterService orderService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    ProductService productService;

    @Autowired
    PayService payService;

    @Autowired
    LogisticsService logisticsService;


    @Login
    @PostMapping("refund")
    public R refund(HttpServletRequest request, @LoginUser User user, @RequestBody RefundForm refundForm) {
        OrderMaster orderMaster = orderService.findById(refundForm.getOrderId());
        if (orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()) && orderMaster.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            orderMaster = payService.refund(refundForm.getOrderId());
            return R.ok().put("refundTradeNo", orderMaster.getRefundTradeNo());
        }else {
            throw new RRException(ResultEnum.ORDER_STATUS_ERROR);
        }
    }

    @Login
    @PostMapping("cancel")
    public R cancel(@LoginUser User user,@RequestBody RefundForm refundForm) {
        OrderMaster orderMaster = orderService.cancel(refundForm.getOrderId());
        return R.ok().put("refundTradeNo", orderMaster.getRefundTradeNo());
    }

    @Login
    @GetMapping("logistic/{orderId}")
    public R logistic(@LoginUser User user, @PathVariable String orderId) {
        OrderMaster orderMaster = orderService.findById(orderId);
        Logistics logistics = logisticsService.findById(orderMaster.getTrackingNumber());
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderMaster, orderVO);
        return R.ok().put("orderInfo", orderVO).put("logistics", logistics);
    }

    @Login
    @PostMapping("finish")
    public R finish(@LoginUser User user, @RequestBody OrderForm orderForm) {
        OrderMaster orderMaster = orderService.findById(orderForm.getOrderId());
        if (!orderMaster.getOrderStatus().equals(OrderStatusEnum.SHIPPING.getCode()) || !orderMaster.getBuyerId().equals(user.getUserId())) {
            throw new RRException(ResultEnum.ORDER_STATUS_ERROR);
        }
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderMaster.update();
        return R.ok();
    }

    @Login
    @PostMapping("delete")
    public R delete(@LoginUser User user, @RequestBody OrderForm orderForm) {
        OrderMaster orderMaster = orderService.findById(orderForm.getOrderId());
        if (orderMaster.getOrderStatus().equals(OrderStatusEnum.FINISHED.getCode()) || orderMaster.getOrderStatus().equals(OrderStatusEnum.SHIPPING.getCode()) || !orderMaster.getBuyerId().equals(user.getUserId())) {
            throw new RRException(ResultEnum.ORDER_STATUS_ERROR);
        }
        orderService.delete(orderForm.getOrderId());
        return R.ok();
    }

    @Login
    @GetMapping("info/{id}")
    public R checkout(@LoginUser User user, @PathVariable String id) {
        OrderMaster orderMaster = orderService.findById(id);
        OrderDetail query = new OrderDetail();
        query.setOrderId(orderMaster.getOrderId());
        List<OrderDetail> orderDetailList = orderDetailService.findByModel(query);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderMaster, orderVO);
        List<OrderDetailVO> orderDetailVOList = orderDetailList.stream().map(item -> {
            OrderDetailVO orderDetailVO = new OrderDetailVO();
            BeanUtils.copyProperties(item, orderDetailVO);
            return orderDetailVO;
        }).collect(Collectors.toList());
        orderVO.setOrderDetailList(orderDetailVOList);
        return R.ok().put("orderInfo", orderVO);
    }

    @Login
    @PostMapping("detailList")
    public R detailList(@LoginUser User user,@RequestBody OrderDetailForm orderDetailForm) {

        List<Filter> filterList = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();
        Filter filter = new Filter();
        filter.setProperty("buyer_id");
        filter.setOperator(Filter.Operator.eq);
        filter.setValue(user.getUserId());
        filterList.add(filter);

        if (orderDetailForm.getOrderStatus() != -1) {
            filter = new Filter();
            filter.setProperty("order_status");
            filter.setOperator(Filter.Operator.in);
            filter.setValue(orderDetailForm.getOrderStatus());
            filterList.add(filter);
        }
        if (orderDetailForm.getPayStatus() != -1) {
            filter = new Filter();
            filter.setProperty("pay_status");
            filter.setOperator(Filter.Operator.in);
            filter.setValue(orderDetailForm.getPayStatus());
            filterList.add(filter);
        }

        Order order = new Order();
        order.setProperty("create_date");
        order.setDirection(Order.Direction.desc);
        orderList.add(order);

        Page<OrderMaster> orderDetailPage = orderService.paginate(orderDetailForm.getPageNum(), orderDetailForm.getPageSize(), filterList, orderList);

        if(orderDetailPage.getList().size() == 0) {
            return R.ok().put("orderDetailPage", new ArrayList<>());
        }

        List<String> orderIdList = orderDetailPage.getList().stream().map(item -> {
            return item.getOrderId();
        }).collect(Collectors.toList());

        filter = new Filter();
        filter.setProperty("order_id");
        filter.setOperator(Filter.Operator.in);
        filter.setValue(orderIdList);
        filterList.add(filter);

        List<OrderVO> orderVOList = orderDetailPage.getList().stream().map(item -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(item, orderVO);
            return orderVO;
        }).collect(Collectors.toList());

        List<OrderDetail> orderDetailList = orderDetailService.findByFilter(filter);
        for (OrderVO orderVO:orderVOList
             ) {
            for (OrderDetail detail :
                    orderDetailList) {
                if (detail.getOrderId().equals(orderVO.getOrderId())) {
                    OrderDetailVO orderDetailVO = new OrderDetailVO();
                    BeanUtils.copyProperties(detail, orderDetailVO);
                    if(orderVO.getOrderDetailList() == null) {
                        orderVO.setOrderDetailList(new ArrayList<>());
                    }
                    orderVO.getOrderDetailList().add(orderDetailVO);
                }
            }
        }

        return R.ok().put("orderDetailPage", new PageUtils(orderVOList, orderDetailPage.getTotalPage(), orderDetailPage.getPageSize(), orderDetailForm.getPageNum()));
    }

    @Login
    @PostMapping("create")
    public R create(@LoginUser User user,@RequestBody CheckOutForm checkOutForm) {

        if (checkOutForm.getProductInfos() == null || checkOutForm.getProductInfos().size() == 0) {
            throw new RRException(ResultEnum.CART_CANNOT_NULL);
        }

        InvoiceInfo invoiceInfo = checkOutForm.getInvoiceInfo();
        ReceiveInfo receiveInfo = checkOutForm.getReceiveInfo();

        List<String> productIds = checkOutForm.getProductInfos().stream().map(item -> {
            return item.getId();
        }).collect(Collectors.toList());

        List<ProductDTO> productList = productService.listDetailByProductIds(String.join(",", productIds));

        for (ProductDTO item :productList
             ) {
            for (ProductInfo info :checkOutForm.getProductInfos()
                 ) {
                if (info.getId().equals(item.getSn())) {
                    item.setQuantity(info.getQuantity());
                    break;
                }
            }
        }

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerAddress(receiveInfo.getFullAddr());
        orderDTO.setBuyerName(receiveInfo.getName());
        orderDTO.setBuyerOpenid(user.getMaOpenId());
        orderDTO.setBuyerPhone(receiveInfo.getPhone());
        orderDTO.setBuyerId(user.getUserId());
        orderDTO.setTitle(invoiceInfo.getTitle());
        orderDTO.setTaxNumber(invoiceInfo.getTaxNumber());
        orderDTO.setBankAccount(invoiceInfo.getBankAccount());
        orderDTO.setBankName(invoiceInfo.getBankName());
        orderDTO.setInvoiceType(invoiceInfo.getType());
        orderDTO.setCompanyAddress(invoiceInfo.getCompanyAddress());
        orderDTO.setTelephone(invoiceInfo.getTelephone());
        orderDTO.setRemark(checkOutForm.getRemark());
        orderDTO.setNeedInvoice(checkOutForm.isNeedInvoice()? GeneralEnum.TRUE.getCode(): GeneralEnum.FALSE.getCode());
        orderDTO.setTitle(invoiceInfo.getTitle());
        orderDTO.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderDTO.setPayStatus(PayStatusEnum.WAIT.getCode());
        List<OrderDetail> orderDetails = productList.stream().map(item->{
            OrderDetail orderDetail = new OrderDetail();
            String sn = snService.generate(SnEnum.ORDER_MASTER);
            orderDetail.setDetailId(sn);
            orderDetail.setProductIcon(item.getImage());
            orderDetail.setProductId(item.getSn());
            orderDetail.setProductName(item.getName());
            orderDetail.setProductModel(item.getModel());
            orderDetail.setProductSpec(item.getSpecificationValues());
            orderDetail.setProductPrice(item.getPrice());
            orderDetail.setProductQuantity(item.getQuantity());
            return orderDetail;
        }).collect(Collectors.toList());
        orderDTO.setOrderDetailList(orderDetails);

        orderDTO = orderService.create(orderDTO);
        if (checkOutForm.getCartItemIds() != null && checkOutForm.getCartItemIds().size() > 0) {
            Object[] ids = checkOutForm.getCartItemIds().toArray();
            cartService.deleteBatch(ids);
        }

        PayResponse payResponse = null;
        if(orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            payResponse = payService.create(orderDTO);
        }

        return R.ok().put("orderDTO", orderDTO).put("payResponse", payResponse);
    }



}
