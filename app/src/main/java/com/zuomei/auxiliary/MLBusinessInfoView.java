package com.zuomei.auxiliary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseLayout;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLHomeProductData;

public class MLBusinessInfoView extends BaseLayout {

    private Context _context;

    public MLBusinessInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _context = context;
        init();
    }

    public MLBusinessInfoView(Context context) {
        super(context);
        _context = context;
        init();
    }

    @ViewInject(R.id.catalog_tv_name)
    private TextView _nameTv;

    @ViewInject(R.id.catalog_iv)
    private ImageView _imageIv;

    private void init() {
        View view = LayoutInflater.from(_context).inflate(R.layout.business_product_item, null);
        addView(view);
//		LayoutParams param = new LayoutParams(Rela);
        ViewUtils.inject(this, view);
    }

    public void setData(MLHomeProductData image) {
        _nameTv.setText(image.title);
        String imgUrl = APIConstants.API_IMAGE_SHOW  + image.image;
        //	 bitmapUtils.display(_imageIv, imgUrl, bigPicDisplayConfig);
        BaseApplication.IMAGE_CACHE.get(imgUrl, _imageIv);
    }
}
