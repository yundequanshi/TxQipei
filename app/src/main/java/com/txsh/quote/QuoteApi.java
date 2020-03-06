package com.txsh.quote;

import com.txsh.quote.business.entity.BizQuotedDetailData;
import com.txsh.quote.business.entity.BizQuotedListData;
import com.txsh.quote.deport.entity.CompanyDetailData;
import com.txsh.quote.deport.entity.QuotedDetailData;
import com.txsh.quote.deport.entity.QuotedListData;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by iscod. Time:2016/6/21-9:46.
 */
public interface QuoteApi {

  //上传文件
  @Multipart
  @POST("file/upload/")
  Observable<List<String>> upload(@Part MultipartBody.Part file);

  //上传文件
  @Multipart
  @POST("file/upload/")
  Observable<List<String>> uploads(@Part List<MultipartBody.Part> files);

  //下载
  @GET
  @Streaming
  Observable<ResponseBody> download(@Url String fileUrl);

  //修理厂发布报价
  @POST("mobile/offersheet/addOfferSheet")
  Observable<Boolean> addOfferSheet(@Body Map<String, String> map);

  //修理厂报价列表
  @POST("mobile/offersheet/findDepotOfferSheet")
  Observable<List<QuotedListData>> findDepotOfferSheet(@Body Map<String, String> map);

  //修理厂报价详情
  @POST("mobile/offersheet/getOfferSheetDetail")
  Observable<QuotedDetailData> getOfferSheetDetail(@Body Map<String, String> map);

  //修理厂查看报价详情
  @POST("mobile/offersheet/getCompanyOfferSheetDetail")
  Observable<CompanyDetailData> getCompanyOfferSheetDetail(@Body Map<String, String> map);

  //商家查看报价详情
  @POST("mobile/offersheet/getCompanyOfferSheetDetail")
  Observable<BizQuotedDetailData> getCompanyOfferSheetDetail2(@Body Map<String, String> map);

  //修理厂确认采购
  @POST("mobile/offersheet/purchase")
  Observable<Boolean> purchase(@Body Map<String, String> map);

  //商家报价列表
  @POST("mobile/offersheet/findCompanyOfferSheetByState")
  Observable<List<BizQuotedListData>> findCompanyOfferSheet(@Body Map<String, String> map);

  //商家提交报价
  @POST("mobile/offersheet/companyOffer")
  Observable<Boolean> companyOffer(@Body Map<String, String> map);

  //商家报价确认
  @POST("mobile/offersheet/companySure")
  Observable<Boolean> companySure(@Body Map<String, String> map);

  //商家发货
  @POST("mobile/offersheet/companySend")
  Observable<Boolean> companySend(@Body Map<String, String> map);
}

