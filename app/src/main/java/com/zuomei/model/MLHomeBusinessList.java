package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLHomeBusinessList extends MLBaseResponse{
	
	@Expose
	public List<MLHomeBusinessData> datas;
	
}
