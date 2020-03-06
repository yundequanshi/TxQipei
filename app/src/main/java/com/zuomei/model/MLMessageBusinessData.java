package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLMessageBusinessData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9071753735687344822L;

	@Expose
	public String companyName;
	
	@Expose
	public String logo;
}
