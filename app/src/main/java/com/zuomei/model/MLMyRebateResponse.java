package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLMyRebateResponse extends MLBaseResponse{
	
	@Expose
	public List<MLMyRebateData> datas;
	
}
