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

import io.sdb.common.annotation.SysLog;
import io.sdb.common.exception.RRException;
import io.sdb.vo.ProductCategoryVO;
import io.sdb.common.utils.R;
import io.sdb.common.utils.TreeBuilder;
import io.sdb.model.ProductCategory;
import io.sdb.service.ProductCategoryService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sys/productCategory")
public class SysProductCategoryController extends AbstractController {
	@Autowired
	private ProductCategoryService productCategoryService;

	@ResponseBody
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
	@GetMapping("/list")
	public R list(){
		List<ProductCategory> productCategoryList = productCategoryService.findAll();
		for(ProductCategory p : productCategoryList){
			ProductCategory productCategory = productCategoryService.findById(p.getParentId());
			if(productCategory!=null){
				p.setParentName(productCategory.getName());
			}
		}
		return R.ok().put("productCategoryList", productCategoryList);
	}

	/**
	 * 分类信息
	 */
	@GetMapping("/info/{productCategoryId}")
	@RequiresPermissions("sys:productCategory:info")
	public R info(@PathVariable("productCategoryId") Long productCategoryId){
		ProductCategory productCategory = productCategoryService.findById(productCategoryId);
		return R.ok().put("productCategory", productCategory);
	}

	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:productCategory:select")
	public R select(){
		//查询列表数据
		List<ProductCategory> productCategoryList = productCategoryService.queryListOrder();
		//添加顶级菜单
		/*ProductCategory root = new ProductCategory();
		root.setId(null);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		root.setOrder(100);
		productCategoryList.add(0, root);*/

		return R.ok().put("productCategoryList", productCategoryList);
	}

	/**
	 * 保存
	 */
	@SysLog("保存商品类目")
	@PostMapping("/save")
	@RequiresPermissions("sys:productCategory:save")
	public R save(@RequestBody ProductCategory productCategory){
		//数据校验
		verifyForm(productCategory);
		productCategoryService.save(productCategory);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@SysLog("修改菜单")
	@PostMapping("/update")
	@RequiresPermissions("sys:productCategory:update")
	public R update(@RequestBody ProductCategory productCategory){
		//数据校验
		verifyForm(productCategory);
		productCategoryService.update(productCategory);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除菜单")
	@PostMapping("/delete/{productCategoryId}")
	@RequiresPermissions("sys:productCategory:delete")
	public R delete(@PathVariable("productCategoryId") long productCategoryId){
		return productCategoryService.delete(productCategoryId);
	}

	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(ProductCategory productCategory){
		if(StringUtils.isBlank(productCategory.getName())){
			throw new RRException("类目名称不能为空");
		}

//		if(productCategory.getParentId() == null){
//			throw new RRException("上级类目不能为空");
//		}
	}
}