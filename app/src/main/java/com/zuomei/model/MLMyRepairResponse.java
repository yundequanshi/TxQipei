package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLMyRepairResponse extends MLBaseResponse{
	
	@Expose
	public List<MLMyRepairData> datas;
	
}
