package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLDealListResponse extends MLBaseResponse{
	
	@Expose
	public List<MLDealListData> datas;
	
}
