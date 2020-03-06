package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLMyPacketData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3079226482468613286L;

	@Expose
	public String tradingLimit;
	@Expose
	public String redEnvelopeMoney;
	@Expose
	public String redEnvelopeNum;
}
