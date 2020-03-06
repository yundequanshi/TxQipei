package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLMyBnakData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	@Expose
	public String companyId;

	@Expose
	public String openBank;
	@Expose
	public String subsidiaryBank;
	@Expose
	public String card;
	@Expose
	public String userName;
}
