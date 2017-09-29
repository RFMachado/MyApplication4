package com.example.nodo.myapplication4.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nodo on 28/09/17.
 */

public class Repositories implements Serializable {

    @SerializedName("items")
    public List<Repository> items;
}
