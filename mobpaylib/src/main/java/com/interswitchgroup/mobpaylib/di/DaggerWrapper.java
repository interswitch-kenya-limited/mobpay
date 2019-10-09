package com.interswitchgroup.mobpaylib.di;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class DaggerWrapper {

    private static MainComponent mComponent;

    public static MainComponent getComponent(Context context, String clientId, String clientSecret) throws PackageManager.NameNotFoundException {
        if (mComponent == null) {
            initComponent(context, clientId, clientSecret);
        }
        return mComponent;
    }

    private static void initComponent(Context context, String clientId, String clientSecret) throws PackageManager.NameNotFoundException {
        ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        String baseurl = String.valueOf(ai.metaData.get("interswitch-kenya-limited.mobpay.base_url"));
        mComponent = DaggerMainComponent
                .builder()
                .netModule(new NetModule(baseurl, clientId, clientSecret))
                .build();
    }
}
