package com.easemob.easeui.utils;

import com.easemob.easeui.model.GroupData;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.model.TodoData;
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
public interface HxApi {

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

  //1--获取环信信息
  @POST("user/getUser")
  Observable<HxUser> getHxUser(@Body Map<String, String> map);

  //2--获取环信信息根据emid
  @POST("user/getUserByEmId")
  Observable<HxUser> getUserByEmId(@Body Map<String, String> map);

  //3--获取环信全部联系人
  @POST("friend/friends")
  Observable<List<HxUser>> friends(@Body Map<String, String> map);

  //4--获取环信待办数量
  @POST("user/toDoList")
  Observable<List<TodoData>> toDoList(@Body Map<String, String> map);

  //5--删除好友
  @POST("friend/dissolution")
  Observable<String> dissolution(@Body Map<String, String> map);

  //6--查找好友
  @POST("user/findUser")
  Observable<List<HxUser>> findUser(@Body Map<String, String> map);

  //7--添加好友
  @POST("friend/invitation")
  Observable<String> invitation(@Body Map<String, String> map);

  //8--获取环信群列表
  @POST("group/userGroup")
  Observable<List<GroupData>> userGroup(@Body Map<String, String> map);

  //9--解散群
  @POST("group/dissolution")
  Observable<String> groupDissolution(@Body Map<String, String> map);

  //10--创建群
  @POST("group/createGroup")
  Observable<String> createGroup(@Body Map<String, String> map);

  //11--群邀请
  @POST("group/invitationGroup")
  Observable<String> invitationGroup(@Body Map<String, String> map);

  //12--群邀请同意
  @POST("group/addUser2Group")
  Observable<String> addUser2Group(@Body Map<String, String> map);

  //14--好友邀请同意
  @POST("friend/addFriend")
  Observable<String> addFriend(@Body Map<String, String> map);

  //15--待办事情清空
  @POST("user/toDoMsg")
  Observable<String> toDoMsg(@Body Map<String, String> map);

  //16--获取群好友
  @POST("group/groupUsersByEmgId")
  Observable<List<HxUser>> groupUsersByEmgId(@Body Map<String, String> map);

  //17--移除群好友
  @POST("group/removeUser")
  Observable<String> removeUser(@Body Map<String, String> map);

  //18--修改群
  @POST("group/updateGroup")
  Observable<String> updateGroup(@Body Map<String, String> map);

  //19--查找群
  @POST("group/findGroup")
  Observable<List<GroupData>> findGroup(@Body Map<String, String> map);

  //20--申请加群
  @POST("group/applyGroup")
  Observable<String> applyGroup(@Body Map<String, String> map);

  //21--修改用户信息
  @POST("user/updateUser")
  Observable<String> updateUser(@Body Map<String, String> map);
}

