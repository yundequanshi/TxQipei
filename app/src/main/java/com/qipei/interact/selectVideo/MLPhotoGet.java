package com.qipei.interact.selectVideo;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import cn.ml.base.utils.MLFolderUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 汉玉 on 2016/11/16.
 */
public class MLPhotoGet {

    public static String getPath(Context context,String path) {

        Bitmap bitmap = null;
        FileOutputStream fos = null;

        File mDataVideoImage = new File(MLFolderUtils.getDiskFile(context,"video"),
                "thvideo" + System.currentTimeMillis() + ".jpg");
        try {
            if (!mDataVideoImage.getParentFile().exists()) {
                mDataVideoImage.getParentFile().mkdirs();
            }
            bitmap = ThumbnailUtils.createVideoThumbnail(path, 3);
            fos = new FileOutputStream(mDataVideoImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            return mDataVideoImage.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
            }
            if (bitmap != null) {
                bitmap = null;
            }
        }
        return null;
    }
}
