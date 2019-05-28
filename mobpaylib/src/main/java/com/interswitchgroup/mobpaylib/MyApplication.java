package com.interswitchgroup.mobpaylib;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.interswitchgroup.mobpaylib.di.AppComponent;
import com.interswitchgroup.mobpaylib.di.AppModule;
import com.interswitchgroup.mobpaylib.di.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class MyApplication extends DaggerApplication {

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        AppComponent appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        return appComponent;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}