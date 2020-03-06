package com.txsh.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.TXShopCarData;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;

import cn.ml.base.MLAdapterBase;
import cn.ml.base.utils.MLStrUtil;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2015/7/21.
 */
public class TXShopCarAdapter extends MLAdapterBase<TXShopCarData.TXShopCarDetailData> {
    public TXShopCarAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }

    @ViewInject(R.id.shopcar_item_add)
    private ImageButton mBtnAdd;
    @ViewInject(R.id.shopcar_item_subtract)
    private ImageButton mBtnSub;
    @ViewInject(R.id.shopcar_item_ed)
    private EditText mEtCount;

    @ViewInject(R.id.shopcar_item_rb)
    private CheckBox mCheckBox;

    //商品名称
    @ViewInject(R.id.shopcar_item_name)
    private TextView mpname;

    //钱数
    @ViewInject(R.id.shopcar_item_num)
    private TextView mpfactoryprice;

    //图片
    @ViewInject(R.id.shopcar_item_iv)
    private ImageView mshopIv;

    //库存
    @ViewInject(R.id.orderpay_item_Stock)
    private TextView mpstock;

    //选择
    private boolean mCanChoose;

    public void setChoose(boolean b) {
        mCanChoose = b;
        notifyDataSetChanged();
    }

    @Override
    protected void setItemData(View view, final TXShopCarData.TXShopCarDetailData data, int position) {
        ViewUtils.inject(this, view);
        //   String imageUrl = data.picpath;
        String imageUrl = APIConstants.API_IMAGE_SHOW+data.imgPath;
        mshopIv.setTag(imageUrl);
        if (!BaseApplication.IMAGE_CACHE.get(imageUrl, mshopIv)) {
        }
        mpname.setText(data.productName);
         mpfactoryprice.setText("￥ "+data.privce);
         mpstock.setText(data.productFreight);
        mEtCount.setText(data.number + "");
        mCheckBox.setChecked(data.isChoose);


        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.buyednum=Integer.valueOf(data.number);
                if (MLStrUtil.isEmpty(data.buyednum + "") || !MLStrUtil.isNumber(data.buyednum + ""))
                    return;
                data.buyednum++;
                data.number=String.valueOf(data.buyednum);
                data.addplus="add";
               // data.position=position;
                EventBus.getDefault().post(data);
                notifyDataSetChanged();
            }
        });

        mBtnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.buyednum=Integer.valueOf(data.number);
                if (MLStrUtil.isEmpty(data.buyednum + "") || !MLStrUtil.isNumber(data.buyednum + "") ||
                        MLStrUtil.compare(data.buyednum + "", "1")) {
                    return;
                }

                data.buyednum--;
                data.number=String.valueOf(data.buyednum);
                data.addplus="plus";
            //    data.position=position;
                EventBus.getDefault().post(data);
                notifyDataSetChanged();
            }


        });
    }
}
