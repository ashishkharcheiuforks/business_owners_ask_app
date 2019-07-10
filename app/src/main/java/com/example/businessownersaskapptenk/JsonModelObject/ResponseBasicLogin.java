package com.example.businessownersaskapptenk.JsonModelObject;

import com.google.gson.annotations.SerializedName;

public class ResponseBasicLogin {

    @SerializedName("key")
    private String accessToken;

    @SerializedName("full_name")
    private String fullName;
    @SerializedName("avatar")
    private String avatar;



    public String getFullName() {
        return fullName;
    }

    public String getAvatar() {
        return avatar;
    }


    public String getAccessToken() {
        return accessToken;
    }



}
