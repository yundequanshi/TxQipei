package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLLotteryInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;
	
	@Expose
	public String id;
	
	@Expose
	public String name;
	
	@Expose
	public double percentage;
	
	@Expose
	public String value;

	public int getPercentage() {
		int i = (int) (percentage*100);
		return i;
	}
	
	
}
