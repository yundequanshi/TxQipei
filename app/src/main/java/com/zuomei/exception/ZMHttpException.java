/**
 * FlieName:LTHttpException.java
 * Destribution:
 * Author:michael
 * 2013-5-17 下午4:07:03
 */
package com.zuomei.exception;

import com.zuomei.utils.MLToolUtil;

/**
 * @author michael
 *
 */
public class ZMHttpException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2199603193956026137L;

	
	public ZMHttpException(String detailMessage) {
		super(detailMessage);
	}


	@Override
	public String getMessage() {
		if(MLToolUtil.isNull(super.getMessage()))
			return "请求服务器异常：未知错误！";
		else
			return String.format("请求服务器异常：%s", super.getMessage());
	}
}
