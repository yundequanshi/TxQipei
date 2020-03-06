package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLLotteryRecordData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	
	
	@Expose
	public String awardName;

	@Expose
	public String createTime;
	
	@Expose
	public String id;
	
	@Expose
	public String val;
	
}
