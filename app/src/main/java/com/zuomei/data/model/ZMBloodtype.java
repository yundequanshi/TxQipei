package com.zuomei.data.model;

import com.google.gson.annotations.Expose;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
@Table(name="zm_bloodtype")
public class ZMBloodtype implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1708339506359007130L;

	@Id
	@Expose
	public  String  id;

	@Column
	@Expose
	public  String  name;


}