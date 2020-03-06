package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

public class TXProductModel extends MLBaseResponse{
	
	@Expose
	public String title;
	
	@Expose
	public String image;
}
