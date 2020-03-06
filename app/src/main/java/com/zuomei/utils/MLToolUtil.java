/**
 * FlieName:LTToolUtils.java
 * Destribution:
 * Author:michael
 * 2013-5-17 下午4:04:18
 */
package com.zuomei.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zuomei.base.BaseApplication;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author michael
 * 
 */
public class MLToolUtil {

	/**
	 * Destribution： 判断字符串是否为空或者是空字符串
	 * 
	 * @param 传入的字符串
	 * @return 返回布尔值
	 */
	public static boolean isNull(String s) {

		if (s == null || s.equalsIgnoreCase(""))
			return true;
		return false;
	}
	
	public static boolean isNull(Long s) {

		if (s == null )
			return true;
		return false;
	}
  
	/**
	 * Destribution： 打印LOG信息
	 * 
	 * @param tag
	 *            TAG信息
	 * @param msg
	 *            Debug Info
	 */
	public static void DebugInfo(String tag, String msg) {
		Log.d(tag, msg);
	}

	/**
	 * Destribution： 获取资源中的字符串
	 * 
	 * @param resId
	 *            资源id
	 * @return 返回字符串
	 */
	public static String getResourceString(int resId) {
		return BaseApplication.getInstance().getString(resId);
	}

	/**
	 * Destribution：从SharePreferences中获取值
	 * 
	 * @param key
	 *            键值
	 * @return 返回保存对应键值的字符串
	 */
	/*
	 * public static String getStringFromSP(String key){ SharedPreferences sp =
	 * LTOAApplication
	 * .getInstance().getSharedPreferences(LTConstants.SETTING_INFOS, 0); return
	 * sp.getString(key, ""); }
	 *//**
	 * Destribution：从SharePreferences中获取值
	 * 
	 * @param key
	 *            键值
	 * @return 返回对应键值的布尔值
	 */
	/*
	 * public static boolean getBooleanFromSP(String key){ SharedPreferences sp
	 * =
	 * LTOAApplication.getInstance().getSharedPreferences(LTConstants.SETTING_INFOS
	 * , 0); return sp.getBoolean(key,false); }
	 * 
	 * public static void writeStringToSP(String key,String val){
	 * 
	 * 
	 * }
	 * 
	 * public static void writeBooleanToSP(String key,boolean val){
	 * SharedPreferences sp =
	 * LTOAApplication.getInstance().getSharedPreferences(
	 * LTConstants.SETTING_INFOS, 0); sp.edit().putBoolean(key, val) .commit();
	 * }
	 * 
	 * public static void saveRemember(String userName,String password,boolean
	 * isRemember){ SharedPreferences sp =
	 * LTOAApplication.getInstance().getSharedPreferences(
	 * LTConstants.SETTING_INFOS, 0); if (isRemember) {
	 * sp.edit().putBoolean(LTConstants.SETTING_ISREMEMBER,isRemember)
	 * .putString(LTConstants.SETTING_USERNAME,userName)
	 * .putString(LTConstants.SETTING_PASSWORD, password) .commit(); }else{
	 * sp.edit().putBoolean(LTConstants.SETTING_ISREMEMBER,isRemember)
	 * .putString(LTConstants.SETTING_USERNAME,"")
	 * .putString(LTConstants.SETTING_PASSWORD, "") .commit();
	 * 
	 * }
	 * 
	 * }
	 */

	public static String getTime() {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);// 获取年份
		int month = ca.get(Calendar.MONTH);// 获取月份
		int day = ca.get(Calendar.DATE);// 获取日
		int minute = ca.get(Calendar.MINUTE);// 分
		int hour = ca.get(Calendar.HOUR);// 小时
		int second = ca.get(Calendar.SECOND);// 秒
		return String.format("%d%d%d%d%d%d", year, month, day, minute, hour,
				second);
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static String MD5(String original) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(original.getBytes("UTF-8"));
		} catch (Exception e) {
			return "";
		}
	
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString().toLowerCase();
	}
/**
 *   手机号验证
 * @param str 
 * @return
 */
	public static boolean isCellphone(String str) {
		String s = "^((13[0-9])|(15[0-9])|(18[0-9])|(14[57]))\\d{8}$";
		return str.matches(s);
	}
	
	

	
	public static int createAge(String birthday){
		if(birthday==null)return 0;
		long d = Long.parseLong(birthday);
		Date data = new Date(d);
		Calendar c=Calendar.getInstance();
		c.setTime(data);
		int dy =c.get(Calendar.YEAR);
		int dm =c.get(Calendar.MONTH)+1; 
		Calendar now=Calendar.getInstance();
		int nowy = now.get(Calendar.YEAR);
		int nowm = now.get(Calendar.MONTH)+1;
		float m= (nowy - dy +(nowm-dm)/12f);
		int t = Integer.parseInt(String.valueOf(new BigDecimal(m).setScale(0, BigDecimal.ROUND_HALF_UP)));
		return t;
	}
/**
 *  获取经纬度
 */
	
	public static void getLocation(Context context){
		double latitude=0.0;  
		double longitude =0.0;  
		  
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);  
		        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
		            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  
		            if(location != null){  
		                latitude = location.getLatitude();  
		                longitude = location.getLongitude();  
		                }  
		        }else{  
		            LocationListener locationListener = new LocationListener() {  
		                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数  
		                @Override  
		                public void onStatusChanged(String provider, int status, Bundle extras) {  
		                }  
		                // Provider被enable时触发此函数，比如GPS被打开  
		                @Override  
		                public void onProviderEnabled(String provider) {  
		                }  
		                  
		                // Provider被disable时触发此函数，比如GPS被关闭   
		                @Override  
		                public void onProviderDisabled(String provider) {  
		                }  
		                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发   
		                @Override  
		                public void onLocationChanged(Location location) {  
		                    if (location != null) {     
		                        Log.e("Map", "Location changed : Lat: "    
		                        + location.getLatitude() + " Lng: "    
		                        + location.getLongitude());     
		                    }  
		                }  
		            };  
		            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000, 0,locationListener);     
		            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);     
		            if(location != null){     
		                latitude = location.getLatitude(); //经度     
		                longitude = location.getLongitude(); //纬度  
		            }     
		        }  
	}
	
	
	/**
	 * 根据用户生日计算年龄
	 */
	public static int getAgeByBirthday(String data) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟  
		Date birthday = null;
		try {
			birthday = sdf.parse(data);
		} catch (ParseException e) {
			return 0 ;
		}
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		}
		return age;
	}
	public static final String[] zodiacArr = { "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊" };   
	  
	public static final String[] constellationArr = { "水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",   
	        "天蝎座", "射手座", "魔羯座" };   
	  
	public static final int[] constellationEdgeDay = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };   
	  
	/**  
	 * 根据日期获取生肖  
	 * @return  
	 */  
	public static String getZodica(String date) {   
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			return "";
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
	    return zodiacArr[calendar.get(Calendar.YEAR) % 12];   
	}   
	  
	/**  
	 * 根据日期获取星座  
	 * @param time  
	 * @return  
	 */  
	public static String getConstellation(String date) {   
		
		
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			return "";
		}
		Calendar time = Calendar.getInstance();
		time.setTime(d);
		
	    int month = time.get(Calendar.MONTH);   
	    int day = time.get(Calendar.DAY_OF_MONTH);   
	    if (day < constellationEdgeDay[month]) {   
	        month = month - 1;   
	    }   
	    if (month >= 0) {   
	        return constellationArr[month];   
	    }   
	    return constellationArr[11];   
	}  
	
	 /**
     * 检测网络是否可用
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * 获取APP版本名称
      * @description  
      *
      * @author marcello
     */
    public static  String getVersionName(Context context)
    {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo;
			try {
				packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
			    String version = packInfo.versionName;
	            return version;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "0";
			}
        
    }
    
    /**
     * 获取APP版本号
      * @description  
      *
      * @author marcello
     */
    public static  int getVersionCode(Context context)
    {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo;
			try {
				packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
			    int version = packInfo.versionCode;
	            return version;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 0;
			}
        
    }
    
    /**
     * 获取当前网络类型
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    public static final int NETTYPE_3G = 0x04;
    public static int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }        
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if(!isNull(extraInfo)){
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else if(extraInfo.toLowerCase().equals("3gnet")){
                    netType =NETTYPE_3G;
                }else{
                	 netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }
    /**
     * 隐藏键盘
     * 如果输入法在窗口上已经显示，则隐藏，反之则显示
      * @description  
      *
      * @author marcello
     */
    public static void hideKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
	
    public  static Bitmap readBitMap(Context context,int resId){  
	      BitmapFactory.Options opt = new BitmapFactory.Options();  
	      opt.inPreferredConfig = Bitmap.Config.RGB_565;   
	      opt.inPurgeable = true;  
	      opt.inInputShareable = true;  
	       //获取资源图片  
	     InputStream is = context.getResources().openRawResource(resId);  
	        return BitmapFactory.decodeStream(is,null,opt);  
}
    
    
    /**
     *   DialogInterface 点击后保存对话框
      * @description  
      *
      * @author marcello
     */
    
    public static void keepDialog(DialogInterface dialog) {  
        try {  
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");  
            field.setAccessible(true);  
            field.set(dialog, false);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    /**
     *  点击后让对话框消失
      * @description  
      *
      * @author marcello
     */
    public static void distoryDialog(DialogInterface dialog){  
        try {  
            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");  
            field.setAccessible(true);  
            field.set(dialog, true);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }   
    }  
    
    /**
     *  解决scrollview 和 listView 冲突
      * @description  
      *
      * @author marcello
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            // pre-condition  
            return;  
        }  
  
        int totalHeight = 0;  
        int size = listAdapter.getCount();
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
    }  
}
