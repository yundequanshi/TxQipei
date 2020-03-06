package com.zuomei.model;

import com.google.gson.annotations.Expose;

public class MLMyUserData extends MLBaseResponse{
	
	@Expose
	public String id;
	@Expose
	public String name;
	@Expose
	public String phone;
	@Expose
	public String isCompany;
	
}
