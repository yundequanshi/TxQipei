package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/7/23.
 */
public class TXShopPjData extends MLBaseResponse {
    @Expose
    public List< TXShopPjDataDetail> datas;

    public class TXShopPjDataDetail implements Serializable {
        @Expose
        public String createTime;
        @Expose
        public String detail;
        @Expose
        public String UserImage;
        @Expose
        public String state;
        @Expose
        public String userName;
    }

}



