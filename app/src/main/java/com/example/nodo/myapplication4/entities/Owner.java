package com.example.nodo.myapplication4.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nodo on 28/09/17.
 */

public class Owner implements Serializable{

    @SerializedName("login")
    public String login;

    @SerializedName("avatar_url")
    public String avatarUrl;

    @SerializedName("id")
    public String id;

}
