package com.example.nodo.myapplication4.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nodo on 28/09/17.
 */

public class Repository implements Serializable{

    @SerializedName("name")
    public String name;

    @SerializedName("url")
    public String url;

    @SerializedName("owner")
    public Owner owner;

}
