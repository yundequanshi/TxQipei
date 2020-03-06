package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marcello on 2015/6/25.
 */
public class TXIntegralShopConvertRes extends MLBaseResponse {

    @Expose
    public TXIntegralShopData datas;

    public class TXIntegralShopData{
        @Expose
        public String phone;
        @Expose
        public List<TXIntegralConvert> list;
        @Expose
        public String dlogo;
        @Expose
        public String dName;
        @Expose
        public int integral;
    }

    public class TXIntegralConvert implements Serializable{
        @Expose
        public String id;
        @Expose
        public String createTime;
        @Expose
        public String title;
        @Expose
        public int price;
        @Expose
        public String images;
    }
}
