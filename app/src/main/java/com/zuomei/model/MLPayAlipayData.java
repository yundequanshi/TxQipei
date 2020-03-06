package com.zuomei.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Administrator on 2015/6/6.
 */
public class MLPayAlipayData  extends MLBaseResponse{
    @Expose
    public String payInfo;
    @Expose
    public String retcode;
    @Expose
    public String retmsg;

    @Expose
    public String out_trade_no;
//    @Expose
//    public String rsa_public;
}
