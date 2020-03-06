package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marcello on 2015/6/17.
 */
public class TXInfoRes extends MLBaseResponse {

    @Expose
    public List<TXInfoData> datas;

    public class TXInfoData implements Serializable{
        @Expose
        public String id;
        @Expose
        public String createTime;
        @Expose
        public String title;
        @Expose
        public String imagePath;
        @Expose
        public String url;
    }



}
