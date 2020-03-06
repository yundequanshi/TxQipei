package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MLMyBankResponse extends MLBaseResponse{
	
	@Expose
	public List<MLMyBnakData> datas;
	
}
