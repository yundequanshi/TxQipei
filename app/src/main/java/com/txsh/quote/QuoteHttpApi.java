package com.txsh.quote;

import com.baichang.android.request.HttpFactory;
import com.txsh.quote.business.entity.BizQuotedDetailData;
import com.txsh.quote.business.entity.BizQuotedListData;
import com.txsh.quote.deport.entity.CompanyDetailData;
import com.txsh.quote.deport.entity.QuotedDetailData;
import com.txsh.quote.deport.entity.QuotedListData;
import com.easemob.easeui.utils.HxApi;
import com.zuomei.constants.APIConstants;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 汉玉 on 2016/12/14.
 */
public class QuoteHttpApi implements QuoteApi {

  @Override
  public Observable<List<String>> upload(@Part MultipartBody.Part file) {
    return HttpFactory.creatUpload(QuoteApi.class).upload(file).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread());
  }

  @Override
  public Observable<List<String>> uploads(@Part List<MultipartBody.Part> files) {
    return HttpFactory.creatUpload(QuoteApi.class).uploads(files)
        .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
  }


  @Override
  public Observable<ResponseBody> download(@Url String fileUrl) {
    //下载不需要设置线程，底层已经设置
    return HttpFactory.creatDownload(QuoteApi.class).download(fileUrl);
  }

  @Override
  public Observable<Boolean> addOfferSheet(@Body Map<String, String> map) {
    return getRequest().addOfferSheet(map);
  }

  @Override
  public Observable<List<QuotedListData>> findDepotOfferSheet(@Body Map<String, String> map) {
    return getRequest().findDepotOfferSheet(map);
  }

  @Override
  public Observable<QuotedDetailData> getOfferSheetDetail(@Body Map<String, String> map) {
    return getRequest().getOfferSheetDetail(map);
  }

  @Override
  public Observable<CompanyDetailData> getCompanyOfferSheetDetail(@Body Map<String, String> map) {
    return getRequest().getCompanyOfferSheetDetail(map);
  }

  @Override
  public Observable<BizQuotedDetailData> getCompanyOfferSheetDetail2(
      @Body Map<String, String> map) {
    return getRequest().getCompanyOfferSheetDetail2(map);
  }

  @Override
  public Observable<Boolean> purchase(@Body Map<String, String> map) {
    return getRequest().purchase(map);
  }

  @Override
  public Observable<List<BizQuotedListData>> findCompanyOfferSheet(@Body Map<String, String> map) {
    return getRequest().findCompanyOfferSheet(map);
  }

  @Override
  public Observable<Boolean> companyOffer(@Body Map<String, String> map) {
    return getRequest().companyOffer(map);
  }

  @Override
  public Observable<Boolean> companySure(@Body Map<String, String> map) {
    return getRequest().companySure(map);
  }

  @Override
  public Observable<Boolean> companySend(@Body Map<String, String> map) {
    return getRequest().companySend(map);
  }


  private QuoteApi getRequest() {
    return HttpFactory.creatHttp(QuoteApi.class, APIConstants.API_NEW_DEFAULT_HOST);
  }
}
