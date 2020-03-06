package com.zuomei.model;

import com.google.gson.annotations.Expose;

public class MLUpdateData extends MLBaseResponse{
	
	@Expose  
	public String downloadPath;
	
	@Expose
	public String version;
	
	//1强制，0非强制 
	@Expose
	public String isUpdate;
}
