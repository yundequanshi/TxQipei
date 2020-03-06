package com.txsh.utils;

import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zuomei.constants.APIConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.MLToastUtils;

public class MLHttpImage {
	/**
	 * 获取头像
	 * 
	 * @param id
	 * @return
	 */
	public static String getIconUrl(int size,int id){
		String imageSize = "";
		switch (size) {
		case 0:
			//原图
			imageSize = "defalut";
			break;
		case 1:
			//小图
			imageSize = "small";
			break;
		case 2:
			//中图
			imageSize = "mid";
			break;
		case 3:
			//大图
			imageSize = "big";
			break;
		default:
			break;
		}
		String path = String.format("%s/%s/%s/load",APIConstants.API_IMAGE_UPLOAD,imageSize,id+"");
		return path;
	}
	
	
	private static IEvent<List> mEvent;
	public static void updateIcon(final Context context,String path, IEvent<List> event){
		mEvent = event;

		  	RequestParams params = new RequestParams();
	    	params.addBodyParameter("file", new File(path));
          //  params.addBodyParameter("is300","1");
	    	HttpUtils http = new HttpUtils();

	    	http.send(HttpRequest.HttpMethod.POST,
	    	   APIConstants.API_IMAGE_UPLOAD+	 APIConstants.API_UPLOAD_IMAGES,
	    	    params,
	    	    new RequestCallBack<String>() {

	    	        @Override
	    	        public void onStart() {
	    	        	MLDialogUtils.showProgressDialog(context, "图片上传中，请稍等...");
	    	        }

	    	        @Override
	    	        public void onLoading(long total, long current, boolean isUploading) {
	    	        }

	    	        @Override
	    	        public void onSuccess(ResponseInfo<String> responseInfo) {
	    	        	mEvent.onEvent(null, getResponse(responseInfo.result));
	    	        }

					@Override
	    	        public void onFailure(HttpException error, String msg) {
						MLDialogUtils.dismissProgressDialog();
	    	        	MLToastUtils.showMessage(context, "图片上传失败!");
	    	        }
	    	});
	}


	protected static List  getResponse(String ret) {
		
		try {
			JSONObject jb = new JSONObject(ret);
			// 服务器状态
			boolean service_state = jb.getBoolean("state");
			if (service_state == true) {
				// 接口状态
				int ret_state = jb.getJSONObject("res").getInt("code");
				if (ret_state == 40000) {
					String ret_data = "";
					//data 为null  直接返回
					if( jb.getJSONObject("res").isNull("data")){
						//Object msg = jb.getJSONObject("res").get("msg");
						//return msg==null?"":msg.toString();
						return new ArrayList();
					}else{

						List<String> mPath = new ArrayList<String>();

						JSONArray jsonArray = new JSONArray(jb.getJSONObject("res").getJSONArray("data").toString());

						for (int i = 0, length = jsonArray.length(); i < length; i++) {
							String str = jsonArray.getString(i);
							mPath.add(str);
						}


						return 	mPath;
					}
				} else {
					return new ArrayList();
				}
			} else {
				return new ArrayList();
			}
		} catch (Exception e) {
			return new ArrayList();
		}
		
	}
}
