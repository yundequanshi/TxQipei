package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLMyRepairData {

	@Expose
	public String id;

	@Expose
	public String carNum;
	
	@Expose
	public String cost;
	
	@Expose
	public String mtime;
	
	@Expose
	public String phone;

	@Expose
	public String userId;
	
	@Expose
	 public List<MLMyRepairDetail> breakdownDetail;
}
