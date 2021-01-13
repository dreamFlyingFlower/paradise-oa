package com.wy.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

import com.wy.utils.StrUtils;

/**
 * xss非法标签工具类
 * 
 * @author ParadiseWY
 * @date 2020年4月6日 下午8:35:25
 */
public class JsoupUtils {

	/**
	 * 使用自带的basicWithImages白名单,允许的标签有a,a标签的href,img,img标签的src,
	 * b,blockquote,br,cite,code,dd,dl,dt,em,i,li,ol,p,pre,q,small,span,
	 * strike,strong,sub,sup,u,ul以及align,alt,height,width,title属性
	 */
	private static final Whitelist WHITE_LIST = Whitelist.basicWithImages();

	/** 配置过滤化参数,不对代码进行格式化 */
	private static final Document.OutputSettings OUTPUT_SETTINGS = new Document.OutputSettings().prettyPrint(false);
	static {
		// 富文本编辑时一些样式是使用style来进行实现的,比如红色字体 style="color:red;",所以需要给所有标签添加style属性
		WHITE_LIST.addAttributes(":all", "style");
	}

	public static String clean(String content) {
		if (StrUtils.isNotBlank(content)) {
			content = content.trim();
		}
		return Jsoup.clean(content, "", WHITE_LIST, OUTPUT_SETTINGS);
	}
}