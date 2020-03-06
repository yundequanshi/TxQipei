package com.qipei.found;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.adapter.MLAccidentAdd1Adapter;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import com.zuomei.widget.ScrollGridView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.photo.MLPhotoUtil;
import cn.ml.base.utils.photo.MLPicData;

/**
 * Created by Marcello on 2015/6/6.
 */
public class MLAccidentAdd1Aty extends BaseActivity {


    @ViewInject(R.id.accident_grid)
    private ScrollGridView mGridPhoto;

    private MLAccidentAdd1Adapter mAdapter;

    private List<MLPicData> mPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qp_accident_add1);
        ViewUtils.inject(this);

        mPath = new ArrayList<MLPicData>();
        mAdapter = new MLAccidentAdd1Adapter(MLAccidentAdd1Aty.this, R.layout.item_accident_photo);
        mGridPhoto.setAdapter(mAdapter);
        mGridPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String s[] = {"删除"};
                AlertDialog builder = MLDialogUtils.getAlertDialog(MLAccidentAdd1Aty.this)
                        .setItems(s, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPath.remove(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .create();
                builder.show();
            }
        });
    }

    @OnClick({R.id.accident_tv_1, R.id.accident_tv_2})
    public void camera(View view) {

        if(mPath!=null&&mPath.size()==6){
            showMessage("最多填写6张照片");
            return;
        }

        if (view.getId() == R.id.accident_tv_1) {
            //相册
            MLPhotoUtil.choose(MLAccidentAdd1Aty.this, 0);
        } else {
            //拍照
            MLPhotoUtil.choose(MLAccidentAdd1Aty.this, 1);
        }
    }

    @OnClick(R.id.top_btn_left)
    public void backOnClick(View view) {
        finish();
    }


    @OnClick(R.id.accident_btn_next)
    public void nextOnClick(View view) {

        if(mPath==null||mPath.size()==0){
            showMessage("请至少上传一张照片");
            return;
        }

        List<String> mList = new ArrayList<String>();
        for(MLPicData pic : mPath){
            mList.add(pic.path);

        }


       // startAct(MLAccidentAdd1Aty.this,MLAccidentAdd2Aty.class,mPath);
        Intent intent = new Intent(MLAccidentAdd1Aty.this,MLAccidentAdd2Aty.class);

        intent.putExtra("data", (Serializable) mList);
        startActivity(intent);
        finish();
    }




    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(MLPhotoUtil.IsCancel()&&requestCode!=100){
        showMessage("操作已取消!");
            return;
        }
        Bitmap b = MLPhotoUtil.gePhotoBitmap();
        if (requestCode == 100 && data != null) {
//相册选择返回
            MLPhotoUtil.photoZoom(data.getData());
        } else if (requestCode == 101) {
//拍照返回 调用裁剪
            MLPhotoUtil.photoZoom(null);
        }
        else if(requestCode == 102 && resultCode != 0) {
//裁剪返回
            MLPicData pic = new MLPicData();
            pic.mBitMap = MLPhotoUtil.gePhotoBitmap();
            pic.path = MLPhotoUtil.getPhotoPath();
            mPath.add(pic);
            mAdapter.setData(mPath);

        }
    }
}
