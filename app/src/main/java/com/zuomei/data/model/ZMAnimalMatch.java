package com.zuomei.data.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
@Table(name="zm_animal_match")
public class ZMAnimalMatch {

	@Id
	public  String  id;

	@Column
	public  String  maleAninal;

	@Column
	public  String  femaleAnimal;

	@Column
	public  String  macthacy;

	@Column
	public  String  remark;


}