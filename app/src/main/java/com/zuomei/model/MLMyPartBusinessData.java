package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLMyPartBusinessData implements Serializable{

	private static final long serialVersionUID = -8803918152010632198L;

	@Expose
	public String id;
	
	@Expose
	public String address;
	
	@Expose
	public String majors;
	@Expose
	public String companyName;
	@Expose
	public String companyId;
	@Expose
	public String logoId;
	
	public String state;

}
