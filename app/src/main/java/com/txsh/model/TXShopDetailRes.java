package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;

/**
 * Created by Marcello on 2015/6/25.
 */
public class TXShopDetailRes extends MLBaseResponse {

    @Expose
    public TXShopDetailData datas;


    public class TXShopDetailData implements Serializable{
        @Expose
        public String id;
        @Expose
        public String privce;
        @Expose
        public String count;
        @Expose
        public String oldprivce;
        @Expose
        public String sales;
        @Expose
        public String name;
        @Expose
        public String freight;
        @Expose
        public String shelveState;
        @Expose
        public String evaluationCount;
        @Expose
        public String images;

        @Expose
        public String cityName;

        @Expose
        public String companyId;
        @Expose
        public String companyName;
        @Expose
        public String companyAddress;
        @Expose
        public String companyPhone;
        @Expose
        public String companyLogo;
        @Expose
        public String hxUser;




    }
}
