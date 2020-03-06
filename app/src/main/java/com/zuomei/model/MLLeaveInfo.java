package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLLeaveInfo  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2365435882548195675L;
	@Expose
	public String carType;
	@Expose
	public String childType;
	@Expose
	public MLHomeCityData city;
	@Expose
	public String cityName;
	@Expose
	public String createTime;
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
	public String state;
	@Expose
	public String user;
	
}
