package com.interswitchgroup.mobpaylib.di;

import com.interswitchgroup.mobpaylib.ui.MobPayActivity;
import com.interswitchgroup.mobpaylib.ui.fragments.card.CardPaymentFragment;
import com.interswitchgroup.mobpaylib.ui.fragments.mobile.MobilePaymentFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
  @ContributesAndroidInjector
  abstract CardPaymentFragment provideCardPaymentFragment();

  @ContributesAndroidInjector
  abstract MobilePaymentFragment provideMobilePaymentFragment();

  @ContributesAndroidInjector
  abstract MobPayActivity provideMobPayActivity();
}
