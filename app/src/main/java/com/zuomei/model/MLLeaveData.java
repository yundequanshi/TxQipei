package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLLeaveData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -140859426084441999L;

/*	@Expose
	public MLDepotData user;*/
	
	@Expose
	public String userType;
	
	@Expose
	public MLLeaveInfo info;
	
	
	
}
