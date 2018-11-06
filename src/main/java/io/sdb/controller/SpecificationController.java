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
import io.sdb.common.utils.TreeBuilder;
import io.sdb.model.Specification;
import io.sdb.service.SpecificationService;
import io.sdb.vo.SpecificationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/wechat/specification")
public class SpecificationController {
	@Autowired
	private SpecificationService specificationService;

	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	public R list(){
		List<Specification> specificationList = specificationService.findAll();

		List<SpecificationVO> specificationVOList = specificationList.stream().map(item -> {
			SpecificationVO specificationVO = new SpecificationVO(item);
			return specificationVO;
		}).collect(Collectors.toList());

		return R.ok().put("specificationList", specificationVOList);
	}
}