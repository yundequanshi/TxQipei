package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLHomeCatalogData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8337224921705988278L;

	
	public  String sortLetters;  //显示数据拼音的首字母
	
	@Expose
	public String id;
	
	@Expose
	public String isLogistics;
	
	@Expose
	public String imageId;
	
	@Expose
	public String name;
	
	@Expose
	public String status;
	
	@Expose
	public String superId;

	public String searchKeyWord="";
}
