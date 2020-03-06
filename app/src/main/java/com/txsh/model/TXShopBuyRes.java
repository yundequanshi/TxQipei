package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marcello on 2015/6/25.
 */
public class TXShopBuyRes extends MLBaseResponse {

    @Expose
    public TXShopBuyData datas;

    public class TXShopBuyData implements Serializable{
        @Expose
        public String allPrice;
        @Expose
        public List<TXShopProductData> products;
        @Expose
        public String money;
        @Expose
        public TXShopAddressData address;
    }



    public class TXShopAddressData  implements Serializable{

        @Expose
        public String addressId;
        @Expose
        public String address;
        @Expose
        public String name;
        @Expose
        public String mobile;
    }


    public class TXShopProductData implements Serializable{
        @Expose
        public String productNumber;
        @Expose
        public String productPrice;
        @Expose
        public String productName;
        @Expose
        public String productFreight;
        @Expose
        public String productPicture;
        @Expose
        public String productId;
    }
}
