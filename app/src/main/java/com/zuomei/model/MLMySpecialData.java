package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLMySpecialData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3575463679999420536L;
	
	@Expose
	public String id;
	
	@Expose
	public String time;
	
	@Expose
	public String title;
	
}
