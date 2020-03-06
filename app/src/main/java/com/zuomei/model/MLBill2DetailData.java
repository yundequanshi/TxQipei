package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLBill2DetailData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	@Expose
	public MLDepotData company;
	
	@Expose
	public String createTime;
	
	@Expose
	public String examineTime;
	@Expose
	public String id;
	@Expose
	public String images;
	@Expose
	public String sendTime;
	@Expose
	public String state;

	@Expose
	public String money;
	
}
