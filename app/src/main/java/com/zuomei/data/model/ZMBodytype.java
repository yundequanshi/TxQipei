package com.zuomei.data.model;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
@Table(name="zm_bodytype")
public class ZMBodytype {

	@Id
	public  String  id;

	@Column
	public  String  gender;

	@Column
	public  String  name;


}