package com.zuomei.http;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: michael
 * Date: 12-12-8
 * Time: 下午2:54
 * Description:
 */
public class ZMHttpRequestMessage {
	
	private IWebService webService;

    private ZMHttpType.RequestType httpType;

    private Map<String, Object> urlParams;

    private Map<String, String> postParams;

    private List<ZMHttpParam> urlParamList;

    private  List<ZMHttpParam> postParamList;

    private Map<String, Object> otherParmas;

    private Handler handler;

    private int handlerMessageID;

    private Map<String, Object> dbRepositories;

    private String showMessage;


    public ZMHttpRequestMessage() {

    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public ZMHttpRequestMessage(ZMHttpType.RequestType httpType, ZMRequestParams urlParams,
    		ZMRequestParams postParams, Handler handler, int handlerMessageID,
                         IWebService webService) {
        this.httpType = httpType;
        setUrlParamList(urlParams);
        setPostParamList(postParams);
        setHandler(handler);
        setHandlerMessageID(handlerMessageID);
        setWebService(webService);
    }

    public IWebService getWebService() {
		return webService;
	}

	public void setWebService(IWebService webService) {
		this.webService = webService;
	}



	public List<ZMHttpParam> getUrlParamList() {
        return urlParamList;
    }

    public void setUrlParamList(ZMRequestParams urlParamList) {
        this.urlParams = new HashMap<String, Object>();
        if (urlParamList != null){
        	this.urlParamList = urlParamList.getParams();
            for (ZMHttpParam param : this.urlParamList) {
                this.urlParams.put(param.getParamName(), param.getParamValue());
            }
        }

    }

    public List<ZMHttpParam> getPostParamList() {
        return postParamList;
    }

    public void setPostParamList(ZMRequestParams postParamList) {
        this.postParams = new HashMap<String, String>();
     /*   if (postParamList != null&&postParamList.getQueryStringParams()!=null){
                    for (NameValuePair param : postParamList.getQueryStringParams()) {
                        this.postParams.put(param.getName(), (String) param.getValue());
                    }
                }else{
                	this.postParamList = new RequestParams();  
                }*/
        if (postParamList != null){
        	this.postParamList = postParamList.getParams();
            for (ZMHttpParam param : this.postParamList) {
                this.postParams.put(param.getParamName(), (String) param.getParamValue());
            }
        }else{
        	this.postParamList = new ArrayList<ZMHttpParam>();  
        }
    }
    
    public void addPostParam(String paramName,String paramValue){
    	if(postParams!=null){
    		postParams.put(paramName, paramValue);
    	}
		ZMHttpParam param = new ZMHttpParam();
		param.setParamName(paramName);
		if(paramValue==null)
			paramValue = "";
		param.setParamValue(paramValue);
		if(postParamList ==null){
			postParamList = new ArrayList<ZMHttpParam>();
		}
		postParamList.add(param);
    }

    public ZMHttpType.RequestType getHttpType() {
        return httpType;
    }

    public void setHttpType(ZMHttpType.RequestType httpType) {
        this.httpType = httpType;
    }

    public Object getUrlParam(String key) {
        if (urlParams == null || urlParams.size() == 0) return null;
        return urlParams.get(key);
    }

    public String getPostParams(String key) {
        if (postParams == null || postParams.size() == 0) return null;
        return postParams.get(key);
    }

    public int getHandlerMessageID() {
        return handlerMessageID;
    }

    public void setHandlerMessageID(int handlerMessageID) {
        this.handlerMessageID = handlerMessageID;
    }

    public Map<String, Object> getDbRepositories() {
        return dbRepositories;
    }

    public void setDbRepositories(Map<String, Object> dbRepositories) {
        this.dbRepositories = dbRepositories;
    }

    public String getShowMessage() {
        return showMessage;
    }

    public void setShowMessage(String showMessage) {
        this.showMessage = showMessage;
    }

    public Object getOtherParmas(String key) {
        if (otherParmas == null) return null;

        return otherParmas.get(key);
    }

    public void setOtherParmas(Map<String, Object> otherParmas) {
        this.otherParmas = otherParmas;
    }
}
