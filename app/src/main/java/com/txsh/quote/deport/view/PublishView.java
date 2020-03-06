package com.txsh.quote.deport.view;

import android.app.Activity;
import com.txsh.quote.IBaseView;

/**
 * Created by 汉玉 on 2017/3/14.
 */
public interface PublishView extends IBaseView {

  Activity getNowAty();

  void setChildType(String typeChildName,String typeChildId);
}
