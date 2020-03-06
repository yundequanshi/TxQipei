package com.zuomei.data.model;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
@Table(name="zm_fiveelements_match")
public class ZMFiveelementsMatch {

	@Id
	public  String  id;

	@Column
	public  String  man_fiveelements;

	@Column
	public  String  woman_fiveelements;

	@Column
	public  String  macthacy;

	@Column
	public  String  remark;


}