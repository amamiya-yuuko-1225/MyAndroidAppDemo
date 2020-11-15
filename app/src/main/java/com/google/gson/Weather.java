package com.google.gson;;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("status")
    public String status;
    public Now now;
}
