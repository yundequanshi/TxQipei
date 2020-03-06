package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marcello on 2015/6/25.
 */
public class TXShopAddressRes extends MLBaseResponse {

    @Expose
    public List<TXShopAddressData> datas;



    public class TXShopAddressData implements Serializable{
        @Expose
        public String id;
        @Expose
        public String postalcode;
        @Expose
        public String address;
        @Expose
        public String location;
        @Expose
        public String name;
        @Expose
        public String isDefaultAddress;
        @Expose
        public String mobile;
    }
}
