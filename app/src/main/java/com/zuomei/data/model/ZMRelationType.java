package com.zuomei.data.model;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
@Table(name="zm_relation_type")
public class ZMRelationType {

	@Id
	public  String  relation_id;

	@Column
	public  String  name;


}