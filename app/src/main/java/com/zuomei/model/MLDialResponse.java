package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLDialResponse extends MLBaseResponse{
	
	@Expose
	public List<MLDialData> datas;
	
}
