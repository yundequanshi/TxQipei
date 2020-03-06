package com.zuomei.auxiliary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.AdapterBase;
import com.zuomei.model.MLMyUserData;

public class MLMyUserAdapter extends AdapterBase<MLMyUserData> {

  private Context _context;

  public MLMyUserAdapter(Context _context) {
    super();
    this._context = _context;
  }

  @Override
  protected View getExView(int position, View view, ViewGroup parent) {

    MLMyUserItemView item = null;
    if (view == null) {
      item = new MLMyUserItemView(_context);
      view = item;
    } else {
      item = (MLMyUserItemView) view;
    }
    MLMyUserData data = (MLMyUserData) getItem(position);
    item.setData(data);
    return item;
  }

  class MLMyUserItemView extends LinearLayout {

    private Context _context;

    public MLMyUserItemView(Context context, AttributeSet attrs) {
      super(context, attrs);
      _context = context;
      init();
    }

    public MLMyUserItemView(Context context) {
      super(context);
      _context = context;
      init();
    }

    @ViewInject(R.id.tv_name)
    private TextView _nameTv;

    @ViewInject(R.id.tv_phone)
    private TextView _phoneTv;

    private void init() {
      View view = LayoutInflater.from(_context).inflate(R.layout.my_user_item, null);
      addView(view);
      ViewUtils.inject(this, view);
    }

    public void setData(MLMyUserData data) {
      if (data == null) {
        return;
      }

      try {
        _nameTv.setText(data.name);
        String maskNumber =
            data.phone.substring(0, 3) + "****" + data.phone.substring(7, data.phone.length());
        _phoneTv.setText(maskNumber);
      } catch (Exception e) {
      }
    }
  }


}
