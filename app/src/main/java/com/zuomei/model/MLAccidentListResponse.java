package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLAccidentListResponse extends MLBaseResponse{
	
	@Expose
	public List<MLAccidentDetailData> datas;
	
}
