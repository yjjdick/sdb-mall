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
import io.sdb.model.ProductCategory;
import io.sdb.common.annotation.Login;
import io.sdb.service.ProductCategoryService;
import io.sdb.vo.ProductCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/wechat/productCategory")
public class ProductCategoryController {
	@Autowired
	private ProductCategoryService productCategoryService;

	@ResponseBody
	@Login
	@GetMapping("/tree")
	public R tree(){
		List<ProductCategory> productCategoryList = productCategoryService.findAll();

		List<ProductCategoryVO> productCategoryVOList = productCategoryList.stream().map(item -> {
			ProductCategoryVO productCategoryVO = new ProductCategoryVO(item);
			return productCategoryVO;
		}).collect(Collectors.toList());
		List<ProductCategoryVO> productCategoryVOTree = new TreeBuilder().buildTree(productCategoryVOList);

		return R.ok().put("productCategroyTree", productCategoryVOTree);
	}

	/**
	 * 列表
	 */
	@ResponseBody
	@Login
	@GetMapping("/list")
	public R list(){
		List<ProductCategory> productCategoryList = productCategoryService.findAll();
		return R.ok().put("productCategoryList", productCategoryList);
	}
}