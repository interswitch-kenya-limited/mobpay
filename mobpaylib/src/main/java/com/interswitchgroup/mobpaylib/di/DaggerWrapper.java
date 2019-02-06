package com.interswitchgroup.mobpaylib.di;

public class DaggerWrapper {

    private static MainComponent mComponent;

    public static MainComponent getComponent() {
        if (mComponent == null) {
            initComponent();
        }
        return mComponent;
    }

    private static void initComponent() {
        mComponent = DaggerMainComponent
                .builder()
                .netModule(new NetModule("https://testids.interswitch.co.ke:9080"))
                .build();
    }
}
