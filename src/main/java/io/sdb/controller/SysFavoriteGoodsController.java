package io.sdb.controller;

import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.R;
import io.sdb.service.FavoriteGoodsService;
import io.sdb.model.FavoriteGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/sys/favoriteGoods")
public class SysFavoriteGoodsController {

	@Autowired
	private FavoriteGoodsService favoriteGoodsService;

	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = favoriteGoodsService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 更新
	 */
	@ResponseBody
	@PostMapping("/update")
	public R update(@RequestBody FavoriteGoods favoriteGoods) {
		favoriteGoods.update();
		return R.ok();
	}

	/**
	 * 新增
	 */
	@ResponseBody
	@PostMapping("/save")
	public R save(@RequestBody FavoriteGoods favoriteGoods) {
		favoriteGoods.save();
		return R.ok();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@PostMapping("/delete")
	public R delete(@RequestBody FavoriteGoods favoriteGoods) {
		favoriteGoods.delete();
		return R.ok();
	}

}