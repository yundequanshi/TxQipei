package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class MLBill2ListData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	
	@Expose
	public String count;
	@Expose
	public String score;
	@Expose
	public List<MLBill2List> list;
	
}
