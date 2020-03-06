package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLLeaveDetailData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;
	
	@Expose
	public String carType;
	@Expose
	public String childType;
	@Expose
	public String cityName;
	@Expose
	public String currentCost;
	@Expose
	public String exhaust;
	@Expose
	public String id;
	@Expose
	public String images;
	@Expose
	public String introduction;
	@Expose
	public String mobile;
	@Expose
	public String name;
	@Expose
	public String originalCost;
	@Expose
	public String quality;
	@Expose
	public String user;
	@Expose
	public String createTime;
	
}
