package com.zuomei.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.ab.util.AbImageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MLPhotoUtil {

/*	private static MLPhotoUtil INSTANCE;
	public static MLPhotoUtil  getInstance(){
		if(INSTANCE==null){
			INSTANCE = new MLPhotoUtil();
		}
		return INSTANCE;
	}*/
	private static Fragment _frg;
	
	 private static File tempFile =null;
	private static int crop = 600;
/*	  private File sdcardTempFile =new File(Environment.getExternalStorageDirectory(),
	             getPhotoFileName());*/
	public static void choose(Fragment frg,int which){
		_frg = frg;
		//初始化文件路径
		tempFile = new File(_frg.getActivity().getExternalCacheDir(),getPhotoFileName());
	    if (which == 1) {
//       	 选择拍照
       	  Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
             // 指定调用相机拍照后照片的储存路径
             cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
                     Uri.fromFile(tempFile));
             _frg.startActivityForResult(cameraintent, 101);
       	 
        } else {
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                intent.putExtra("output", Uri.fromFile(tempFile));
                intent.putExtra("crop", "true");
        /*        intent.putExtra("aspectX", 1);// 裁剪框比例
                intent.putExtra("aspectY", 1);*/
            /*    intent.putExtra("outputX", crop);// 输出图片大小
                intent.putExtra("outputY", crop);*/
                _frg.startActivityForResult(intent, 100);
        }
	}    
	/**
	 * 拍照完成处理
	 */
	public static  void photoZoom(){
		if(tempFile==null){
			return;
		}
		saveBitmap(tempFile,AbImageUtil.scaleImg(tempFile, 500, 500));
		
		/*Uri uri = Uri.fromFile(tempFile);  
		
		 Intent intent = new Intent("com.android.camera.action.CROP");
         intent.setDataAndType(uri, "image/*");
         // crop为true是设置在开启的intent中设置显示的view可以剪裁
         intent.putExtra("crop", "true");
         //保存路径
         intent.putExtra("output", Uri.fromFile(tempFile));
         
         // aspectX aspectY 是宽高的比例
         intent.putExtra("aspectX", 1);
         intent.putExtra("aspectY", 1);

         // outputX,outputY 是剪裁图片的宽高
         intent.putExtra("outputX", 600);
         intent.putExtra("outputY", 600);
    //     intent.putExtra("return-data", true);
         intent.putExtra("noFaceDetection", true);
         _frg.startActivityForResult(intent, 102);*/
	}
	
	public static String getPhotoPath(){
		return tempFile.getAbsolutePath();
	}
	
	
	/**
	 * 返回从相册选择的图片
	 * @return
	 */
	public static  Bitmap getAlbumBitmap(){
		  Bitmap bmp = BitmapFactory.decodeFile(tempFile.getAbsolutePath());
		  return bmp;
	}
	
	
	  // 使用系统当前日期加以调整作为照片的名称
    private static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
    
    public static void saveBitmap(  File f ,Bitmap bm) {
    	if(bm==null){
    		return;
    	}
    	if (f.exists()) {
 	    f.delete();
 	   }
 	   try {
 	    FileOutputStream out = new FileOutputStream(f);
 	    bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
 	    out.flush();
 	    out.close();
 	   } catch (FileNotFoundException e) {
 	    // TODO Auto-generated catch block
 	    e.printStackTrace();
 	   } catch (IOException e) {
 	    // TODO Auto-generated catch block
 	    e.printStackTrace();
 	   }

 	 }
}
