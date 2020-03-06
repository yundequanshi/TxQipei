package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLLeaveResponse extends MLBaseResponse{
	
	@Expose
	public List<MLLeaveData> datas;
	
}
