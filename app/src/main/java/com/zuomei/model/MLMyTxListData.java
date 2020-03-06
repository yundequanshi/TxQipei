package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLMyTxListData extends MLBaseResponse{
	
	@Expose
	public String balance;

	@Expose
	public String logo;
	
	@Expose
	public String minMoneyUser;
	
	@Expose
	public String name;
	
	@Expose
	public String telephoneFare;
	
	@Expose
	public List<MLMyTxCashData> withDrawCash;
}
