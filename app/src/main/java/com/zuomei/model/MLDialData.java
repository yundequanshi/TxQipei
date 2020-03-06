package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLDialData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	@Expose
	public String id;

	@Expose
	public String logo;
	
	@Expose
	public String phoneNum;
	
	
	@Expose
	public String isNetWorkPhone;
	
	
	@Expose
	public String timeLength;
	
	
	@Expose
	public String dialPhone;
	
	@Expose
	public String userId;
	
	@Expose
	public String userName;

	@Expose
	public String creatTime;
	
	
	public String startDate;
	public String endDate;
}
