package com.zuomei.utils;import android.content.Context;import android.graphics.Bitmap;import java.io.IOException;import java.io.InputStream;import java.lang.ref.ReferenceQueue;import java.lang.ref.WeakReference;import java.util.Hashtable;public class BitmapResoureManager {    private class BitmapRef extends WeakReference<Bitmap> {        public String _key = "";        private BitmapRef(int resid, Bitmap bitmap, ReferenceQueue<Bitmap> q) {            super(bitmap, q);            _key = String.valueOf(resid);        }    }    Hashtable<String, BitmapRef> mapBitmapRef;    ReferenceQueue<Bitmap> rq;    private BitmapResoureManager(Context c) {        _context = c;        mapBitmapRef = new Hashtable<String, BitmapRef>();        rq = new ReferenceQueue<Bitmap>();    }    private static BitmapResoureManager brm;    private Context _context;    public static BitmapResoureManager Instance(Context c) {        if (brm == null) {            brm = new BitmapResoureManager(c);        }        return brm;    }    private void cleanCache() {        BitmapRef ref = null;        while ((ref = (BitmapRef) rq.poll()) != null) {            mapBitmapRef.remove(ref._key);        }    }    private Bitmap cacheBitmapResource(int resid) {        cleanCache();        InputStream is = _context.getResources().openRawResource(resid);        Bitmap bitmap = BitmapUtil.decodeStream(is);        try {            is.close();        } catch (IOException e) {            // TODO Auto-generated catch block            e.printStackTrace();        }        BitmapRef ref = new BitmapRef(resid, bitmap, rq);        mapBitmapRef.put(ref._key, ref);        bitmap = null;        return (Bitmap) mapBitmapRef.get(ref._key).get();    }    public Bitmap getBitmapResource(int resid) {        Bitmap bitmap = null;        BitmapRef ref = null;        if (mapBitmapRef.containsKey(String.valueOf(resid))) {            ref = mapBitmapRef.get(String.valueOf(resid));            bitmap = (Bitmap) ref.get();        }        if (bitmap == null) {            return cacheBitmapResource(resid);        } else {            return bitmap;        }    }}