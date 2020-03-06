package com.zuomei.utils;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class ZMImageCache implements ImageCache{
	private LruCache<String, Bitmap> cache;
    public ZMImageCache(int maxSize) {
        super();
        cache=new LruCache<String, Bitmap>(maxSize);
    }
    @Override
    public Bitmap getBitmap(String url) {
        return cache.get(url);//在缓存里面寻找是否有
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        cache.put(url, bitmap);//把图片存进缓存
    }
}
