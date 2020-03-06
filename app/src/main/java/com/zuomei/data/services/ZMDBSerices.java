package com.zuomei.data.services;

import android.content.Context;

import com.lidroid.xutils.exception.DbException;
import com.zuomei.base.BaseDBService;
import com.zuomei.utils.MLToolUtil;

import java.util.ArrayList;
import java.util.List;

public class ZMDBSerices extends BaseDBService{

	public ZMDBSerices(Context context) {
		super(context);
	}
	private static ZMDBSerices INSTANCE =null;
	public static ZMDBSerices instance(Context context){
		if(INSTANCE==null){
			INSTANCE = new ZMDBSerices(context);
		}
		return INSTANCE;
	} 
	
	/**
	 * 增 ，改
	 * @param entity
	 */
	public <T> void saveOrUpdate(T entity){
		try {
			db.saveOrUpdate(entity);
			MLToolUtil.DebugInfo("",
					"=============插入成功===================================");
		} catch (DbException e) {
			MLToolUtil.DebugInfo("",
					"=============插入失败===================================");
			return;
		}
	}
	
	/**
	 * 查询
	 * @param entity
	 */
/*	public <T> T getFirst(T entity){
		T result =null;
		try {
			result = db.findFirst(entity);
		} catch (DbException e) {
			ZMToolUtil.DebugInfo("",
					"=============查询失败===================================");
			return result;
		}
		return result;
	}*/
	
	/**
	 * 查询全部
	 * @param entity
	 */
	public <T> List<T> getAll(Class<T> entity){
		 List<T> result = new ArrayList<T>();
		try {
			result = db.findAll(entity);
		} catch (DbException e) {
			MLToolUtil.DebugInfo("",
					"=============查询失败===================================");
			return result;
		}
		 return result;
	}
	/**
	 *  条件查询
	 * @param entity
	 * @return
	 */
/*	public <T> List<T> getAll(T entity){
		 List<T> result = new ArrayList<T>();
		try {
			result = db.findAll(entity);
		} catch (DbException e) {
			ZMToolUtil.DebugInfo("",
					"=============查询失败===================================");
			return result;
		}
		 return result;
	}*/
	/**
	 * 删除
	 * @param entity
	 */
	public <T> void delete(T entity){
		try {
			db.delete(entity);
			MLToolUtil.DebugInfo("",
					"=============删除成功===================================");
		} catch (DbException e) {
			MLToolUtil.DebugInfo("",
					"=============删除失败===================================");
			return;
		}
	}
}
