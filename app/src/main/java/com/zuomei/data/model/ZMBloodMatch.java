package com.zuomei.data.model;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
@Table(name="zm_blood_match")
public class ZMBloodMatch {

	@Id
	public  String  id;

	@Column
	public  String  maleblood;

	@Column
	public  String  femaleblood;

	@Column
	public  String  macthacy;

	@Column
	public  String  remark;


}