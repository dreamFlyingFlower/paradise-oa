package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.Salary;

import io.swagger.annotations.Api;

/**
 * 工资表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "工资表API")
@RestController
@RequestMapping("salary")
public class SalaryCrl extends AbstractCrl<Salary, Long> {

}