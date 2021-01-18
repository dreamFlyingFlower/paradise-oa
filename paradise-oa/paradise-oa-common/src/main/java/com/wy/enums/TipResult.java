package com.wy.enums;

import com.wy.common.TipCode;

/**
 * 提示信息,后期接入国际化
 * 
 * @author 飞花梦影
 * @date 2021-01-18 13:38:08
 * @git {@link https://github.com/mygodness100}
 */
public enum TipResult implements TipCode {

	UPLOAD_FILE_STREAM_NOT_FOUND(0, "uploadFileStreamNotFound"),
	UPLOAD_FILE_NAME_NOT_EXIST(0, "uploadFileNameNotExist"),
	UPLOAD_FILE_FAILED(0, "uploadFileFailed"),
	UPLOAD_FILE_NAME_OVER(0, "uploadFileNameOver");

	private int code;

	private String msg;

	private TipResult(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getMsg() {
		return msg;
	}
}