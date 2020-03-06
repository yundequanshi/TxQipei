package com.easemob.easeui.model;

import com.google.gson.annotations.Expose;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Marcello on 2015/12/23.
 * Description
 */
public class TodoData implements Serializable{

    @Expose
    public String action;
    @Expose
    public String createTime;
    @Expose
    public String data;
    @Expose
    public String kid;
    @Expose
    public String userId;
    @Expose
    public String userName;

    public String groupName;
    public String groupKid;
    public String userKid;

    public void initGroup(){
        try {
            groupName = new JSONObject(data).getString("groupName");
            groupKid = new JSONObject(data).getString("groupKid");
            userKid = new JSONObject(data).getString("userKid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
