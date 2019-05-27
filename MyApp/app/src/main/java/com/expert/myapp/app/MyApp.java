package com.expert.myapp.app;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApp extends Application {

    private static  MyApp instance;

    public MyApp(){
        super();
        instance = this;
    }

    public static MyApp getApp(){
        return instance;
    }

    public static Context getContext(){
        return  instance;
    }

    @Override

    public void onCreate() {

        super.onCreate();

        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext())
                .name("newapp-android.realm").build();

        Realm.setDefaultConfiguration(config);

    }

}