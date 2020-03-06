package com.easemob.easeui.utils;

import com.easemob.easeui.controller.EaseUI;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

public class MLDBUtils {
		
	protected static DbUtils db;
	
	private static void initDB() {
		db = EaseUI.db;
	}
	
	// 增,改
	public static <T> void saveOrUpdate(T entity){
		initDB();
		try {
			db.saveOrUpdate(entity);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static <T> void saveOrUpdate(List<T> entity){
		initDB();
		try {
			for(T t : entity){
				db.saveOrUpdate(t);
			}
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 删
	public  static <T> void delete(T entity){
		initDB();
		try {
			db.delete(entity);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	// 查
	public  static <T> T getFirst(Class<T> entity){
		initDB();
		T result;
		try {
			result = (T) db.findFirst(entity);
		} catch (DbException e) {
			return null;
		}
		return result;
	}
	
	public  static <T> T getFirst(Selector selector){
		initDB();
		T result;
		try {
			result = (T) db.findFirst(selector);
		} catch (DbException e) {
			return null;
		}
		return result;
	}
	
	public  static <T> List<T> getAll(Class<T> entity){
		initDB();
		 List<T> result;
		try {
			result = db.findAll(entity);
		} catch (DbException e) {
			return null;
		}
		 return result;
	}
	
	public  static <T> List<T> getAll(Selector select){
		initDB();
		 List<T> result;
		try {
			result = db.findAll(select);
		} catch (DbException e) {
			return null;
		}
		 return result;
	}
}
