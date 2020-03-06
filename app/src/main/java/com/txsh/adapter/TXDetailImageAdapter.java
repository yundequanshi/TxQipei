package com.txsh.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.txsh.R;
import com.zuomei.constants.APIConstants;
import com.zuomei.home.MLHomeProductPop;

public class TXDetailImageAdapter extends PagerAdapter {
	private List<View> list;
	final String imageIds[];
	Context context;
	public TXDetailImageAdapter(Context mcontext,String iamgeid[],List<View> list) {
		this.list = list;
		this.imageIds=iamgeid;
		this.context=mcontext;
	}

	@Override
	public int getCount() {

		if (list != null && list.size() > 0) {
			return list.size();
		} else {
			return 0;
		}
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		container.addView(list.get(position));
		
		list.get(position).findViewById(R.id.tx_detail_item_image1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 // TODO Auto-generated method stub
				 List<String> images = new ArrayList<String>();
				for (int i = 0; i < imageIds.length; i ++) {
					String imgUrl = APIConstants.API_IMAGE + "?id="+ imageIds[i];
					images.add(imgUrl);
				}
				
				 MLHomeProductPop _pop = new MLHomeProductPop(context,images,0);
				 _pop.showAtLocation(list.get(position).findViewById(R.id.tx_detail_item_image1), Gravity.CENTER, 0, 0);
			}
		});
		list.get(position).findViewById(R.id.tx_detail_item_image2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				List<String> images = new ArrayList<String>();
				for (int i = 0; i < imageIds.length; i ++) {
					String imgUrl = APIConstants.API_IMAGE + "?id="+ imageIds[i];
					images.add(imgUrl);
				}
				
				MLHomeProductPop _pop = new MLHomeProductPop(context,images,1);
				_pop.showAtLocation(list.get(position).findViewById(R.id.tx_detail_item_image1), Gravity.CENTER, 0, 0);
			}
		});
		list.get(position).findViewById(R.id.tx_detail_item_image3).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				List<String> images = new ArrayList<String>();
				for (int i = 0; i < imageIds.length; i ++) {
					String imgUrl = APIConstants.API_IMAGE + "?id="+ imageIds[i];
					images.add(imgUrl);
				}
				
				MLHomeProductPop _pop = new MLHomeProductPop(context,images,2);
				_pop.showAtLocation(list.get(position).findViewById(R.id.tx_detail_item_image1), Gravity.CENTER, 0, 0);
			}
		});
		return list.get(position);
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}
