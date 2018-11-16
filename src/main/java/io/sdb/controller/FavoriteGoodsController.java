package io.sdb.controller;

import io.sdb.common.annotation.Login;
import io.sdb.common.annotation.LoginUser;
import io.sdb.common.entity.Filter;
import io.sdb.common.utils.R;
import io.sdb.dto.FavoriteGoodsDTO;
import io.sdb.model.FavoriteGoods;
import io.sdb.model.User;
import io.sdb.service.FavoriteGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/wechat/favoriteGoods")
public class FavoriteGoodsController {

	@Autowired
	private FavoriteGoodsService favoriteGoodsService;

	@ResponseBody
	@GetMapping("list")
	@Login
	public R list(@LoginUser User user){
		List<FavoriteGoodsDTO> favoriteGoodsDTOList = favoriteGoodsService.list(user.getUserId());
		return R.ok().put("favoriteList",favoriteGoodsDTOList);
	}

	/**
	 * 添加收藏
	 */
	@ResponseBody
	@GetMapping("/add/{goodsId}")
	@Login
	public R add(@LoginUser User user, @PathVariable String goodsId){
		List<Filter> filterList = new ArrayList<>();
		Filter filterGoods = new Filter();
		filterGoods.setProperty("favorite_goods");
		filterGoods.setOperator(Filter.Operator.eq);
		filterGoods.setValue(goodsId);
		filterList.add(filterGoods);
		Filter filterUser = new Filter();
		filterUser.setProperty("favorite_user");
		filterUser.setOperator(Filter.Operator.eq);
		filterUser.setValue(user.getUserId());
		filterList.add(filterUser);
		List<FavoriteGoods> favoriteGoodsList = favoriteGoodsService.findByFilters(filterList);
		if(favoriteGoodsList==null || favoriteGoodsList.isEmpty()){
			FavoriteGoods favoriteGoods = new FavoriteGoods();
			favoriteGoods.setFavoriteGoods(goodsId);
			favoriteGoods.setFavoriteUser(user.getUserId());
			favoriteGoodsService.insert(favoriteGoods);
		}
		return R.ok();
	}

	/**
	 * remove
	 */
	@ResponseBody
	@Login
	@GetMapping("/remove/{goodsId}")
	public R remove(@LoginUser User user, @PathVariable String goodsId){
		Filter filter = new Filter();
		filter.setProperty("favorite_goods");
		filter.setOperator(Filter.Operator.eq);
		filter.setValue(goodsId);
		filter.setWhereOpt(Filter.WhereOpt.and);
		filter.setProperty("favorite_user");
		filter.setOperator(Filter.Operator.eq);
		filter.setValue(user.getUserId());
		List<FavoriteGoods> favoriteGoodsList = favoriteGoodsService.findByFilter(filter);
		if(favoriteGoodsList!=null || favoriteGoodsList.size()>0){
			FavoriteGoods favoriteGoods = new FavoriteGoods();
			favoriteGoods.setFavoriteGoods(goodsId);
			favoriteGoods.setFavoriteUser(user.getUserId());
			favoriteGoodsService.deleteByModel(favoriteGoods);
		}
		return R.ok();
	}

}