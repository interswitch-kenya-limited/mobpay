package com.interswitchgroup.mobpaylib.di;

import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.di.vms.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(
        modules = {
                NetModule.class
        })
public interface MainComponent {
    void inject(MobPay mobPay);
}
