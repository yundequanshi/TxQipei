package com.zuomei.model;

import com.google.gson.annotations.Expose;

public class MLMyRebateData extends MLBaseResponse{
	
	@Expose
	public String userId;
	
	@Expose
	public String userLogo;
	
	@Expose
	public String userName;
	
	@Expose
	public String rebateTime;
	
	@Expose
	public String rebateMoney;
	
	@Expose
	public String rebateId;
}
