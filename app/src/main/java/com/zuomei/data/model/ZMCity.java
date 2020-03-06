package com.zuomei.data.model;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.zuomei.utils.MLToolUtil;
@Table(name="zm_city")
public class ZMCity {

	@Id
	public  String  id;

	@Column
	public  String  province_id;

	@Column
	public  String  sinaid;

	@Column
	public  String  name;
	
	
	public String getName(String name){
		if(MLToolUtil.isNull(name))
			return null;
		String[] str = name.split("  ");
		if(str!=null&&str.length>1)
			return str[1];
		return null;
	}
}