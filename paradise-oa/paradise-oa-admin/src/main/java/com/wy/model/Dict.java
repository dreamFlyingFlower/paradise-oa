package com.wy.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.wy.base.AbstractModel;
import com.wy.database.Pid;
import com.wy.database.Pri;
import com.wy.database.Sort;
import com.wy.database.Unique;
import com.wy.excel.ExcelColumn;
import com.wy.valid.ValidEdit;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 系统字典类 ts_dict
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@ApiModel(description = "系统字典类 ts_dict")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Dict extends AbstractModel {

	private static final long serialVersionUID = 1L;

	/**
	 * 字典编号
	 */
	@ApiModelProperty("字典编号")
	@ExcelColumn("字典编号")
	@NotNull(groups = ValidEdit.class)
	@Pri
	private Long dictId;

	/**
	 * 字典名
	 */
	@ApiModelProperty("字典名")
	@ExcelColumn("字典名称")
	@NotBlank(message = "字典名称不能为空")
	@Size(min = 0, max = 50, message = "字典名称长度不能超过50个字符")
	private String dictName;

	/**
	 * 字典编码,唯一
	 */
	@ApiModelProperty("字典编码,唯一")
	@ExcelColumn("字典唯一标识")
	@NotBlank(message = "字典唯一标识不能为空")
	@Size(min = 0, max = 50, message = "字典唯一标识长度不能超过50个字符")
	@Unique
	private String dictCode;

	/**
	 * 字典值
	 */
	@ApiModelProperty("字典值")
	@ExcelColumn("字典键值")
	@NotBlank(message = "字典键值不能为空")
	@Size(min = 0, max = 50, message = "字典键值长度不能超过50个字符")
	private Integer dictVal;

	/**
	 * 上级字典编号
	 */
	@ApiModelProperty("上级字典编号")
	@ExcelColumn("父级字典编号")
	@NotNull(message = "父级字典编号不能为空")
	@Pid
	private Long pid;

	/**
	 * 上级字典名
	 */
	@ApiModelProperty("上级字典名")
	private String pname;

	/**
	 * 排序
	 */
	@ApiModelProperty("排序")
	@Sort
	private Integer sortIndex;

	/** 非数据库字段 */
}