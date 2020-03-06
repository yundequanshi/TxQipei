package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLAccidentDetailData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	@Expose
	public MLDepotData user;
	
	//1是汽修厂   2是商家
	@Expose
	public String userType;
	
	@Expose
	public MLAccidentInfo info;
	
}
