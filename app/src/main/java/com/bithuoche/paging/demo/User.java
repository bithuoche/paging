package com.bithuoche.paging.demo;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    public long userId;

    @SerializedName("login")
    public String firstName;

    public String address;
}
