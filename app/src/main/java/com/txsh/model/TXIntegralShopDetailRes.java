package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

/**
 * Created by Marcello on 2015/6/25.
 */
public class TXIntegralShopDetailRes extends MLBaseResponse {

    @Expose
    public TXIntegralShopDataRes datas;

    public class TXIntegralShopDataRes{
        @Expose
        public String content;
        @Expose
        public String phone;
        @Expose
        public String title;
        @Expose
        public int price;
        @Expose
        public String images;
    }

}
