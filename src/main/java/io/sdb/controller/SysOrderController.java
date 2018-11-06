/**
 * Copyright 2018
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.sdb.controller;

import com.jfinal.validate.Validator;
import io.sdb.common.exception.RRException;
import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.R;
import io.sdb.dto.OrderDTO;
import io.sdb.enums.OrderStatusEnum;
import io.sdb.enums.PayStatusEnum;
import io.sdb.enums.ResultEnum;
import io.sdb.model.OrderDetail;
import io.sdb.model.OrderMaster;
import io.sdb.form.SysOrderForm;
import io.sdb.service.OrderDetailService;
import io.sdb.service.OrderMasterService;
import io.sdb.service.PayService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sys/order")
public class SysOrderController {
	@Autowired
	private OrderMasterService orderService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private PayService payService;

	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = orderService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 退款
	 */
	@ResponseBody
	@PostMapping("/refund")
	public R list(@RequestBody SysOrderForm orderForm){

		OrderMaster orderMaster = orderService.findById(orderForm.getOrderId());
		if (orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()) && orderMaster.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
			orderMaster = payService.refund(orderForm.getOrderId());
			return R.ok().put("refundTradeNo", orderMaster.getRefundTradeNo());
		}else {
			throw new RRException(ResultEnum.ORDER_STATUS_ERROR);
		}

	}

	/**
	 * 信息
	 */
	@ResponseBody
	@GetMapping("/info/{id}")
	public R list(@PathVariable String id){
		OrderMaster orderMaster = orderService.findById(id);
		return R.ok().put("order", orderMaster);
	}

	/**
	 * 详情
	 */
	@ResponseBody
	@GetMapping("/detail/{id}")
	public R detail(@PathVariable String id){
		List<OrderDetail> orderDetailList = orderDetailService.findByModel(new OrderDetail(){
			{this.setOrderId(id);}
		});
		return R.ok().put("orderDetailList", orderDetailList);
	}

	/**
	 * 更新
	 */
	@ResponseBody
	@PostMapping("/update")
	public R update(@RequestBody SysOrderForm orderForm) {
		OrderMaster orderMaster = new OrderMaster();
		BeanUtils.copyProperties(orderForm, orderMaster);
		orderMaster.update();
		return R.ok();
	}

	/**
	 * 发货
	 */
	@ResponseBody
	@PostMapping("/shipping")
	public R shipping(@RequestBody SysOrderForm orderForm) {
		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setOrderId(orderForm.getOrderId());
		orderDTO.setTrackingNumber(orderForm.getTrackingNumber());
		orderDTO.setDeliveryCode(orderForm.getDeliveryCode());

		orderService.shipping(orderDTO, orderForm.getDeliveryCode());
		return R.ok();
	}

	/**
	 * 收款
	 */
	@ResponseBody
	@PostMapping("/gathering")
	public R gathering(@RequestBody SysOrderForm orderForm) {
		OrderMaster orderMaster = new OrderMaster();
		BeanUtils.copyProperties(orderForm, orderMaster);
		orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
		orderMaster.setPayStatus(PayStatusEnum.SUCCESS.getCode());
		orderMaster.update();
		return R.ok();
	}

}
