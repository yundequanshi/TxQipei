/**
 * FlieName:LTToolUtils.java
 * Destribution:
 * Author:michael
 * 2013-5-17 下午4:04:18
 */
package cn.ml.base.utils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import cn.ml.base.MLApplication;

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
/*	public static boolean isNull(String s) {

		if (s == null || s.equalsIgnoreCase(""))
			return true;
		return false;
	}
	
	public static boolean isNull(Long s) {

		if (s == null )
			return true;
		return false;
	}
  
	*//**
	 * Destribution： 打印LOG信息
	 * 
	 * @param tag
	 *            TAG信息
	 * @param msg
	 *            Debug Info
	 *//*
	public static void DebugInfo(String tag, String msg) {
		Log.d(tag, msg);
	}*/

	/**
	 * Destribution： 获取资源中的字符串
	 * 
	 * @param resId
	 *            资源id
	 * @return 返回字符串
	 */
	public static String getResourceString(int resId) {
		return MLApplication.getInstance().getString(resId);
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

	
/**
 *   手机号验证
 * @param str 
 * @return
 */
	public static boolean isCellphone(String str) {
		String s = "^((13[0-9])|(15[0-9])|(18[0-9])|(14[57]))\\d{8}$";
		return str.matches(s);
	}
	
	
	public static String MD5(String original) {
MessageDigest messageDigest = null;
try {
	messageDigest = MessageDigest.getInstance("MD5");
	messageDigest.reset();
	messageDigest.update(original.getBytes("UTF-8"));
} catch (Exception e) {
	return null;
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
    public static boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) MLApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
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
        ConnectivityManager connectivityManager = (ConnectivityManager) MLApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }        
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if(!MLStrUtil.isEmpty(extraInfo)){
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
	 * 打卡软键盘
	 * 
	 * @param mEditText输入框
	 * @param mContext上下文
	 */
	public static void openKeybord(EditText mEditText, Context mContext)
	{
		InputMethodManager imm = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	/**
	 * 关闭软键盘
	 * 
	 * @param mEditText输入框
	 * @param mContext上下文
	 */
	public static void closeKeybord(EditText mEditText, Context mContext)
	{
		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
	}
    
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
    
    /**
     * 打开键盘.
     *
     * @param context the context
     */
    public static void showSoftInput(Context context){
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    
    /**
     * 关闭键盘事件.
     *
     * @param context the context
     */
    public static void closeSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager)context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && ((Activity)context).getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(((Activity)context).getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    /**
     * 拨打电话    
     * @param act
     * @param number
     */
        public static void call(Activity act,String number){
        	//跳到拨号界面不呼叫 ACTION_DIAL
        	//直接呼叫ACTION_CALL
        	Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+number));  
        	act.startActivity(intent);  
        }
        
       /**
        * 调用发短信界面
        * @param act
        * @param number
        */
        public static void sendMessage(Activity act,String number){
        	  Uri uri = Uri.parse("smsto:"+number);             
        	    Intent it = new Intent(Intent.ACTION_SENDTO, uri);             
        	    it.putExtra("sms_body", "");             
        	    act.startActivity(it);  
        }

	public static String getLocalIpAddress() {
		String ipaddress = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
				NetworkInterface intf = en.nextElement();
				if (intf.getName().toLowerCase().equals("eth0") || intf.getName().toLowerCase().equals("wlan0")) {
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							ipaddress = inetAddress.getHostAddress().toString();
							if (!ipaddress.contains("::")) {//ipV6的地址
								return ipaddress;
							}
						}
					}
				} else {
					continue;
				}
			}
		} catch (Exception e) {
			return "127.0.0.1";
		}
		return "127.0.0.1";
	}

		/*try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			//     Log.e(LOG_TAG, ex.toString());
		}
		return null;*/
/*
		String IP = null;
		StringBuilder IPStringBuilder = new StringBuilder();
		try {
			Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaceEnumeration.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
				Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
				while (inetAddressEnumeration.hasMoreElements()) {
					InetAddress inetAddress = inetAddressEnumeration.nextElement();
					if (!inetAddress.isLoopbackAddress()&&
							!inetAddress.isLinkLocalAddress()&&
							inetAddress.isSiteLocalAddress()) {
						IPStringBuilder.append(inetAddress.getHostAddress().toString()+"\n");
					}
				}
			}
		} catch (SocketException ex) {

		}

		IP = IPStringBuilder.toString();
		if(!MLStrUtil.isEmpty(IP)){
			IP = IP.replaceAll("\\s*", "");
		}
		return IP;*/


}
