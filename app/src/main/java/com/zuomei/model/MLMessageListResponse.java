package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLMessageListResponse extends MLBaseResponse{
	
	@Expose
	public List<MLMessageData> datas;
	
}
