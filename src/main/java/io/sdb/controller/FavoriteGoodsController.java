package io.sdb.controller;

import io.sdb.common.annotation.LoginUser;
import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.R;
import io.sdb.model.FavoriteGoods;
import io.sdb.model.User;
import io.sdb.service.FavoriteGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/wechat/favoriteGoods")
public class FavoriteGoodsController {

	@Autowired
	private FavoriteGoodsService favoriteGoodsService;

	/**
	 * 添加收藏
	 */
	@ResponseBody
	@GetMapping("/add")
	public R add(){
		//检查是否已被收藏，如果是，直接return true,否则则添加收藏
		return R.ok();
	}

	/**
	 * remove
	 */
	@ResponseBody
	@GetMapping("/remove")
	public R remove(){
		//检查是否已被收藏，如果不是，直接return true,否则则移除
		return R.ok();
	}

}