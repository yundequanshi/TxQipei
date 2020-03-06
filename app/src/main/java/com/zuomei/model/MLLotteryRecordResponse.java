package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLLotteryRecordResponse extends MLBaseResponse{
	
	@Expose
	public List<MLLotteryRecordData> datas;
	
}
