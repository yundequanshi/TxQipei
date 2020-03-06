package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLHomeAdResponse extends MLBaseResponse{
	
	@Expose
	public List<MLHomeAdData> datas;
	
}
