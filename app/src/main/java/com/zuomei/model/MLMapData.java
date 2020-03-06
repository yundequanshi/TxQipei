package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLMapData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4910101702857064213L;

	@Expose
	public String userID;
	
	@Expose
	public double lon;
	
	@Expose
	public	String Phone; 
	
	@Expose
	public String userName;
	
	@Expose
	public double lat;
}
