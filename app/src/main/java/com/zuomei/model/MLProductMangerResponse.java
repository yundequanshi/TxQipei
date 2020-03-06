package com.zuomei.model;

import com.google.gson.annotations.Expose;
import java.util.List;

public class MLProductMangerResponse extends MLBaseResponse{
	
	@Expose
	public List<ProMangerData> datas;
	
}
