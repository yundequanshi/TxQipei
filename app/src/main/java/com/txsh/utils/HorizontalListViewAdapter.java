package com.txsh.utils;

import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.utils.BitmapUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HorizontalListViewAdapter extends BaseAdapter {
	private String images;
	private Context mContext;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	private int selectIndex = -1;
	final String imageId[];

	public HorizontalListViewAdapter(Context context, String images) {
		this.mContext = context;
		this.images = images;
		imageId = this.images.split(",");
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);// LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return imageId.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater
					.inflate(R.layout.horizontal_list_item, null);
			holder.mImage1 = (ImageView) convertView
					.findViewById(R.id.img_list_item1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == selectIndex) {
			convertView.setSelected(true);
		} else {
			convertView.setSelected(false);
		}

		// holder.mTitle.setText(mTitles[position]);
		// iconBitmap = getPropThumnail(mIconIDs[position]);
		// holder.mImage.setImageBitmap(iconBitmap);
		String imgUrl1 = APIConstants.API_IMAGE + "?id=" + imageId[position];
		// bitmapUtils.display(iv, imgUrl, bigPicDisplayConfig);
		// BaseApplication.IMAGE_CACHE.get(imgUrl1, holder.mImage1);
		holder.mImage1.setTag(imgUrl1);
		if (!BaseApplication.IMAGE_CACHE.get(imgUrl1, holder.mImage1)) {
			holder.mImage1.setImageResource(R.drawable.sgc_photo);
		}

		return convertView;
	}

	private static class ViewHolder {
		private ImageView mImage1;
	}
}