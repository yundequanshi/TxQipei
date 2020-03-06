package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLMyPartOfferMgResponse extends MLBaseResponse{
	
	@Expose
	public List<MLMyPartOfferMgData> datas;
	
}
