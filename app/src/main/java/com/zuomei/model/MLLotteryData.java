package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class MLLotteryData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;
	
	
	@Expose
	public List<MLLotteryInfo> activitie;
	
	@Expose
	public int sorce;
	
	@Expose
	public int restrict;
}
