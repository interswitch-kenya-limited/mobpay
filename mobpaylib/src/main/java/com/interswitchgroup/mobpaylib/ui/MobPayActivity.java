package com.interswitchgroup.mobpaylib.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.R;
import com.interswitchgroup.mobpaylib.api.model.ErrorWrapper;
import com.interswitchgroup.mobpaylib.api.model.TransactionResponse;
import com.interswitchgroup.mobpaylib.databinding.ActivityMobPayBinding;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;
import com.interswitchgroup.mobpaylib.ui.fragments.PlaceHolderFragment;
import com.interswitchgroup.mobpaylib.ui.fragments.card.CardPaymentFragment;
import com.interswitchgroup.mobpaylib.ui.fragments.card.PaymentVm;
import com.interswitchgroup.mobpaylib.utils.AndroidUtils;
import com.interswitchgroup.mobpaylib.utils.NetUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MobPayActivity extends DaggerAppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public Merchant merchant;
    public Customer customer;
    public Payment payment;
    public String clientId;
    public String clientSecret;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private PaymentVm paymentVm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.merchant = (Merchant) getIntent().getSerializableExtra("merchant");
        this.customer = (Customer) getIntent().getSerializableExtra("customer");
        this.payment = (Payment) getIntent().getSerializableExtra("payment");
        this.clientId = getIntent().getStringExtra("clientId");
        this.clientSecret = getIntent().getStringExtra("clientSecret");
        paymentVm = ViewModelProviders.of(this, viewModelFactory).get(PaymentVm.class);
        paymentVm.setMobPay(new MobPay(this.clientId, this.clientSecret));
        paymentVm.setCustomer(this.customer);
        paymentVm.setMerchant(this.merchant);
        paymentVm.setPayment(this.payment);
        paymentVm.setOnSuccess(new TransactionSuccessCallback() {
            @Override
            public void onSuccess(TransactionResponse transactionResponse) {
                paymentVm.getLoading().set(false);
                View dialogView = LayoutInflater.from(MobPayActivity.this).inflate(R.layout.result_dialog, (ViewGroup) getWindow().getDecorView().getRootView(), false);
                TextView title = dialogView.findViewById(R.id.dialog_result_title);
                ImageView imageView = dialogView.findViewById(R.id.dialog_image);
                TextView message = dialogView.findViewById(R.id.dialog_message);
                imageView.setImageDrawable(ContextCompat.getDrawable(MobPayActivity.this, R.drawable.happy_face));
                title.setText(R.string.payment_successful_title);
                message.setText("Your transaction was completed successfully, your payment reference is " + transactionResponse.getTransactionReference());
                // TODO Find a way to dismiss the mobpay activity and using passed context from calling activity to launch the success dialog on it
                //                getActivity().finish();
                new AlertDialog.Builder(MobPayActivity.this)
                        .setView(dialogView)
                        .create()
                        .show();
            }
        });
        paymentVm.setOnFailure(new TransactionFailureCallback() {
            @Override
            public void onError(Throwable error) {
                paymentVm.getLoading().set(false);
                View dialogView = LayoutInflater.from(MobPayActivity.this).inflate(R.layout.result_dialog, (ViewGroup) getWindow().getDecorView().getRootView(), false);
                ImageView imageView = dialogView.findViewById(R.id.dialog_image);
                TextView title = dialogView.findViewById(R.id.dialog_result_title);
                TextView message = dialogView.findViewById(R.id.dialog_message);
                imageView.setImageDrawable(ContextCompat.getDrawable(MobPayActivity.this, R.drawable.sad_face));
                title.setText(R.string.payment_failed_title);
                ErrorWrapper errorWrapper = NetUtil.parseError(error);
                if (errorWrapper != null) {
                    message.setText(errorWrapper.getError().getStatusMessage());
                } else {
                    message.setText(error.getMessage());
                }
                new AlertDialog.Builder(MobPayActivity.this)
                        .setView(dialogView)
                        .create()
                        .show();
            }
        });
        ActivityMobPayBinding activityMobPayBinding = DataBindingUtil.setContentView(this, R.layout.activity_mob_pay);
        activityMobPayBinding.setLifecycleOwner(this);
        activityMobPayBinding.setPaymentVm(paymentVm);

        final FrameLayout progressOverlay = activityMobPayBinding.loading.progressOverlay;
        Glide.with(this)
                .load(R.drawable.running_man)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into((ImageView) findViewById(R.id.running_man));
        paymentVm.getLoading().addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (((ObservableBoolean) sender).get()) {
                    AndroidUtils.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
                } else {
                    AndroidUtils.animateView(progressOverlay, View.GONE, 0, 200);
                }
            }
        });

        Toolbar toolbar = activityMobPayBinding.toolbar;
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addTab("Card");
        mSectionsPagerAdapter.addTab("Mobile");
        mSectionsPagerAdapter.addTab("Bank");
        mSectionsPagerAdapter.addTab("Verve Paycode");

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<String> mFragmentTitlesList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CardPaymentFragment();
                default:
                    return PlaceHolderFragment.newInstance(mFragmentTitlesList.get(position));
            }
        }

        @Override
        public int getCount() {
            return mFragmentTitlesList.size();
        }

        public void addTab(String title) {
            mFragmentTitlesList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitlesList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
//        TODO display an are-you-sure-you-want-to-exit dialog
        Toast.makeText(this, "Are you sure you wanna leave?", Toast.LENGTH_LONG).show();
    }
}
