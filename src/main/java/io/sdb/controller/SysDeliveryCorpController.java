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

import io.sdb.common.utils.R;
import io.sdb.model.DeliveryCorp;
import io.sdb.service.DeliveryCorpService;
import io.sdb.vo.DeliveryCorpVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sys/deliveryCorp")
public class SysDeliveryCorpController {

	@Autowired
	DeliveryCorpService deliveryCorpService;
	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	public R list(){
		List<DeliveryCorp> deliveryCorpList = deliveryCorpService.findAll();
		List<DeliveryCorpVO> deliveryCorpVOList = deliveryCorpList.stream().map(item -> {
			return convert(item);
		}).collect(Collectors.toList());
		return R.ok().put("deliveryCorpVOList", deliveryCorpVOList);
	}

	private DeliveryCorpVO convert(DeliveryCorp deliveryCorp) {
		DeliveryCorpVO deliveryCorpVO = new DeliveryCorpVO();
		deliveryCorpVO.setValue(deliveryCorp.getCode());
		deliveryCorpVO.setLabel(deliveryCorp.getName());
		return deliveryCorpVO;
	}
}
