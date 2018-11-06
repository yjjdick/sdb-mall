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

import io.sdb.common.entity.Filter;
import io.sdb.common.utils.R;
import io.sdb.dto.ProductDTO;
import io.sdb.model.Product;
import io.sdb.model.Specification;
import io.sdb.service.ProductService;
import io.sdb.service.SpecificationService;
import io.sdb.vo.ProductVO;
import io.sdb.vo.SpecificationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/wechat/product")
public class ProductController {
	@Autowired
	private ProductService productService;

	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/listByGoodId/{goodId}")
	public R listByGoodId(@PathVariable String goodId){
		Product product = new Product();
		product.setGoodsSn(goodId);
		List<Product> productList = productService.findByModel(product);
		return R.ok().put("productList", productList);
	}

	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/listByProductIds/{productIds}")
	public R listByProductIds(@PathVariable String productIds){
        String[] productIdArr = productIds.split(",");
//        List<String> pruductIdList = Arrays.asList(productIdArr);
        Filter filter = new Filter();
        filter.setOperator(Filter.Operator.in);
        filter.setProperty("sn");
        filter.setValue(productIdArr);
        List<Product> productList = productService.findByFilter(filter);
		return R.ok().put("productList", productList);
	}

	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/listDetailByProductIds/{productIds}")
	public R listDetailByProductIds(@PathVariable String productIds){
        List<ProductDTO> productList = productService.listDetailByProductIds(productIds);

        List<ProductVO> productVOList = productList.stream().map(item -> {
            ProductVO productVO = new ProductVO();
            BeanUtils.copyProperties(item, productVO);
            return productVO;
        }).collect(Collectors.toList());

		return R.ok().put("productList", productVOList);
	}
}