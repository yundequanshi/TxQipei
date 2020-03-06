package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marcello on 2015/6/25.
 */
public class TXIntegralShopRes extends MLBaseResponse {

    @Expose
    public TXIntegralShopData datas;

    public class TXIntegralShopData{
        @Expose
        public String groomProductId;
        @Expose
        public List<TXIntegralProduct> list;
        @Expose
        public String groomImage;
        @Expose
        public int integral;
    }

    public class TXIntegralProduct implements Serializable{
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
