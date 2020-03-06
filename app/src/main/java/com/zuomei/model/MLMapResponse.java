package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLMapResponse extends MLBaseResponse{
	
	@Expose
	public List<MLMapData> datas;
	
}
