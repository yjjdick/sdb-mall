package io.sdb.controller;


import com.lly835.bestpay.model.PayResponse;
import io.sdb.common.utils.R;
import io.sdb.dto.OrderDTO;
import io.sdb.enums.PayStatusEnum;
import io.sdb.model.OrderMaster;
import io.sdb.model.User;
import io.sdb.common.annotation.Login;
import io.sdb.common.annotation.LoginUser;
import io.sdb.service.OrderMasterService;
import io.sdb.service.PayService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * APP登录授权
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:31
 */
@RestController
@RequestMapping("/wechat/pay")
public class WxPayController {

    @Autowired
    PayService payService;

    @Autowired
    OrderMasterService orderService;

    @Login
    @GetMapping("/create/{orderId}")
    public R create(@LoginUser User user, @PathVariable String orderId) {
        PayResponse payResponse = null;
        OrderMaster orderMaster = orderService.findById(orderId);
        if(orderMaster.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setBuyerOpenid(user.getMaOpenId());
            BeanUtils.copyProperties(orderMaster, orderDTO);
            payResponse = payService.create(orderDTO);
        }
        //返回给微信处理结果
        return R.ok().put("payResponse", payResponse);
    }

}
