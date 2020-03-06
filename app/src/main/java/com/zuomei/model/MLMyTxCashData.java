package com.zuomei.model;

import com.google.gson.annotations.Expose;

public class MLMyTxCashData extends MLBaseResponse{
	
	@Expose
	public String id;

	@Expose
	public String createTime;
	
	@Expose
	public String forCashId;
	
	@Expose
	public String dealFailInfo;
	
	@Expose
	public String monery;
	
	@Expose
	public String dealType;
	
	@Expose
	public String cashStatus;

	
}
