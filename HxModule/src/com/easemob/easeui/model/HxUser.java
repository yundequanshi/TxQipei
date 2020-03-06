package com.easemob.easeui.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by Marcello on 2015/12/16.
 * Description
 */
public class HxUser implements Serializable {
    @Expose
    public String createTime;
    @Expose
    public String emId;
    @Expose
    public String emPwd;
    @Expose
    public String id;
    @Expose
    public String kid;
    @Expose
    public String name;
    @Expose
    public String photo = "";
    @Expose
    public String userId;
    @Expose
    public String userType;

    //是否是好友
    @Expose
    public int flag = 0;

    @Expose
    public boolean isNoUserId = false;
    /**
     * 昵称首字母
     */
    public String initialLetter;

    public boolean isCheck;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof HxUser) {
            HxUser u = (HxUser) obj;
            return this.emId.equals(u.emId)
                    && this.userId.equals(u.userId);
        }
        return super.equals(obj);
    }
}
