package com.wy.crl;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.annotation.Log;
import com.wy.base.AbstractCrl;
import com.wy.enums.BusinessType;
import com.wy.excel.ExcelModelUtils;
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

	@ApiOperation("根据父级dicId获取父级以及直接子级数据")
	@GetMapping("getSelfChildren/{dicId}")
	public Result<?> getSelfChildren(@ApiParam("父级字典编号") @PathVariable Long dicId) {
		return Result.ok(dictService.getSelfChildren(dicId));
	}

	@ApiOperation("根据父级dicCode获取直接子级数据")
	@GetMapping("getChildren/{dicCode}")
	public Result<?> getChildren(@ApiParam("父级字典编码") @PathVariable String dicCode) {
		return Result.ok(dictService.getChildren(dicCode));
	}

	@PreAuthorize("@ss.hasPermi('system:dict:list')")
	@GetMapping("/list")
	public Result<?> list(Dict dictData) {
		return dictService.selectDictList(dictData);
	}

	@Log(value = "字典数据", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('system:dict:export')")
	@GetMapping("/export")
	public Result<?> export(Dict dictData, HttpServletResponse response) {
		List<Dict> list = dictService.selectDictList(dictData).getData();
		ExcelModelUtils.getInstance().exportExcel(list, response, "字典数据.xlsx");
		return Result.ok();
	}

	/**
	 * 根据字典类型查询字典数据信息
	 */
	@GetMapping(value = "/dictType/{dictType}")
	public Result<?> dictType(@PathVariable String dictType) {
		return Result.ok(dictService.selectDictDataByType(dictType));
	}
}