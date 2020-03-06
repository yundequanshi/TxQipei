package com.zuomei.services;

import com.lidroid.xutils.exception.HttpException;
import com.zuomei.base.BaseHttpService;
import com.zuomei.constants.MLConstants;
import com.zuomei.exception.ZMHttpException;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.IWebService;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpUrl;
import com.zuomei.model.MLLoginResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.model.MLUpdateResponse;
import com.zuomei.utils.ZMJsonParser;
import com.zuomei.weixin.pay.EncryptUtils;

import java.io.IOException;

public class MLLoginServices extends BaseHttpService implements IWebService {

    public String home_cache = null;

    public static MLLoginServices INSTANCE = new MLLoginServices();

    public static MLLoginServices getInstance() {
        return INSTANCE;
    }

    @Override
    public Object httpPost(ZMHttpRequestMessage httpMessage)
            throws ZMParserException, ZMHttpException {
        switch (httpMessage.getHttpType()) {
            case REGISTER: {
                //注册
                return getRegister(httpMessage);
            }
            case LOGIN: {
                //登录
                return getLogin(httpMessage);
            }
            case LOGIN_GETCODE: {
                //获取验证码
                return getCode(httpMessage);
            }
            case LOGIN_RESET_PWD: {
                //重置密码
                return resetPwd(httpMessage);
            }
            case LOGIN_UPDATE: {
                //更新
                return checkUpdate(httpMessage);
            }
            
            
            
            default:
                break;
        }
        return null;
    }

    private Object checkUpdate(ZMHttpRequestMessage httpMessage) throws ZMParserException, ZMHttpException {
    	MLUpdateResponse ret = post(httpMessage, MLUpdateResponse.class, false);
		return ret;
	}

	/**
     * @description  重置密码
     *
     * @author marcello
     * @throws ZMHttpException 
     * @throws ZMParserException 
    */
private Object resetPwd(ZMHttpRequestMessage httpMessage) throws ZMParserException, ZMHttpException {
	MLRegister ret = post(httpMessage, MLRegister.class, false);
	return ret;
	}

/**
  * @description  登录
  *
  * @author marcello
 */
    private Object getLogin(ZMHttpRequestMessage httpMessage) throws ZMParserException, ZMHttpException {
    	
    return	post(httpMessage, MLLoginResponse.class);
    	
    //	MLLoginResponse ret = post(httpMessage, MLLoginResponse.class, false);
/*    	String url = ZMHttpUrl.getUrl(httpMessage.getHttpType(),
				httpMessage.getUrlParamList());
    	String ret = post(url, httpMessage);
    	MLBaseResponse	data = ZMParserResponse.parserResponse(ResponseType.JSON, ret, MLBaseResponse.class);
    	if(data.state.equalsIgnoreCase("1")){
    		MLLoginResponse	success = ZMParserResponse.parserResponse(ResponseType.JSON, ret, MLLoginResponse.class);
    		return success;
    	}else{
    		MLLoginFail	fail = ZMParserResponse.parserResponse(ResponseType.JSON, ret, MLLoginFail.class);
    		return fail;
    	}*/
	}    
    
    /**
     * 获取验证码
      * @description  
      *
      * @author marcello
     */
    private Object getCode(ZMHttpRequestMessage httpMessage) throws ZMParserException, ZMHttpException {
    	return	EncodeData(httpMessage, MLSpecialResonse.class);
	}

	/**
     * 注册
     *
     * @param httpMessage
     * @return
     */
    private Object getRegister(ZMHttpRequestMessage httpMessage) throws ZMParserException, ZMHttpException {
    	
    	MLLoginResponse ret = post(httpMessage, MLLoginResponse.class, false);
    	return ret;
      /*  String url = ZMHttpUrl.getUrl(httpMessage.getHttpType(),
                httpMessage.getUrlParamList());
        String ret = postAttach(url, httpMessage.getPostParamList());
        if (!ZMToolUtil.isNull(ret)) {
        	ZMRegisterResponse data = ZMParserResponse.parserResponse(ZMHttpType.ResponseType.JSON, ret, ZMRegisterResponse.class);
            return data;
        } else {
            throw new ZMHttpException(ZMToolUtil.getResourceString(R.string.http_response_null));
        }*/
    }


    private <T> Object EncodeData(ZMHttpRequestMessage httpMessage,Class<T> cls){

        String data = httpMessage.getPostParams("data");
        String content1= "";
        //加密
        try {
            content1 = 	EncryptUtils.encode(data, MLConstants.CARSHOP_HTTP_TAG);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //获取URL
        String url = ZMHttpUrl.getUrl(httpMessage.getHttpType(),
                httpMessage.getUrlParamList());
        //==========
        try {
            String rs = 	post(url, content1);
            String rs1 = EncryptUtils.decode(rs, MLConstants.CARSHOP_HTTP_TAG);
            return ZMJsonParser.fromJsonString(cls, rs1);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
