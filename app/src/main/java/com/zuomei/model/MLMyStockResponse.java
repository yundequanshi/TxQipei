package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLMyStockResponse extends MLBaseResponse{
	
	@Expose
	public List<MLMyStockData> datas;
	
}
