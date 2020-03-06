package com.txsh.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by lingyun on 2016/1/27.
 */
public class APliayData implements Serializable {

    @Expose
    public String alipay;

    @Expose
    public String orderId;

    @Expose
    public String recordId;
}
