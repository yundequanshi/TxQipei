package com.zuomei.services;

import com.zuomei.base.BaseHttpService;
import com.zuomei.exception.ZMHttpException;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.IWebService;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.model.MLBaseResponse;
import com.zuomei.model.MLInteractionData;
import com.zuomei.model.MLMessageListResponse;
import com.zuomei.model.MLParseResponse;
import com.zuomei.model.MLRegister;

public class MLMessageServices extends BaseHttpService implements IWebService {

  public String home_cache = null;

  public static MLMessageServices INSTANCE = new MLMessageServices();

  public static MLMessageServices getInstance() {
    return INSTANCE;
  }

  @Override
  public Object httpPost(ZMHttpRequestMessage httpMessage)
      throws ZMParserException, ZMHttpException {
    switch (httpMessage.getHttpType()) {
      case MESSAGE_LIST: {
        //获取互动列表
        return getResonseData(MLMessageListResponse.class, httpMessage);
      }
      case FIND_COLLECT_INTERACTION: {
        return getResonseData(MLMessageListResponse.class, httpMessage);
      }
      case MESSAGE_REPLY: {
        //回复消息
        return getResonseData(MLRegister.class, httpMessage);
      }

      case MESSAGE_PUBLISH: {
        //发表互动消息
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MESSAGE_REPORT: {
        //举报
        return getResonseData(MLRegister.class, httpMessage);
      }

      case MY_MESSAGE: {
        //获取我的互动列表
        return getResonseData(MLMessageListResponse.class, httpMessage);
      }
      case MY_MESSAGE_ME: {
        //@我
        return getResonseData(MLMessageListResponse.class, httpMessage);
      }
      case MY_MESSAGE_REPLY: {
        //我的回复消息
        return getResonseData(MLRegister.class, httpMessage);
      }
      case MY_MESSAGE_DEL: {
        //我的-删除互动消息
        return getResonseData(MLRegister.class, httpMessage);
      }
      case HD_COLLECTION: {
        //收藏
        return getResonseData(MLRegister.class, httpMessage);
      }
      case HD_DIANZAN: {
        //点赞
        return getResonseData(MLRegister.class, httpMessage);
      }
      case HD_JUBAO: {
        //举报
        return getResonseData(MLInteractionData.class, httpMessage);
      }
      //修理厂互动
      case GET_USER_TRENDS: {
        return getResonseData(MLMessageListResponse.class, httpMessage);
      } //修理厂互动
      case FIND_PRAISEINFO_BY_INTERACTIONID: {
        return getResonseData(MLParseResponse.class, httpMessage);
      }
      default:
        break;
    }
    return null;
  }

  private <T extends MLBaseResponse> Object getResonseData(Class<T> cls,
      ZMHttpRequestMessage httpMessage) throws ZMParserException, ZMHttpException {
    T ret = post(httpMessage, cls);
    return ret;
  }
}
