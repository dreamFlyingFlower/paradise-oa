package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.Apply;

import io.swagger.annotations.Api;

/**
 * 请假表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 12:11:22
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "请假表API")
@RestController
@RequestMapping("apply")
public class ApplyCrl extends AbstractCrl<Apply, Long> {

}