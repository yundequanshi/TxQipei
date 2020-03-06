package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLCommentUserData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	
	
	@Expose
	public String clientNumber;

	@Expose
	public String clientPwd;
	
	@Expose
	public String depotName;
	
	@Expose
	public String userPhoto;
	
}
