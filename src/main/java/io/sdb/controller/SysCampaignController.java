package io.sdb.controller;

import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.R;
import io.sdb.service.CampaignService;
import io.sdb.model.Campaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/sys/campaign")
public class SysCampaignController {

	@Autowired
	private CampaignService campaignService;

	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = campaignService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 更新
	 */
	@ResponseBody
	@PostMapping("/update")
	public R update(@RequestBody Campaign campaign) {
		campaign.update();
		return R.ok();
	}

	/**
	 * 新增
	 */
	@ResponseBody
	@PostMapping("/save")
	public R save(@RequestBody Campaign campaign) {
		campaign.save();
		return R.ok();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@PostMapping("/delete")
	public R delete(@RequestBody Campaign campaign) {
		campaign.delete();
		return R.ok();
	}

}