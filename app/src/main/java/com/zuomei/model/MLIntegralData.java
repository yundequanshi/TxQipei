package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLIntegralData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	
	@Expose
	public String sorce;
	@Expose
	public String signVal;
	@Expose
	public String depotName;
	@Expose
	public String userPhoto;
}
