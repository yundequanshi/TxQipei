package com.zuomei.auxiliary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitpopupwindow.DensityUtils;
import com.fitpopupwindow.FitPopupWindow;
import com.fitpopupwindow.ScreenUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.AdapterBase;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseLayout;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLBill2List;

public class MLMyBill2ListAdapter extends AdapterBase<MLBill2List> {

  private Context _context;

  public MLMyBill2ListAdapter(Context _context) {
    super();
    this._context = _context;
  }

  @Override
  protected View getExView(int position, View view, ViewGroup parent) {

    MLBill2ItemView item = null;
    if (view == null) {
      item = new MLBill2ItemView(_context);
      view = item;
    } else {
      item = (MLBill2ItemView) view;
    }
    MLBill2List data = (MLBill2List) getItem(position);
    item.setData(data);
    return item;
  }


  public class MLBill2ItemView extends BaseLayout {

    private Context _context;

    public MLBill2ItemView(Context context, AttributeSet attrs) {
      super(context, attrs);
      _context = context;
      init();
    }

    public MLBill2ItemView(Context context) {
      super(context);
      _context = context;
      init();
    }

    private Handler _callHandler;

    public MLBill2ItemView(Context context, Handler callHandler) {
      super(context);
      _callHandler = callHandler;
      _context = context;
      init();
    }


    @ViewInject(R.id.home_business_iv)
    private ImageView _headIv;


    @ViewInject(R.id.tv_name)
    private TextView _nameTv;


    @ViewInject(R.id.tv_time)
    private TextView _timeTv;

    @ViewInject(R.id.tv_state)
    private TextView _stateTv;

    private void init() {
      View view = LayoutInflater.from(_context).inflate(R.layout.my_bill2_item, null);
      addView(view);
      ViewUtils.inject(this, view);

    }

    public void setData(final MLBill2List data) {
      if (data == null) {
        return;
      }
      String imgUrl = APIConstants.API_IMAGE + "?id=" + data.logo;

      _headIv.setTag(imgUrl);
      if (!BaseApplication.IMAGE_CACHE.get(imgUrl, _headIv)) {
        _headIv.setImageResource(R.drawable.chanpinzhanshitp);
      }
      _nameTv.setText(data.companyName);
      _timeTv.setText("发货日期:" + data.billDate);
      if (data.billState.equalsIgnoreCase("通过")) {
        _stateTv.setBackgroundColor(Color.parseColor("#4CC222"));
        _stateTv.setText(data.billState + " +" + data.source + "分");
        _stateTv.setClickable(false);
      } else if (data.billState.equalsIgnoreCase("未通过")) {
        _stateTv.setBackgroundColor(Color.parseColor("#8D8D8D"));
        _stateTv.setText(data.billState);
        _stateTv.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            initPopup(v, data.refuseReason);
          }
        });
      } else {
        _stateTv.setBackgroundColor(Color.parseColor("#DC7248"));
        _stateTv.setText(data.billState);
        _stateTv.setClickable(false);
      }
    }
  }

  private void initPopup(View anchorView, String reasion) {
    View contentView = LayoutInflater.from(_context)
        .inflate(R.layout.item_popupwindow_two_car, null);
    FitPopupWindow fitPopupWindow = new FitPopupWindow((Activity) _context,
        ScreenUtils.getScreenWidth(_context) - DensityUtils.dp2px(20),
        ViewGroup.LayoutParams.WRAP_CONTENT
    );
    TextView tvReasion = (TextView) contentView.findViewById(R.id.tv_reason);
    tvReasion.setText(reasion);
    fitPopupWindow.setView(contentView, anchorView);
    fitPopupWindow.show();
  }
}
