package com.example.nodo.myapplication4;

import com.example.nodo.myapplication4.entities.Download;
import com.example.nodo.myapplication4.entities.Repositories;
import com.example.nodo.myapplication4.entities.Repository;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by nodo on 28/09/17.
 */

public interface RepositoryInterface {

    @GET("search/repositories")
    Observable<Repositories> getRepos(@Query("q") String sort);

    @GET("repos/{first}/{second}/readme")
    Observable<Download> getDownload(@Path("first") String first, @Path("second") String second);

}




