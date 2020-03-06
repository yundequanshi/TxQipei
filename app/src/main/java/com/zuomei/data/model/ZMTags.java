package com.zuomei.data.model;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
@Table(name="zm_tags")
public class ZMTags {

	@Id
	public  String  tag_id;

	@Column
	public  String  tag_content;


}