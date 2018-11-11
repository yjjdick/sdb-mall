package io.sdb.controller;

import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.R;
import io.sdb.service.GrouponTeamService;
import io.sdb.model.GrouponTeam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/sys/grouponTeam")
public class SysGrouponTeamController {

	@Autowired
	private GrouponTeamService grouponTeamService;

	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = grouponTeamService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 详情
	 */
	@ResponseBody
	@GetMapping("/info/{id}")
	public R info(@PathVariable Object id){
        GrouponTeam grouponTeam = grouponTeamService.findById(id);
		return R.ok().put("grouponTeam", grouponTeam);
	}

	/**
	 * 更新
	 */
	@ResponseBody
	@PostMapping("/update")
	public R update(@RequestBody GrouponTeam grouponTeam) {
		grouponTeam.update();
		return R.ok();
	}

	/**
	 * 新增
	 */
	@ResponseBody
	@PostMapping("/save")
	public R save(@RequestBody GrouponTeam grouponTeam) {
		grouponTeam.save();
		return R.ok();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@PostMapping("/delete")
    public R delete(@RequestBody Object[] ids) {
        grouponTeamService.deleteBatch(ids);
        return R.ok();
    }

}