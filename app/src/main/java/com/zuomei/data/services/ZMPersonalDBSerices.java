package com.zuomei.data.services;

import android.content.Context;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.zuomei.base.BaseDBService;
import com.zuomei.data.model.ZMCity;
import com.zuomei.data.model.ZMProvince;
import com.zuomei.utils.MLToolUtil;

public class ZMPersonalDBSerices extends BaseDBService{

	public ZMPersonalDBSerices(Context context) {
		super(context);
	}
	private static ZMPersonalDBSerices INSTANCE =null;
	public static ZMPersonalDBSerices instance(Context context){
		if(INSTANCE==null){
			INSTANCE = new ZMPersonalDBSerices(context);
		}
		return INSTANCE;
	} 

	/**
	 * 根据城市id  获取 城市名
	 * @param cityId
	 * @return
	 */
	public String getLiveCity(int cityId){
		ZMCity city =null;
		StringBuilder cityName = null;
		try {
		   city = getDBUtils().findFirst((Selector.from(ZMCity.class).where(WhereBuilder.b("id", "=", cityId))));
		   if(city==null)return "";
		   ZMProvince province =  getDBUtils().findFirst((Selector.from(ZMProvince.class).where(WhereBuilder.b("id", "=", city.province_id))));
		   cityName = new StringBuilder();
		   cityName.append(province.name);
		   cityName.append(" ");
		   cityName.append(city.name);
		   return cityName.toString();
		} catch (DbException e) {
			return "";
		}
	}
	

	
	private <T> T getEntity(Class<T> cls,Object value){
		try {
			T entity = getDBUtils().findFirst((Selector.from(cls).where(WhereBuilder.b("id", "=",value))));
			return entity;
		} catch (DbException e) {
			return null;
		}
	}
	public static String getMarrayStatus(String str){
		if(!MLToolUtil.isNull(str)){
			if(str.equalsIgnoreCase("0"))
				return "未婚";
			if(str.equalsIgnoreCase("1"))
				return "已婚";
			if(str.equalsIgnoreCase("2"))
				return "离异";
			if(str.equalsIgnoreCase("3"))
				return "丧偶";
			if(str.equalsIgnoreCase("4"))
				return "保密";
		}
		return "未知";
	}

}
