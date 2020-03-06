package com.zuomei.data.model;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
@Table(name="zm_constellation_match")
public class ZMConstellationMatch {

	@Id
	public  String  id;

	@Column
	public  String  man_constellation;

	@Column
	public  String  woman_constellation;

	@Column
	public  String  macthacy;

	@Column
	public  String  remark;


}