package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class TXHomeCatalogResponse extends MLBaseResponse{
	
	@Expose
	public TXHomeCatalogData datas;

	public class TXHomeCatalogData {
		@Expose
		public List<MLHomeCatalogData> hotTypeList;
		@Expose
		public List<MLHomeCatalogData> typeList;
	}
}
