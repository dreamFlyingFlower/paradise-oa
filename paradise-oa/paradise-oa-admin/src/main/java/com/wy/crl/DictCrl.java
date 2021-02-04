package com.wy.crl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.Dict;
import com.wy.result.Result;
import com.wy.service.DictService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 系统字典类API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "系统字典类API")
@RestController
@RequestMapping("dict")
public class DictCrl extends AbstractCrl<Dict, Long> {

	@Autowired
	private DictService dictService;

	@ApiOperation("根据上级dictId获取上级以及直接下级数据")
	@GetMapping("getSelfChildren/{dictId}")
	public Result<?> getSelfChildren(@ApiParam("上级字典编号") @PathVariable Long dictId) {
		return Result.ok(dictService.getSelfChildren(dictId));
	}

	@ApiOperation("根据上级dictCode获取直接下级数据或上级以及直接下级数据")
	@GetMapping("getChildrenByCode/{dictCode}")
	public Result<?> getChildrenByCode(@ApiParam("上级字典编码") @PathVariable String dictCode,
			@ApiParam("是否获得上级数据,null或false不获取,true获取") Boolean self) {
		return Result.ok(dictService.getChildrenByCode(dictCode,self));
	}
}