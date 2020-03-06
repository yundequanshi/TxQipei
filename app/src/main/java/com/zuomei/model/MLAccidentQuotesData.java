package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLAccidentQuotesData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	@Expose
	public String id;;
	@Expose
	public String deputyFactoryPrice="0";
	@Expose
	public String num;
	@Expose
	public String remark;
	@Expose
	public String name;
	@Expose
	public String factoryPrice="0";
}
