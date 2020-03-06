package com.txsh.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by iscod on 2016/5/24.
 */
public class HxAppUserData implements Serializable {
    @Expose
    public String userName;
    @Expose
    public String name;
    @Expose
    public String headPic;
    @Expose
    public String hxUser;
    @Expose
    public String id;

}
