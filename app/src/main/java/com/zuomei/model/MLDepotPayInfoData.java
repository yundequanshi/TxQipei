package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLDepotPayInfoData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	
	@Expose
	public double money;
	
	@Expose
	public double redMoney;
	
}
