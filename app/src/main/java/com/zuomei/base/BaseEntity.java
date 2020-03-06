package com.zuomei.base;

import com.lidroid.xutils.db.annotation.Id;

import java.io.Serializable;

public class BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	public String Pid;
}
