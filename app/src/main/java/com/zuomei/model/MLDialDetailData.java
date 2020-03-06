package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLDialDetailData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	
	@Expose
	public String companyPhone;
	@Expose
	public String depotPhone;
	@Expose
	public String id;
	@Expose
	public String isNetWorkPhone;
	@Expose
	public String mtime;
	@Expose
	public String timelength;
	
}
