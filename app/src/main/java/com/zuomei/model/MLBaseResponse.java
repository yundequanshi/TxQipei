package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class MLBaseResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4520680213399978115L;

	@Expose
	public String state;
	
	public String message;
}
