package com.zuomei.http;

import java.util.ArrayList;
import java.util.List;

public class ZMRequestParams {
	
	private List<ZMHttpParam> postParamList;
	public void addParameter(String paramName,String paramValue){
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

    public  List<ZMHttpParam> getParams(){
    	return postParamList;
    }
    
    
}
