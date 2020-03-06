package com.zuomei.services;

import com.txsh.model.TXHomeImageRes;
import com.txsh.model.TXHomeTTRes;
import com.txsh.model.TXInfoRes;
import com.zuomei.base.BaseHttpService;
import com.zuomei.exception.ZMHttpException;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.IWebService;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMHttpUrl;
import com.zuomei.http.ZMParserResponse;
import com.zuomei.model.MLBaseResponse;
import com.zuomei.model.MLHomeAdResponse;
import com.zuomei.model.MLHomeBusinessDetail1;
import com.zuomei.model.MLHomeBusinessList;
import com.zuomei.model.MLHomeBusinessResponse;
import com.zuomei.model.MLHomeCatalogResponse;
import com.zuomei.model.MLHomeCityResponse;
import com.zuomei.model.MLHomeProductResponse;
import com.zuomei.model.MLMapResponse;
import com.zuomei.model.MLPayAlipayData;
import com.zuomei.model.MLProductMangerResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.model.MLSpecialResonse;
import com.zuomei.model.TXCarTypeResponse;
import com.zuomei.model.TXHomeCatalogResponse;

public class MLHomeServices extends BaseHttpService implements IWebService {

  public String home_cache = null;

  public static MLHomeServices INSTANCE = new MLHomeServices();

  public static MLHomeServices getInstance() {
    return INSTANCE;
  }

  @Override
  public Object httpPost(ZMHttpRequestMessage httpMessage)
      throws ZMParserException, ZMHttpException {
    switch (httpMessage.getHttpType()) {
      case HOME_CITY: {
        //获取城市
        return getResonseData(MLHomeCityResponse.class, httpMessage);
      }
      case HOME_CATALOG: {
        //获取分类
        return getResonseData(MLHomeCatalogResponse.class, httpMessage);
      }
      case TX_HOME_CATALOG: {
        //获取分类
        return getResonseData(TXHomeCatalogResponse.class, httpMessage);
      }

      case HOME_BUSINESS: {
        //获取首页商家
        return getResonseData(MLHomeBusinessResponse.class, httpMessage);
      }
      case HOME_BUSINESS_DETAIL: {
        //获取商家详情
        return getResonseData(MLHomeBusinessDetail1.class, httpMessage);
      }
      case HOME_COLLECT: {
        //商家收藏
        return getResonseData(MLRegister.class, httpMessage);
      }
      case HOME_BUSINESS_LIST: {
        //商家列表
        return getResonseData(MLHomeBusinessList.class, httpMessage);
      }
      case HOME_AD: {
        //获取广告位
        return getResonseData(MLHomeAdResponse.class, httpMessage);
      }
      case HOME_PRODUCT: {
        //获取更多产品
        return getResonseData(MLHomeProductResponse.class, httpMessage);
      }
      case HOME_MAP: {
        //获取地图坐标
        return getResonseData(MLMapResponse.class, httpMessage);
      }
      case HOME_SEARCH: {
        //获取搜索内容
        return getResonseData(MLHomeBusinessResponse.class, httpMessage);
      }
      case HOME_SEARCH_ALL: {
        //获取搜索内容
        return getResonseData(MLHomeBusinessResponse.class, httpMessage);
      }

      case HOME_CALL: {
        //拨打电话
        return getResonseData(MLSpecialResonse.class, httpMessage);
      }
      case HOME_MESSAGE_COUNT: {
        //获取互动消息数量
        return getResonseData(MLSpecialResonse.class, httpMessage);
      }

      case OFFER_ADD: {
        //商家报价
        return getResonseData(MLRegister.class, httpMessage);
      }

      case TX_INFO_LIST: {
        //行业资讯
        return getResonseData(TXInfoRes.class, httpMessage);
      }
      case HOME_CALL_WEB: {
        //回拨电话
        return getResonseData(MLRegister.class, httpMessage);
      }
      case ORDER_PAY_ALIPAY_RIGHT: {
        // 支付宝确认
        return getResonseData(MLSpecialResonse.class, httpMessage);
      }
      case ORDER_PAY_PARAM_ALIPAY: {
        //支付宝
        String url = ZMHttpUrl.getUrl(httpMessage.getHttpType(),
            httpMessage.getUrlParamList());

        String ret = post(url, httpMessage);
        MLPayAlipayData base1 = ZMParserResponse
            .parserResponse(ZMHttpType.ResponseType.JSON, ret, MLPayAlipayData.class);
        return base1;
        // return getResonseData(MLPayAlipayData.class, httpMessage);
      }

      case HOME_IMAGE: {
        // 获取首页3张图片
        return getResonseData(TXHomeImageRes.class, httpMessage);
      }

      case API_QUERY: {
        return getResonseData(TXHomeTTRes.class, httpMessage);
      }

      case GET_SECOND_COMPANYTYPE: {
        return getResonseData(TXCarTypeResponse.class, httpMessage);
      }
      case GET_SECOND_COMPANYTYPE2: {
        return getResonseData(TXCarTypeResponse.class, httpMessage);
      }
      case FIND_COMPANY_PRODUCTS: {
        return getResonseData(MLProductMangerResponse.class, httpMessage);
      }
      case DELETE_COMPANY_PRODUCTS: {
        return getResonseData(MLRegister.class, httpMessage);
      }
      case ADD_COMPANY_PRODUCTS: {
        return getResonseData(MLRegister.class, httpMessage);
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
