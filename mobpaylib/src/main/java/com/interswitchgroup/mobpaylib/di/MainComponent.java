package com.interswitchgroup.mobpaylib.di;

import com.interswitchgroup.mobpaylib.MobPay;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {NetModule.class})
public interface MainComponent {
  void inject(MobPay mobPay);
}
