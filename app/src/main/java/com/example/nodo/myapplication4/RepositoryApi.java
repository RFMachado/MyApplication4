package com.example.nodo.myapplication4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by nodo on 28/09/17.
 */

public class RepositoryApi {

    public static Retrofit getClient() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();


        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    public static  RepositoryInterface conect() {
        return getClient().create(RepositoryInterface.class);
    }
}
