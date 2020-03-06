package com.zuomei.base;

import android.content.Context;
import android.os.Environment;

import com.lidroid.xutils.DbUtils;
public class BaseDBService {
		
	protected DbUtils db;
	private Context _context;
	public BaseDBService(Context context){
		_context = context;
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		 db = DbUtils.create(_context,path+"/","zm.db");
	     db.configAllowTransaction(true);
	     db.configDebug(true);
	}
	public DbUtils getDBUtils(){
		return db;
	}
	
	
	/*
	 * 
	
	// 增,改
	public <T> void saveOrUpdate(T entity) throws DbException{
		db.saveOrUpdate(entity);
	}
	// 删
	public <T> void delete(T entity) throws DbException{
		db.delete(entity);
	}
	// 查
	public <T> T getFirst(T entity) throws DbException{
		T result = db.findFirst(entity);
		return result;
	}
	public <T> List<T> getAll(T entity) throws DbException{
		 List<T> result = db.findAll(entity);
		 return result;
	}
	
	*/
}
