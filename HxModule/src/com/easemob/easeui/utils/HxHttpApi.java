package com.easemob.easeui.utils;

import com.baichang.android.request.HttpFactory;
import com.easemob.easeui.model.GroupData;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.model.TodoData;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by 汉玉 on 2016/12/14.
 */
public class HxHttpApi implements HxApi {

  @Override
  public Observable<List<String>> upload(@Part MultipartBody.Part file) {
    return HttpFactory.creatUpload(HxApi.class).upload(file);
  }

  @Override
  public Observable<List<String>> uploads(@Part List<MultipartBody.Part> files) {
    return HttpFactory.creatUpload(HxApi.class).uploads(files);
  }

  @Override
  public Observable<ResponseBody> download(@Url String fileUrl) {
    //下载不需要设置线程，底层已经设置
    return HttpFactory.creatDownload(HxApi.class).download(fileUrl);
  }

  @Override
  public Observable<HxUser> getHxUser(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).getHxUser(map);
  }

  @Override
  public Observable<HxUser> getUserByEmId(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).getUserByEmId(map);
  }

  @Override
  public Observable<List<HxUser>> friends(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).friends(map);
  }

  @Override
  public Observable<List<TodoData>> toDoList(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).toDoList(map);
  }

  @Override
  public Observable<String> dissolution(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).dissolution(map);
  }

  @Override
  public Observable<List<HxUser>> findUser(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).findUser(map);
  }

  @Override
  public Observable<String> invitation(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).invitation(map);
  }

  @Override
  public Observable<List<GroupData>> userGroup(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).userGroup(map);
  }

  @Override
  public Observable<String> groupDissolution(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).groupDissolution(map);
  }

  @Override
  public Observable<String> createGroup(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).createGroup(map);
  }

  @Override
  public Observable<String> invitationGroup(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).invitationGroup(map);
  }

  @Override
  public Observable<String> addUser2Group(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).addUser2Group(map);
  }

  @Override
  public Observable<String> addFriend(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).addFriend(map);
  }

  @Override
  public Observable<String> toDoMsg(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).toDoMsg(map);
  }

  @Override
  public Observable<List<HxUser>> groupUsersByEmgId(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).groupUsersByEmgId(map);
  }

  @Override
  public Observable<String> removeUser(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).removeUser(map);
  }

  @Override
  public Observable<String> updateGroup(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).updateGroup(map);
  }

  @Override
  public Observable<List<GroupData>> findGroup(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).findGroup(map);
  }

  @Override
  public Observable<String> applyGroup(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).applyGroup(map);
  }

  @Override
  public Observable<String> updateUser(@Body Map<String, String> map) {
    return HttpFactory.creatHttp(HxApi.class, APIConstants.API_HX_HOST).updateUser(map);
  }
}
