package com.example.nodo.myapplication4.dagger.component;

import com.example.nodo.myapplication4.MainActivity;
import com.example.nodo.myapplication4.RepositoryActivity;
import com.example.nodo.myapplication4.dagger.module.AppModule;
import com.example.nodo.myapplication4.dagger.module.NetModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by nodo on 03/10/17.
 */

@Singleton
@Component(modules = {AppModule.class, NetModule.class})

public interface NetComponent {
    void inject(MainActivity activity);
    void inject(RepositoryActivity activity);
}