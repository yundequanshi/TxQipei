package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marcello on 2015/6/25.
 */
public class TXShopOrderRes extends MLBaseResponse {

    @Expose
    public List<TXShopOrderData> datas;

    public class TXShopOrderData implements Serializable{
        @Expose
        public String productNumber;
        @Expose
        public String id;
        @Expose
        public String orderFormState;
        @Expose
        public String orderTime;
        @Expose
        public String productPrice;
        @Expose
        public String productName;
        @Expose
        public String orderId;
        @Expose
        public double sumPrice;
        @Expose
        public String productFreight;
        @Expose
        public String productPicture;
        @Expose
        public String productId;
        @Expose
        public String payType;
    }

}
