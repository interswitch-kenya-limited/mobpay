package com.interswitchgroup.mobpaylib.di;

import android.app.Application;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class AppModule {
  Application mApplication;

  public AppModule(Application mApplication) {
    this.mApplication = mApplication;
  }

  @Provides
  @Singleton
  Application provideApplication() {
    return mApplication;
  }
}
