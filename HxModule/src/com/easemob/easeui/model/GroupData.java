package com.easemob.easeui.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Marcello on 2015/12/22.
 * Description
 */
public class GroupData implements Serializable{

    @Expose
    public String adminId;
    @Expose
    public String approval;
    @Expose
    public String createTime;
    @Expose
    public String desc;
    @Expose
    public String emgId;
    @Expose
    public String id;
    @Expose
    public String kid;
    @Expose
    public String maxUserCount;
    @Expose
    public String name;

    @Expose
    @SerializedName("public")
    public String isPublic;
}
