package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLCommentData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	
	
	@Expose
	public String assessmentLevels;

	@Expose
	public String companyId;
	
	@Expose
	public String content;
	
	@Expose
	public String id;
	
	@Expose
	public String mtime;
	
	@Expose
	public MLCommentUserData user;
	
}
