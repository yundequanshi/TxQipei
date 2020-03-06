package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLHomeCityData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7005751198198729413L;

	@Expose
	public String id;
	
	@Expose
	public String cityName;

	public String sortLetters;
}
