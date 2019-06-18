package com.interswitchgroup.mobpaylib.di;

import android.app.Application;

import com.interswitchgroup.mobpaylib.di.vms.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
        AndroidSupportInjectionModule.class,
        AndroidInjectionModule.class,
        AppModule.class,
        ViewModelModule.class,
        ActivityModule.class
})
@Singleton
public interface AppComponent extends AndroidInjector<Application> {
    @Override
    void inject(Application instance);
}