package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/23.
 */
public class TXOrderDetailRes extends MLBaseResponse {
    @Expose
    public OrderDetail datas;

    public class OrderDetail extends MLBaseResponse {
        @Expose
        public String  detail;
        @Expose
        public String   address;
        @Expose
        public String  companyMobile;
        @Expose
        public String  orderFormState;
        @Expose
        public String  orderTime;
        @Expose
        public String  companyName;
        @Expose
        public String  productId;
        @Expose
        public String  productFreight;
        @Expose
        public String  id;
        @Expose
        public String  productNumber;

        @Expose
        public String  companyId;
        @Expose
        public String  productName;
        @Expose
        public String  productPrice;
        @Expose
        public String  orderId;
        @Expose
        public String  productPicture;
        @Expose
        public String  sumPrice;
        @Expose
        public String  depotName;


    }




}
