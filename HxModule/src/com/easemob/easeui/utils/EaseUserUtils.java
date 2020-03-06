package com.easemob.easeui.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.baichang.android.request.HttpErrorListener;
import com.baichang.android.request.HttpSubscriber;
import com.baichang.android.request.HttpSuccessListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easemob.easeui.R;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.easemob.easeui.domain.EaseUser;
import com.easemob.easeui.model.HxUser;
import com.lidroid.xutils.db.sqlite.Selector;
import java.util.HashMap;
import java.util.Map;

public class EaseUserUtils {

  static EaseUserProfileProvider userProvider;

  static {
    userProvider = EaseUI.getInstance().getUserProfileProvider();
  }

  /**
   * 根据username获取相应user
   */
  public static EaseUser getUserInfo(String username) {
    if (userProvider != null) {
      return userProvider.getUser(username);
    }

    return null;
  }

  /**
   * 设置用户头像
   **/
  public static void setUserAvatar(Context context, String path, ImageView imageView) {
    Glide.with(context)
        .load(APIConstants.API_LOAD_IMAGE + path)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.ease_default_avatar)
        .placeholder(R.drawable.ease_default_avatar)
        .into(imageView);
  }

  /**
   * 设置用户昵称
   */
  public static void setUserNick(String username, TextView textView) {
    if (textView != null) {
      HxUser user = MLDBUtils.getFirst(Selector.from(HxUser.class).where("emId", "=", username));
      if (user != null) {
        textView.setText(user.name);
      } else {
        textView.setText(username);
      }


    }
  }

  /**
   * 设置用户昵称和头像
   */
  public static void setUserInfo(Context context, String emid, ImageView imageView) {
    HxUser user = MLDBUtils.getFirst(Selector.from(HxUser.class).where("emId", "=", emid));
    String path = "";
    if (user != null) {
      path = user.photo;
    }
    Glide.with(context)
        .load(APIConstants.API_LOAD_IMAGE + path)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ease_default_avatar)
        .into(imageView);
  }

  /**
   * 设置用户昵称和头像
   */
  public static void setUserInfo(Context context, String emid, TextView textView,
      ImageView imageView) {
    HxUser user = MLDBUtils.getFirst(Selector.from(HxUser.class).where("emId", "=", emid));
    String path = "";
    if (user != null) {
      textView.setText(user.name);
      path = user.photo;
    } else {
      getUserInfoByEmId(context, emid, textView, imageView);
    }
    Log.d("会话path", path);
    Glide.with(context)
        .load(APIConstants.API_LOAD_IMAGE + path)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ease_default_avatar)
        .into(imageView);
  }


  public static void getUserInfoByEmId(final Context context, final String emId,
      final TextView textView, final ImageView imageView) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("emId", emId);
    HxApi requset = new HxHttpApi();
    requset.getUserByEmId(map)
        .compose(HttpSubscriber.<HxUser>applySchedulers())
        .subscribe(new HttpSubscriber<HxUser>(new HttpSuccessListener<HxUser>() {
          @Override
          public void success(HxUser hxUser) {
            MLDBUtils.saveOrUpdate(hxUser);
            if (hxUser != null) {
              textView.setText(hxUser.name);
              Glide.with(context)
                  .load(APIConstants.API_LOAD_IMAGE + hxUser.photo)
                  .diskCacheStrategy(DiskCacheStrategy.ALL)
                  .placeholder(R.drawable.ease_default_avatar)
                  .into(imageView);
            }
          }
        }, new HttpErrorListener() {
          @Override
          public void error(Throwable throwable) {
            textView.setText(emId);
            Glide.with(context)
                .load(R.drawable.ease_default_avatar)
                .into(imageView);
          }
        }));
  }
}
