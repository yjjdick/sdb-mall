package io.sdb.controller;

import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.R;
import io.sdb.service.GrouponService;
import io.sdb.model.Groupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/sys/groupon")
public class SysGrouponController {

	@Autowired
	private GrouponService grouponService;

	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = grouponService.queryPage(params);
		return R.ok().put("page", page);
	}

	/**
	 * 详情
	 */
	@ResponseBody
	@GetMapping("/info/{id}")
	public R info(@PathVariable Object id){
        Groupon groupon = grouponService.findById(id);
		return R.ok().put("groupon", groupon);
	}

	/**
	 * 更新
	 */
	@ResponseBody
	@PostMapping("/update")
	public R update(@RequestBody Groupon groupon) {
		groupon.update();
		return R.ok();
	}

	/**
	 * 新增
	 */
	@ResponseBody
	@PostMapping("/save")
	public R save(@RequestBody Groupon groupon) {
		groupon.save();
		return R.ok();
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@PostMapping("/delete")
    public R delete(@RequestBody Object[] ids) {
        grouponService.deleteBatch(ids);
        return R.ok();
    }

}