package com.zuomei.data.model;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
@Table(name="zm_religion")
public class ZMReligion {

	@Id
	public  String  id;

	@Column
	public  String  name;


}