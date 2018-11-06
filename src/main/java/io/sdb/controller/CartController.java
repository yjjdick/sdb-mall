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
import io.sdb.dto.CartDTO;
import io.sdb.model.Cart;
import io.sdb.model.User;
import io.sdb.common.annotation.Login;
import io.sdb.common.annotation.LoginUser;
import io.sdb.form.CartForm;
import io.sdb.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/wechat/cart")
public class CartController {
	@Autowired
	private CartService cartService;

	@PostMapping("/add")
	@Login
	public R info(@LoginUser User user, @RequestBody CartForm cartForm){
		cartService.saveOrUpdate(user.getUserId(), cartForm.getProductId(), cartForm.getQuantity());
		Cart query = new Cart();
		query.setUserId(user.getUserId());
		List<Cart> cartList = cartService.findByModel(query);
		return R.ok().put("cartList", cartList);
	}

	@PostMapping("/remove")
	@Login
	public R remove(@LoginUser User user, @RequestBody CartForm cartForm) {
		Long[] cartItemIdArr = cartForm.getCartItemIds().toArray(new Long[cartForm.getCartItemIds().size()]);
		cartService.deleteBatch(cartItemIdArr);
		Cart query = new Cart();
		query.setUserId(user.getUserId());
		List<Cart> cartList = cartService.findByModel(query);
		return R.ok().put("cartList", cartList);
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	@Login
	public R list(@LoginUser User user){
		List<CartDTO> cartDTOList = cartService.listDetail(user.getUserId());
		return R.ok().put("cartList", cartDTOList);
	}
}