package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLImage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3265054291378604432L;
	@Expose
	public String id;
	
	@Expose
	public String name;
	
	
	@Expose
	public String status;
	
	@Expose
	public String uploadpath;
}
