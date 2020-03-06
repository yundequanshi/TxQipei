package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class MLLeaveDetail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	
	
	@Expose
	public String nice;
	@Expose
	public String type;
	@Expose
	public String child;
	@Expose
	public String quality;
	@Expose
	public String city;
	@Expose
	public String displacement;
	@Expose
	public String price;
	@Expose
	public String oldPrice;
	@Expose
	public String masterName;
	@Expose
	public String masterPhone;

	public List<String> paths;
}
