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

import com.jfinal.plugin.activerecord.Page;
import io.sdb.common.entity.Filter;
import io.sdb.common.entity.Order;
import io.sdb.common.utils.R;
import io.sdb.enums.GeneralEnum;
import io.sdb.model.Goods;
import io.sdb.model.ProductCategory;
import io.sdb.common.annotation.Login;
import io.sdb.form.GoodsListForm;
import io.sdb.service.GoodsService;
import io.sdb.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/wechat/goods")
public class GoodsController {
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private ProductCategoryService productCategoryService;

	/**
	 * 列表
	 */
	@ResponseBody
	@Login
	@GetMapping("/info/{goodId}")
	public R info(@PathVariable String goodId){
		Goods goods = goodsService.findById(goodId);
		return R.ok().put("goodsInfo", goods);
	}

	/**
	 * 列表
	 */
	@ResponseBody
	@Login
	@PostMapping("/list")
	public R info(@RequestBody GoodsListForm goodsListForm){
		List<Filter> filterList = new ArrayList<>();

		Filter filter = new Filter();
		if (goodsListForm.getCategoryId() != null &&!goodsListForm.getCategoryId().equals(-1)) {
			filter.setProperty("product_category_id");
			filter.setOperator(Filter.Operator.in);
			Filter pc = new Filter();
			pc.setProperty("tree_path");
			pc.setValue(goodsListForm.getCategoryId());
			pc.setOperator(Filter.Operator.like);
			List<ProductCategory> productCategoryList = productCategoryService.findByFilter(pc);
			List<Long> categoryIds = productCategoryList.stream().map(item -> {
				return item.getId();
			}).collect(Collectors.toList());
			filter.setValue(categoryIds);
			filterList.add(filter);
		}

		filter = new Filter();
		filter.setProperty("is_marketable");
		filter.setValue(GeneralEnum.TRUE.getCode());
		filter.setOperator(Filter.Operator.eq);
		filterList.add(filter);

		Order order = new Order();
		order.setProperty("create_date");
		order.setDirection(Order.Direction.desc);

		Page<Goods> pr = goodsService.paginate(goodsListForm.getPageNum(), goodsListForm.getPageSize(), filterList, order);

		return R.ok().put("goodsPage", pr);
	}
}