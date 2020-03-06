package com.txsh.quote.deport.present;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.baichang.android.widget.BCNoScrollListView;
import com.txsh.quote.IBasePresent;
import com.txsh.quote.deport.PublishActivity;

/**
 * Created by 汉玉 on 2017/3/14.
 */
public interface PublishPresent extends IBasePresent {

  void attachListView(BCNoScrollListView bcNoScrollListView, Activity activity);

  void showPopupWindow(View view, Activity activity);

  void addOfferSheet(String typeId, String typeChildId, String typeName, String childType,
      String years, String displacement, String carJia, String logisticsName,
      final Activity activity);

  void selectYear(Activity publishActivity, TextView etYears);

  void selectPhoto(Activity activity, ImageView quoted, ImageView ivQuoted);

  void delete(PublishActivity activity, ImageView ivQuoted, ImageView ivQuotedDelete);

  void selectChildType(String typeId,Activity activity);
}
