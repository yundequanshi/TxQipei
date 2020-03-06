package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class MLAccidentInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;
	
@Expose
	public String MCtime;
	@Expose
	public String accidentName;
	
	@Expose
	public String city;

	@Expose
	public String companyId;
	
	@Expose
	public String companyLogo;
	
	@Expose
	public String damaged;
	
	@Expose
	public String depotLogo;
	
	@Expose
	public String displacement;
	@Expose
	public String id;
	@Expose
	public String image;
	@Expose
	public String masterContent;
	@Expose
	public String masterName;
	@Expose
	public String masterPhone;
	@Expose
	public String mileage;
	
	@Expose
	public String oldPrice;
	@Expose
	public String platedata;
	
	@Expose
	public String price;
	@Expose
	public String userId;
	
	public List<String> paths;
	
	@Expose
	public String state;
	
}
