package com.qipei.interact.selectVideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.ml.base.MLAdapterBase;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qipei.interact.ShowVideoActivity;
import com.txsh.R;


/**
 * Created by Marcello on 2015/8/5.
 */
public class SelectVideoGvAdapter extends MLAdapterBase<VideoInfo> {

    @ViewInject(R.id.iv_select)
    private ImageView ivIocn;
    @ViewInject(R.id.btn_play)
    private ImageView btnPlay;
    @ViewInject(R.id.btn_select)
    private TextView btnSelect;

    public SelectVideoGvAdapter(Context context, int viewXml) {
        super(context, viewXml);
    }

    @Override
    protected void setItemData(View view, final VideoInfo data, int position) {
        ViewUtils.inject(this, view);
        //视频
        final String mDataVideo = data.getPath();
        Glide.with(mContext)
                .load(data.getImgPath())
                .asBitmap()
                .into(ivIocn);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowVideoActivity.class);
                intent.putExtra("localpath", mDataVideo);
                intent.putExtra("secret", "");
                intent.putExtra("remotepath", "");
                mContext.startActivity(intent);
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getLength() <= 20971520) {
                    btnSelect.setText("选中");
                    btnSelect.setTextColor(Color.RED);
                    Intent intent = new Intent();
                    intent.putExtra("path", mDataVideo);
                    ((Activity) mContext).setResult(1001, intent);
                    ((Activity) mContext).finish();
                } else {
                    Toast.makeText(mContext, "文件过大，无法上传", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
