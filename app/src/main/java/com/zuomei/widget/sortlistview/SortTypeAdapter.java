package com.zuomei.widget.sortlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.txsh.R;
import com.txsh.model.TXShopTypeListRes.TXHomeGoodsTypeImageData;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseLayout;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLHomeCityData;
import java.util.List;

public class SortTypeAdapter extends BaseAdapter implements SectionIndexer {

  private List<TXHomeGoodsTypeImageData> list = null;
  private Context mContext;

  public SortTypeAdapter(Context mContext, List<TXHomeGoodsTypeImageData> list) {
    this.mContext = mContext;
    this.list = list;
  }

  public int getCount() {
    return this.list.size();
  }

  public Object getItem(int position) {
    return list.get(position);
  }

  public long getItemId(int position) {
    return position;
  }

  public View getView(final int position, View view, ViewGroup arg2) {
    final TXHomeGoodsTypeImageData mContent = list.get(position);

    SortItemView item = null;
    if (view == null) {
      item = new SortItemView(mContext);
      view = item;
    } else {
      item = (SortItemView) view;
    }
    item.setData(mContent, position);
    return item;
  }

  public int getSectionForPosition(int position) {
    return list.get(position).sortLetters.charAt(0);
  }

  public int getPositionForSection(int section) {
    for (int i = 0; i < getCount(); i++) {
      String sortStr = list.get(i).sortLetters;
      char firstChar = sortStr.toUpperCase().charAt(0);
      if (firstChar == section) {
        return i;
      }
    }

    return -1;
  }


  @Override
  public Object[] getSections() {
    return null;
  }

  class SortItemView extends BaseLayout {

    public SortItemView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      init();
    }

    public SortItemView(Context context, AttributeSet attrs) {
      super(context, attrs);
      init();
    }

    public SortItemView(Context context) {
      super(context);
      init();
    }

    private TextView tvTitle;
    private TextView tvLetter;
    private ImageView ivHead;

    private void init() {
      View view = LayoutInflater.from(mContext).inflate(R.layout.item_city_sort, null);
      tvTitle = (TextView) view.findViewById(R.id.title);
      tvLetter = (TextView) view.findViewById(R.id.catalog);
      ivHead = (ImageView) view.findViewById(R.id.car_iv_head);
      LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
      addView(view, params);
    }

    public void setData(TXHomeGoodsTypeImageData data, int position) {
      int section = getSectionForPosition(position);
      if (position == getPositionForSection(section)) {
        tvLetter.setVisibility(View.VISIBLE);
        tvLetter.setText(data.sortLetters);
      } else {
        tvLetter.setVisibility(View.GONE);
      }
      tvLetter.setClickable(false);
      tvTitle.setText(data.name);
      String imgUrl = APIConstants.API_IMAGE;
      ivHead.setTag(imgUrl);
      if (!BaseApplication.IMAGE_CACHE.get(imgUrl, ivHead)) {
        ivHead.setImageDrawable(null);
      }
    }

  }

}