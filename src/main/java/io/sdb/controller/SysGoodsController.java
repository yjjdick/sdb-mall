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

import io.sdb.model.Goods;
import io.sdb.model.Product;
import io.sdb.model.ProductCategory;
import io.sdb.form.GoodsForm;
import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.R;
import io.sdb.service.GoodsService;
import io.sdb.service.ProductCategoryService;
import io.sdb.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sys/goods")
public class SysGoodsController {
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = goodsService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 新增
	 */
	@ResponseBody
	@PostMapping("/add")
	public R add(@RequestBody GoodsForm goodsForm){
		goodsService.saveUpdateGoods(goodsForm.getGoods(), goodsForm.getParaList(), goodsForm.getSpecList());
		return R.ok();
	}

	/**
	 * 信息
	 */
	@ResponseBody
	@GetMapping("/info/{goodsId}")
	public R info(@PathVariable String goodsId){
		Goods goods = goodsService.findById(goodsId);
		Product query = new Product();
		query.setGoodsSn(goodsId);
		List<Product> productList = productService.findByModel(query);
		goods.setProductList(productList);

		ProductCategory productCategory = productCategoryService.findById(goods.getProductCategoryId());
		return R.ok().put("goodsInfo", goods).put("treePath", productCategory.getTreePath());
	}

	@ResponseBody
	@PostMapping("batchShelf")
	public R batchShelf(@RequestBody String[] ids){
		return goodsService.batchShelf(ids);
	}

	@ResponseBody
	@PostMapping("batchObtained")
	public R batchObtained(@RequestBody String[] ids){
		return goodsService.batchObtained(ids);
	}
}
