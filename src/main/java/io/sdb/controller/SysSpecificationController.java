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
import io.sdb.common.utils.R;
import io.sdb.common.utils.TreeBuilder;
import io.sdb.model.Specification;
import io.sdb.service.SpecificationService;
import io.sdb.vo.SpecificationVO;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sys/specification")
public class SysSpecificationController extends AbstractController{
	@Autowired
	private SpecificationService specificationService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/tree")
	public R tree(){
		List<Specification> specificationList = specificationService.findAll();

		List<SpecificationVO> specificationVOList = specificationList.stream().map(item -> {
			SpecificationVO specificationVO = new SpecificationVO(item);
			return specificationVO;
		}).collect(Collectors.toList());
		List<SpecificationVO> specificationTree = new TreeBuilder().buildTree(specificationVOList);

		return R.ok().put("specificationTree", specificationTree);
	}

	/**
	 * 列表
	 */
	@ResponseBody
	@GetMapping("/list")
	public R list(){
		List<Specification> specificationList = specificationService.findAll();

		/*List<SpecificationVO> specificationVOList = specificationList.stream().map(item -> {
			SpecificationVO specificationVO = new SpecificationVO(item);
			return specificationVO;
		}).collect(Collectors.toList());*/


		return R.ok().put("specificationList", specificationList);
	}

	/**
	 * 规格信息
	 */
	@GetMapping("/info/{id}")
	@RequiresPermissions("sys:specification:info")
	public R info(@PathVariable("id") Long id){
		Specification specification = specificationService.findById(id);
		return R.ok().put("specification", specification);
	}

	/**
	 * 选择规格(添加、修改规格)
	 */
	@GetMapping("/select")
	@RequiresPermissions("sys:specification:select")
	public R select(){
		//查询列表数据
		List<Specification> specificationList = specificationService.queryListByOrder();

		return R.ok().put("specificationList", specificationList);
	}

	/**
	 * 保存
	 */
	@SysLog("保存规格")
	@PostMapping("/save")
	@RequiresPermissions("sys:specification:save")
	public R save(@RequestBody Specification specification){
		//数据校验
		verifyForm(specification);

		return specificationService.save(specification);

	}

	/**
	 * 修改
	 */
	@SysLog("修改规格")
	@PostMapping("/update")
	@RequiresPermissions("sys:specification:update")
	public R update(@RequestBody Specification specification){
		//数据校验
		verifyForm(specification);
		specificationService.update(specification);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@SysLog("删除规格")
	@PostMapping("/delete/{id}")
	@RequiresPermissions("sys:specification:delete")
	public R delete(@PathVariable("id") long id){
		return specificationService.delete(id);
	}

	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(Specification specification){
		if(StringUtils.isBlank(specification.getName())){
			throw new RRException("规格名称不能为空");
		}

		/*if(specification.getParentId() == null){
			throw new RRException("上级规格不能为空");
		}*/

		if(specification.getType() == null){
			throw new RRException("规格类型不正确");
		}
	}

}