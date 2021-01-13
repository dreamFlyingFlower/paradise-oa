package com.wy.crl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wy.base.AbstractCrl;
import com.wy.model.Button;

import io.swagger.annotations.Api;

/**
 * 菜单按钮表API
 * 
 * @author 飞花梦影
 * @date 2021-01-13 09:43:43
 * @git {@link https://github.com/mygodness100}
 */
@Api(tags = "菜单按钮表API")
@RestController
@RequestMapping("button")
public class ButtonCrl extends AbstractCrl<Button, Long> {

}