package com.zuomei.services;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest;
import com.txsh.model.TXSupplyRes;
import com.zuomei.base.BaseHttpService;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.exception.ZMHttpException;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.IWebService;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.model.MLAccidentDetailResponse;
import com.zuomei.model.MLAccidentListResponse;
import com.zuomei.model.MLBaseResponse;
import com.zuomei.model.MLMessagePublishResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.utils.ZMJsonParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MLAccidentServices extends BaseHttpService implements IWebService {

    public String home_cache = null;

    public static MLAccidentServices INSTANCE = new MLAccidentServices();

    public static MLAccidentServices getInstance() {
        return INSTANCE;
    }

    @Override
    public Object httpPost(ZMHttpRequestMessage httpMessage)
            throws ZMParserException, ZMHttpException {
        switch (httpMessage.getHttpType()) {
            case ACCIDENT_ADD: {
                //添加事故车
            	return addAccident(MLRegister.class,httpMessage);
            }
            case AACIDENT_LIST:{
            	//事故车列表
            	return getResonseData(MLAccidentListResponse.class, httpMessage);
            }
            case AACIDENT_DETAIL:{
            	//事故车详情
            	return getResonseData(MLAccidentDetailResponse.class, httpMessage);
            }


			case SUPPLY_ADD:{
				//增加供求信息
				return getResonseData(MLRegister.class, httpMessage);
			}
			case SUPPLY_LIST:{
				//供求信息列表
				return getResonseData(TXSupplyRes.class, httpMessage);
			}

			case SUPPLY_MY:{
				//我的供应/求购列表
				return getResonseData(TXSupplyRes.class, httpMessage);
			}

			case SUPPLY_MY_DEL:{
				//删除
				return getResonseData(MLRegister.class, httpMessage);
			}

            default:
                break;
        }
        return null;
    }
    
    /**
      * @description  添加事故车
      *
      * @author marcello
     * @throws ZMHttpException 
     * @throws ZMParserException 
     */
    private Object addAccident(Class<MLRegister> class1,
			ZMHttpRequestMessage httpMessage) throws ZMParserException, ZMHttpException {
    	List<String> paths = (List<String>) httpMessage.getOtherParmas("image");
    	String imageId = "";
    	for(int i = 0;i<paths.size();i++){
    		RequestParams params = new RequestParams();
    		params.addBodyParameter("file",  new File(paths.get(i)));
    	    HttpUtils http = new HttpUtils();
            http.configCurrentHttpCacheExpiry(1000 * 10);
            try {
				ResponseStream responseStream = http.sendSync(HttpRequest.HttpMethod.POST, APIConstants.API_IMAGE_UPLOAD_OLD,params);
				MLMessagePublishResponse ret = ZMJsonParser.fromJsonString(MLMessagePublishResponse.class,responseStream.readString());

				if( i ==paths.size()-1){
					imageId= imageId+ret.datas;
				}else{
					imageId= imageId+ret.datas+",";
				}
			} catch (HttpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ZMParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	httpMessage.addPostParam(MLConstants.PARAM_MY_IMAGE,imageId);
		return getResonseData(class1,httpMessage);
	}

	private <T  extends MLBaseResponse> Object getResonseData( Class<T> cls , ZMHttpRequestMessage httpMessage) throws ZMParserException, ZMHttpException{
    	T ret = post(httpMessage, cls);
		return ret;
    }
}
