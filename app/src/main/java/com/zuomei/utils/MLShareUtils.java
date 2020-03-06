package com.zuomei.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.txsh.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.ml.base.utils.MLStrUtil;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;


public class MLShareUtils {
	  public static void showShare(Context context,PlatformActionListener listener,final String title,final String content , final String url) {
		  showShare(context, listener, title, content, url, null);
	  }
	  public static void showShare(Context context,PlatformActionListener listener,final String title,final String content , final String url,final String icon) {
	    	String path = context.getExternalCacheDir()+"/";
	    	File f = new File(path, "icon.png");
	    	Bitmap bitmap =readBitMap(context, R.drawable.logo);
	    	saveBitmap(f,bitmap);
	    	
			 ShareSDK.initSDK(context);
			 OnekeyShare oks = new OnekeyShare();
			 //关闭sso授权
			 oks.disableSSOWhenAuthorize(); 
			// 分享时Notification的图标和文字
			 oks.setNotification(R.drawable.logo, context.getString(R.string.app_name));
			 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		//	 oks.setTitle(context.getString(R.string.share));
			 oks.setTitle(title);
			 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		//	 oks.setTitleUrl("http://sharesdk");
			 oks.setTitleUrl(url);
			 // text是分享文本，所有平台都需要这个字段
			// oks.setText("我是分享文本");
			 oks.setText(content);
			 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
			 //oks.setImagePath(f.getAbsolutePath());
			 if(MLStrUtil.isEmpty(icon)){
				 oks.setImagePath(f.getAbsolutePath());
			 }else{
				 oks.setImageUrl(icon);
			 }
			 
		//	 oks.setImageUrl("http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg");
		//	 oks.setImageUrl("http://bcs.duapp.com/notnoob/fc_icon.png");
			 // url仅在微信（包括好友和朋友圈）中使用
		//	 oks.setUrl("http://sharesdk.cn");
			 oks.setUrl(url);
			 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
			// oks.setComment("我是测试评论文本");
			 // site是分享此内容的网站名称，仅在QQ空间使用
			 oks.setSite(context.getString(R.string.app_name));
			 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
			 oks.setSiteUrl("http://sharesdk.cn");
			 
			 
			 oks.setCallback(listener);
			 oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
				
				@Override
				public void onShare(Platform platform, ShareParams paramsToShare) {
/*					if(ShortMessage.NAME.equalsIgnoreCase(platform.getName())){
//						paramsToShare.setImagePath(null);
						paramsToShare.setText(title+"\n"+url+"");
						paramsToShare.setImagePath(null);
					}*/
				}
			});
			 
				// 启动分享GUI
			 oks.show(context);
			 }
	  
	    public  static Bitmap readBitMap(Context context,int resId){
		      BitmapFactory.Options opt = new BitmapFactory.Options();
		      opt.inPreferredConfig = Bitmap.Config.RGB_565;
		      opt.inPurgeable = true;  
		      opt.inInputShareable = true;  
		       //获取资源图片  
		     InputStream is = context.getResources().openRawResource(resId);
		        return BitmapFactory.decodeStream(is, null, opt);
	}
	    public static void saveBitmap(  File f ,Bitmap bm) {
	    	   if (f.exists()) {
	    	    f.delete();
	    	   }
	    	   try {
	    	    FileOutputStream out = new FileOutputStream(f);
	    	    bm.compress(Bitmap.CompressFormat.PNG, 90, out);
	    	    out.flush();
	    	    out.close();
	    	   } catch (FileNotFoundException e) {
	    	    // TODO Auto-generated catch block
	    	    e.printStackTrace();
	    	   } catch (IOException e) {
	    	    // TODO Auto-generated catch block
	    	    e.printStackTrace();
	    	   }

	    	 }
}
