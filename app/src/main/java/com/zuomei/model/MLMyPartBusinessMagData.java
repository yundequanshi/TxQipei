package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLMyPartBusinessMagData implements Serializable{
	
	private static final long serialVersionUID = 3079226482468613286L;
	
	@Expose
	public String id;
	@Expose
	public String num;
	@Expose
	public String name;
	@Expose
	public String state;
	@Expose
	public String childType;
	@Expose
	public String type;
	
	@Expose
	public String time;
	
	@Expose
	public String particularYear;
	
	
}
