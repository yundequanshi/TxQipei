package com.qipei.interact.selectVideo;

/**
 * Created by 汉玉 on 2016/11/15.
 */
public class VideoInfo {

    private String displayName;

    private String imgPath;

    private String path;

    private long length = 0;

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }


    public void setLength(long length) {
        this.length = length;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getLength() {
        return length;
    }

    public String getImgPath() {
        return imgPath;
    }
}
