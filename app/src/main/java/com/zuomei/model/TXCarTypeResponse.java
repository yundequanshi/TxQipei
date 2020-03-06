package com.zuomei.model;

import com.google.gson.annotations.Expose;
import java.util.List;

public class TXCarTypeResponse extends MLBaseResponse{

	@Expose
	public List<MLHomeCatalogData> datas;
}
