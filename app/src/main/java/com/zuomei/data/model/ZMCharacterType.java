package com.zuomei.data.model;

import com.google.gson.annotations.Expose;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;
@Table(name="zm_character_type")
public class ZMCharacterType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1605547445134427549L;

	@Id
	@Expose
	public  String  id;

	@Column
	@Expose
	public  String  name;


}