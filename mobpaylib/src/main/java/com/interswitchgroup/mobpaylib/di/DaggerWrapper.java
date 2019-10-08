package com.interswitchgroup.mobpaylib.di;

public class DaggerWrapper {

    private static MainComponent mComponent;

    public static MainComponent getComponent(String clientId, String clientSecret) {
        if (mComponent == null) {
            initComponent(clientId, clientSecret);
        }
        return mComponent;
    }

    private static void initComponent(String clientId, String clientSecret) {
//        String baseurl = "https://testids.interswitch.co.ke:18082/api/v1/";
        String baseurl = "https://esb.interswitch-ke.com:18082/api/v1/";
        mComponent = DaggerMainComponent
                .builder()
                .netModule(new NetModule(baseurl, clientId, clientSecret))
                .build();
    }
}
