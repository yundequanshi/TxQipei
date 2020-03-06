package com.txsh.utils;

import android.content.Context;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import com.alterview.AlertView;
import com.alterview.OnItemClickListener;
import java.util.List;

/**
 * Created by lingyun on 2016/9/24.
 */
public class LXPhoto {

  public static final int REQUEST_CODE_CAMERA = 2000;
  public static final int REQUEST_CODE_GALLERY = 2001;

  /**
   * 固定大小正方形裁剪
   */
  public static void getGudingPhoto(Context context,
      final GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback) {
    ThemeConfig themeConfig = ThemeConfig.DARK;
    FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
    cn.finalteam.galleryfinal.ImageLoader imageLoader = new GlideImageLoader();
    PauseOnScrollListener pauseOnScrollListener = new GlidePauseOnScrollListener(false, true);
    functionConfigBuilder.setEnableEdit(true);//设置可编辑
    functionConfigBuilder.setEnableCrop(true);//设置裁剪
    functionConfigBuilder.setCropSquare(true);//设置正方形
    functionConfigBuilder.setCropWidth(600);//设置正方形宽度
    functionConfigBuilder.setCropHeight(600);//设置正方形高度
    functionConfigBuilder.setForceCrop(true);//设置是否强制裁剪
    final FunctionConfig functionConfig = functionConfigBuilder.build();
    CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, themeConfig)
        .setFunctionConfig(functionConfig)
        .setPauseOnScrollListener(pauseOnScrollListener)
        .setNoAnimcation(true)
        .build();
    GalleryFinal.init(coreConfig);
    new AlertView("上传图片", null, "取消", null,
        new String[]{"拍照", "从相册中选择"},
        context, AlertView.Style.ActionSheet, new OnItemClickListener() {
      @Override
      public void onItemClick(Object o, int position) {
        switch (position) {
          //拍照
          case 0:
            GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig,
                mOnHanlderResultCallback);
            break;
          //从相册中选择
          case 1:
            GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig,
                mOnHanlderResultCallback);
            break;
        }
      }
    }).show();
  }

  /**
   * 自由裁剪
   */
  public static void getZiyouPhoto(Context context,
      final GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback) {
    ThemeConfig themeConfig = ThemeConfig.DEFAULT;
    FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
    cn.finalteam.galleryfinal.ImageLoader imageLoader = new GlideImageLoader();
    PauseOnScrollListener pauseOnScrollListener = new GlidePauseOnScrollListener(false, true);
    functionConfigBuilder.setEnableEdit(true);//设置可编辑
    functionConfigBuilder.setEnableCrop(true);//设置裁剪
    functionConfigBuilder.setForceCrop(true);//设置是否强制裁剪
    functionConfigBuilder.setCropWidth(960);//设置宽度
    functionConfigBuilder.setCropHeight(1280);//设置高度
    final FunctionConfig functionConfig = functionConfigBuilder.build();
    CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, themeConfig)
        .setFunctionConfig(functionConfig)
        .setPauseOnScrollListener(pauseOnScrollListener)
        .setNoAnimcation(true)
        .build();
    GalleryFinal.init(coreConfig);
    new AlertView("上传头像", null, "取消", null,
        new String[]{"拍照", "从相册中选择"},
        context, AlertView.Style.ActionSheet, new OnItemClickListener() {
      @Override
      public void onItemClick(Object o, int position) {
        switch (position) {
          //拍照
          case 0:
            GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig,
                mOnHanlderResultCallback);
            break;
          //从相册中选择
          case 1:
            GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig,
                mOnHanlderResultCallback);
            break;
        }
      }
    }).show();
  }

  public static void getMorePhoto(Context context, List<PhotoInfo> mPhotoList,
      final GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback) {

    ThemeConfig themeConfig = ThemeConfig.DARK;
    FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
    cn.finalteam.galleryfinal.ImageLoader imageLoader = new GlideImageLoader();
    PauseOnScrollListener pauseOnScrollListener = new GlidePauseOnScrollListener(false, true);
    functionConfigBuilder.setMutiSelectMaxSize(9);
    functionConfigBuilder.setEnableEdit(true);//设置可编辑
    functionConfigBuilder.setEnableCrop(true);//设置裁剪
    functionConfigBuilder.setCropSquare(true);
    functionConfigBuilder.setSelected(mPhotoList);//添加过滤集合
    final FunctionConfig functionConfig = functionConfigBuilder.build();
    CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, themeConfig)
        .setFunctionConfig(functionConfig)
        .setPauseOnScrollListener(pauseOnScrollListener)
        .setNoAnimcation(true)
        .build();
    GalleryFinal.init(coreConfig);
    new AlertView("选择图片", null, "取消", null,
        new String[]{"拍照", "从相册中选择"},
        context, AlertView.Style.ActionSheet, new OnItemClickListener() {
      @Override
      public void onItemClick(Object o, int position) {
        switch (position) {
          //拍照
          case 0:
            GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig,
                mOnHanlderResultCallback);
            break;
          //从相册中选择
          case 1:
            GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig,
                mOnHanlderResultCallback);
            break;
        }
      }
    }).show();

  }
}
