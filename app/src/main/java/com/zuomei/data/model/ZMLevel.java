package com.zuomei.data.model;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
@Table(name="zm_level")
public class ZMLevel {

	@Id
	public  String  level_id;

	@Column
	public  String  name;

	@Column
	public  String  imageurl;

	@Column
	public  String  integral;


}