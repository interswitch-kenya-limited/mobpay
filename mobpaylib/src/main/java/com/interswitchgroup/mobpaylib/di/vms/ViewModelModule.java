package com.interswitchgroup.mobpaylib.di.vms;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.interswitchgroup.mobpaylib.ui.fragments.card.CardVm;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CardVm.class)
    abstract ViewModel bindMapCardVm(CardVm cardVm);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}