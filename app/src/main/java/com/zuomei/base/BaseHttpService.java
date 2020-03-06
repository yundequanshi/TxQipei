package com.zuomei.base;

import com.lidroid.xutils.http.client.multipart.HttpMultipartMode;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.ContentBody;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.zuomei.exception.ZMHttpException;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.MyHttpClient;
import com.zuomei.http.ZMHttpParam;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMHttpType.ResponseType;
import com.zuomei.http.ZMHttpUrl;
import com.zuomei.http.ZMParserResponse;
import com.zuomei.model.MLBaseResponse;
import com.zuomei.model.MLLoginFail;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.utils.ZMJsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseHttpService {

	public Map<String, Object> _cacheMap = new HashMap<String, Object>();
	
	//分页缓存
	public Map<RequestType, Object> _pageCache = new HashMap<RequestType, Object>();
	//当前页码
	public Map<RequestType,String> _currentPage = new HashMap<RequestType, String>();
	
	protected <T  extends MLBaseResponse> T post(ZMHttpRequestMessage httpMessage, Class<T> cls,
			boolean isCache) throws ZMParserException, ZMHttpException {
		return post(httpMessage, cls, isCache, ResponseType.JSON);
	}
	
	protected <T  extends MLBaseResponse> T post(ZMHttpRequestMessage httpMessage, Class<T> cls) throws ZMParserException, ZMHttpException {
		return post(httpMessage, cls, false, ResponseType.JSON);
	}

/*	protected <T extends ZMBaseResponse> List<T> postForPage(ZMHttpRequestMessage httpMessage, Class<T> cls
			) throws ZMParserException, ZMHttpException {
		return postForPage(httpMessage, cls, ResponseType.JSON);
	}*/
	
/**
 *   普通url请求缓存
 * @param httpMessage
 * @param cls
 * @param isCache
 * @param responseType
 * @return
 * @throws ZMParserException
 * @throws ZMHttpException
 */
	protected <T extends MLBaseResponse> T post(ZMHttpRequestMessage httpMessage, Class<T> cls,
			boolean isCache, ResponseType responseType)
			throws ZMParserException, ZMHttpException {
		String url = ZMHttpUrl.getUrl(httpMessage.getHttpType(),
				httpMessage.getUrlParamList());
		// **********************读取缓存************************
		if (isCache) {
			T data = (T) _cacheMap.get(url);
			if (data != null) {
				return data;
			}
		}
		T data = null;
		// *********************网络请求*************************
		String ret = post(url, httpMessage);
		// *********************JSON解析************************
		//data = ZMParserResponse.parserResponse(responseType, ret, cls);
		
		//*********************汽配城项目修改****************************************
		MLBaseResponse base = ZMParserResponse.parserResponse(responseType, ret, MLBaseResponse.class);
		if(base.state.equalsIgnoreCase("1")){
			data = ZMParserResponse.parserResponse(responseType, ret, cls);
		}else{
			MLLoginFail base1 = ZMParserResponse.parserResponse(responseType, ret, MLLoginFail.class);
			//base.message = base1.datas;
			//data = (T) base;
			try {
				data = cls.newInstance();
				data.state="-1";
				data.message = base1.datas;
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		
		//*************************************************************
		if (isCache) {
			_cacheMap.put(url, data);
		}
		return data;
	}
	
//********************************************************************************************************************************************
	protected InputStream  get(ZMHttpRequestMessage httpMessage) throws ZMHttpException, ZMParserException  {
		String url = ZMHttpUrl.getUrl(httpMessage.getHttpType(),
				httpMessage.getUrlParamList());
		InputStream ret = get(url, httpMessage);
		return ret;
	}

	/**
	 * http 请求
	 * 
	 * @param url
	 * @param httpMessage
	 * @return
	 * @throws ZMHttpException
	 * @throws ZMParserException
	 */
	protected String post(String url, ZMHttpRequestMessage httpMessage)
			throws ZMHttpException, ZMParserException {
		HttpPost request = new HttpPost(url);
		
		HttpResponse response = null;
		MLToolUtil.DebugInfo("request",
				"begin================================================");
		MLToolUtil.DebugInfo("request", String.format("请求的url：%s", url));
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (ZMHttpParam param : httpMessage.getPostParamList()) {
			MLToolUtil.DebugInfo(
					"request",
					String.format("请求的参数{%s:%s}", param.getParamName(),
							param.getParamValue()));
			nameValuePairs.add(new BasicNameValuePair(param.getParamName(),
					param.getParamValue()));
		}
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));
			response = MyHttpClient.getNewInstance(
					BaseApplication.getInstance()).execute(request);
		} catch (Exception e) {
			throw new ZMHttpException(e != null ? e.getMessage() : null);
		}
		InputStream is = getResponseInputStream(response);
		String ret = ZMJsonParser.getStringFromInputStream(is);
		MLToolUtil.DebugInfo("request", String.format("请求的结果：%s", ret));
		MLToolUtil.DebugInfo("request",
				"end================================================");
		return ret;
	}
	
	
	protected String post(String url, String rquestParam)
			throws ZMHttpException, ZMParserException {
		HttpPost request = new HttpPost(url);
		HttpResponse response = null;
		MLToolUtil.DebugInfo("request",
				"begin================================================");
		MLToolUtil.DebugInfo("request", String.format("请求的url：%s", url));
		try {
			request.setEntity(new StringEntity(rquestParam));
			response = MyHttpClient.getNewInstance(
					BaseApplication.getInstance()).execute(request);
		} catch (Exception e) {
			throw new ZMHttpException(e != null ? e.getMessage() : null);
		}
		InputStream is = getResponseInputStream(response);
		String ret = ZMJsonParser.getStringFromInputStream(is);
		MLToolUtil.DebugInfo("request", String.format("请求的结果：%s", ret));
		MLToolUtil.DebugInfo("request",
				"end================================================");
		return ret;
	}
	
	protected InputStream get(String url, ZMHttpRequestMessage httpMessage)
			throws ZMHttpException, ZMParserException {
		HttpGet request = new HttpGet(url);
		HttpResponse response = null;
		MLToolUtil.DebugInfo("",
				"begin================================================");
		MLToolUtil.DebugInfo("", String.format("请求的url：%s", url));
		String urlparams = "";
		for (ZMHttpParam param : httpMessage.getPostParamList()) {
			MLToolUtil.DebugInfo(
					"",
					String.format("请求的参数{%s:%s}", param.getParamName(),
							param.getParamValue()));
			urlparams+=param.getParamName()+"="+param.getParamValue();
		}
		if(urlparams!=null&&!urlparams.equalsIgnoreCase(""))
			url = String.format("%s?%s", url,urlparams);
		try {
			response = MyHttpClient.getNewInstance(
					BaseApplication.getInstance()).execute(request);
		} catch (Exception e) {
			throw new ZMHttpException(e != null ? e.getMessage() : null);
		}
		InputStream is = getResponseInputStream(response);
		return is;
	}

	/**
	 * 上传
	 * 
	 * @param url
	 * @param postParams
	 * @return
	 * @throws ZMHttpException
	 * @throws ZMParserException
	 */
	protected String postAttach(String url, List<ZMHttpParam> postParams)
			throws ZMHttpException, ZMParserException {
		HttpPost request = new HttpPost(url);
		HttpResponse response = null;
		MLToolUtil.DebugInfo("",
				"begin================================================");
		MLToolUtil.DebugInfo("", String.format("请求的url：%s", url));
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (ZMHttpParam param : postParams) {
			MLToolUtil.DebugInfo(
					"",
					String.format("请求的参数{%s:%s}", param.getParamName(),
							param.getParamValue()));
			nameValuePairs.add(new BasicNameValuePair(param.getParamName(),
					param.getParamValue()));
		}
		boolean hasAttach = false;
		try {
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			for (ZMHttpParam param : postParams) {
				// 注册中头像
				//
				if (param.getParamName().equalsIgnoreCase(
				"")||param.getParamName().equalsIgnoreCase(
							"")) {
					hasAttach = true;
					File file = new File(param.getParamValue());
					if (file.exists()) {
						entity.addPart(param.getParamName(), new FileBody(
								new File(param.getParamValue())));
					}
				} else {
					ContentBody contentBody = null;
					contentBody = new StringBody(param.getParamValue(),
							Charset.forName(HTTP.UTF_8));
					entity.addPart(param.getParamName(), contentBody);
				}
			}
			request.setEntity(entity);
			response = MyHttpClient.getNewInstance(
					BaseApplication.getInstance()).execute(request);
		} catch (Exception e) {
			throw new ZMHttpException(e != null ? e.getMessage() : null);
		}
		InputStream is = getResponseInputStream(response);
		String ret = ZMJsonParser.getStringFromInputStream(is);
		MLToolUtil.DebugInfo("", String.format("请求的结果：%s", ret));
		MLToolUtil.DebugInfo("",
				"end================================================");
		return ret;
	}

	private InputStream getISFromRespone(HttpResponse response)
			throws ZMHttpException {
		try {
			InputStream is = response.getEntity().getContent();
			return new BufferedInputStream(is);

		} catch (Exception e) {
			throw new ZMHttpException(e != null ? e.getMessage() : null);
		}
	}

	private InputStream getResponseInputStream(HttpResponse response)
			throws ZMHttpException {
		if (response == null)
			return null;
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			switch (statusCode) {
			case 200: {
				InputStream is = getISFromRespone(response);
				return is;
			}
			default:
				return null;
			}
		} catch (Exception e) {
			throw new ZMHttpException(e != null ? e.getMessage() : null);
		}
	}

	// ********************************************************************************************************
	public void clearCache() {
		_cacheMap.clear();
	}

	public void clearCache(String url) {
		_cacheMap.remove(url);
	}
	
	public void clearPageCache(RequestType type) {
		_pageCache.remove(type);
	}
}
