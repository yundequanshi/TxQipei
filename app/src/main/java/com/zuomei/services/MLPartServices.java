package com.zuomei.services;

import com.lidroid.xutils.exception.HttpException;
import com.zuomei.base.BaseHttpService;
import com.zuomei.constants.MLConstants;
import com.zuomei.exception.ZMHttpException;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.IWebService;
import com.zuomei.http.ZMHttpParam;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpUrl;
import com.zuomei.model.MLBaseResponse;
import com.zuomei.model.MLMyPartBusinessMagResponse;
import com.zuomei.model.MLMyPartBusinessResponse;
import com.zuomei.model.MLMyPartDetailResponse;
import com.zuomei.model.MLMyPartOfferMgResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.utils.ZMJsonParser;
import com.zuomei.weixin.pay.EncryptUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MLPartServices extends BaseHttpService implements IWebService {

    public String home_cache = null;

    public static MLPartServices INSTANCE = new MLPartServices();

    public static MLPartServices getInstance() {
        return INSTANCE;
    }

    @Override
    public Object httpPost(ZMHttpRequestMessage httpMessage)
            throws ZMParserException, ZMHttpException {
        switch (httpMessage.getHttpType()) {
      //汽修厂-配件添加
        case DEPOT_PART_ADD:{
        	return EncodeData(httpMessage, MLRegister.class);
        }

      //汽修厂-我的事故车配件 列表
        case DEPOT_PART_MG_LIST:{
        	return EncodeData(httpMessage, MLMyPartOfferMgResponse.class);
        }
        //汽修厂-我的事故车配件 列表-删除
        case DEPOT_PART_MG_DEL:{
        	return EncodeData(httpMessage, MLRegister.class);
        }
        
      //汽修厂-我的事故车配件 报价商家列表
		case DEPOT_PART_BUSINESS_LIST:{
			return EncodeData(httpMessage, MLMyPartBusinessResponse.class);
		}
        
	      //汽修厂-我的事故车配件 报价商家详情
			case DEPOT_PART_BUSINESS_DETAIL:{
				return EncodeData(httpMessage,MLMyPartDetailResponse .class);
			}
			
		      //  汽修厂 我的事故车配件列表-使用
				case DEPOT_PART_BUSINESS_USE:{
					return EncodeData(httpMessage,MLRegister .class);
				}
        
			      // 商家 报价管理列表
					case BUS_PART_LIST:{
						return EncodeData(httpMessage,MLMyPartBusinessMagResponse .class);
					}
					
					//商家 报价详情
					case BUS_PART_DETAIL:{
						return EncodeData(httpMessage,MLMyPartDetailResponse .class);
					}
					
					//商家 报价
					case BUS_PART_OFFER:{
						return EncodeData(httpMessage,MLRegister.class);
					}
	        
					
				
            case WEIXIN_PAY_ENCRYPT:{
            	//添加交易(加密)
            	
            	JSONObject obj = new JSONObject();
            	for (ZMHttpParam param : httpMessage.getPostParamList()) {
            		try {
						obj.put(param.getParamName()+"", param.getParamValue()+"");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
            	
            	String content1= "";
            	//加密
            	try {
            		content1 = 	EncryptUtils.encode(obj.toString(), MLConstants.CARSHOP_HTTP_TAG);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	//获取URL
            	String url = ZMHttpUrl.getUrl(httpMessage.getHttpType(),
        				httpMessage.getUrlParamList());
            	//==========
            	MLRegister rs2 = null;

            	 try {
            		 String rs = 	post(url, content1);
            		 String rs1 = EncryptUtils.decode(rs, MLConstants.CARSHOP_HTTP_TAG);
            		return ZMJsonParser.fromJsonString(MLRegister.class, rs1);
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	return null;
            	//return getResonseData(MLRegister.class, httpMessage);
            }
            
            
            
            
            default:
                break;
        }
        return null;
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
   
	private <T  extends MLBaseResponse> Object getResonseData( Class<T> cls , ZMHttpRequestMessage httpMessage) throws ZMParserException, ZMHttpException{
    	T ret = post(httpMessage, cls);
		return ret;
    }
}
