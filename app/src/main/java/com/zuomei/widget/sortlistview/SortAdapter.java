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
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseLayout;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLHomeCatalogData;

import java.util.List;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<MLHomeCatalogData> list = null;
    private Context mContext;

    public SortAdapter(Context mContext, List<MLHomeCatalogData> list) {
        this.mContext = mContext;
        this.list = list;
    }

    public void updateListView(List<MLHomeCatalogData> list) {
        this.list = list;
        notifyDataSetChanged();
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
        final MLHomeCatalogData mContent = list.get(position);

        SortItemView item = null;
        if (view == null) {
            item = new SortItemView(mContext);
            view = item;
        } else {
            item = (SortItemView) view;
        }
        item.setData(mContent, position);
        return item;
        /*if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.test_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.ivHead = (ImageView) view.findViewById(R.id.car_iv_head);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		int section = getSectionForPosition(position);
		
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.sortLetters);
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
	
		MLHomeCatalogData data = list.get(position);
		viewHolder.tvTitle.setText(data.name);
		String imgUrl = APIConstants.API_IMAGE+"?id="+data.imageId;
		 BaseApplication.IMAGE_CACHE.get(imgUrl, viewHolder.ivHead);*/

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

    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.test_item, null);
            tvTitle = (TextView) view.findViewById(R.id.title);
            tvLetter = (TextView) view.findViewById(R.id.catalog);
            ivHead = (ImageView) view.findViewById(R.id.car_iv_head);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addView(view, params);
        }

        public void setData(MLHomeCatalogData data, int position) {
            int section = getSectionForPosition(position);

            if (position == getPositionForSection(section)) {
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText(data.sortLetters);
            } else {
                tvLetter.setVisibility(View.GONE);
            }
            tvTitle.setText(data.name);
            String imgUrl = APIConstants.API_IMAGE + "?id=" + data.imageId;
            //		bitmapUtils.display(ivHead, imgUrl);
            ivHead.setTag(imgUrl);
            if (!BaseApplication.IMAGE_CACHE.get(imgUrl, ivHead)) {
                ivHead.setImageDrawable(null);
            }
        }

    }

}