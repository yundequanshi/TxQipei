package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class MLMyPartDetailData implements Serializable{

	private static final long serialVersionUID = -8803918152010632198L;

	@Expose
	public String id;
	@Expose
	public String remark;
	@Expose
	public String cremark;
	@Expose
	public String type;
	@Expose
	public String childType;
	@Expose
	public String carNum;
	@Expose
	public String particularYear;
	@Expose
	public String name;
	
	@Expose

	public List<MLAccidentQuotesData> accidentQuotes;
}
