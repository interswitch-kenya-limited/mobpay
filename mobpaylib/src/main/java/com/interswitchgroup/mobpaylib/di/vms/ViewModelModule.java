package com.interswitchgroup.mobpaylib.di.vms;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.interswitchgroup.mobpaylib.ui.fragments.card.CardVm;
import com.interswitchgroup.mobpaylib.ui.fragments.card.PaymentVm;
import com.interswitchgroup.mobpaylib.ui.fragments.mobile.MobileVm;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(CardVm.class)
    abstract ViewModel bindMapCardVm(CardVm cardVm);

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(MobileVm.class)
    abstract ViewModel bindMapMobileVm(MobileVm mobileVm);

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(PaymentVm.class)
    abstract ViewModel bindMapPaymentVm(PaymentVm paymentVm);

    @Binds
    @Singleton
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}