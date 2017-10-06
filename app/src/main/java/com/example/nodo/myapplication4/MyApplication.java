package com.example.nodo.myapplication4;

import android.app.Application;

import com.example.nodo.myapplication4.dagger.component.DaggerNetComponent;
import com.example.nodo.myapplication4.dagger.component.NetComponent;
import com.example.nodo.myapplication4.dagger.module.AppModule;
import com.example.nodo.myapplication4.dagger.module.NetModule;

/**
 * Created by nodo on 28/09/17.
 */

public class MyApplication extends Application {

    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule("https://api.github.com/"))
                .build();
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}