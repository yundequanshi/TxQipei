package com.zuomei.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zuomei.base.BaseHttpService;
import com.zuomei.exception.ZMHttpException;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.IWebService;
import com.zuomei.http.ZMHttpRequestMessage;

import java.io.InputStream;

public class HttpDownloadServices extends BaseHttpService implements IWebService {

	public String home_cache = null;

	public static HttpDownloadServices INSTANCE = new HttpDownloadServices();

	public static HttpDownloadServices getInstance() {
		return INSTANCE;
	}

	@Override
	public Object httpPost(ZMHttpRequestMessage httpMessage)
			throws ZMParserException, ZMHttpException {
		switch (httpMessage.getHttpType()) {

		case  DOWNLOADFILE:{
			return download(httpMessage);
		}
		default:
			break;
		}
		return null;
	}

 
	private Bitmap download(ZMHttpRequestMessage httpMessage)
			throws ZMParserException, ZMHttpException {
		InputStream is =  get(httpMessage);
		Bitmap b = BitmapFactory.decodeStream(is);
		return b;
	}
	
}
