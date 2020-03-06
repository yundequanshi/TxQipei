package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class MLCommentResponse extends MLBaseResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4520680213399978115L;

	@Expose
	public List<MLCommentData> datas;
	
}
