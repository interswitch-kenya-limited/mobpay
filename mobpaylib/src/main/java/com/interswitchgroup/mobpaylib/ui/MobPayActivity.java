package com.interswitchgroup.mobpaylib.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.interswitchgroup.mobpaylib.R;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;
import com.interswitchgroup.mobpaylib.ui.fragments.PlaceHolderFragment;
import com.interswitchgroup.mobpaylib.ui.fragments.card.CardPaymentFragment;

import java.util.ArrayList;
import java.util.List;

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
    private Merchant merchant;
    private Customer customer;
    private Payment payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.merchant = (Merchant) getIntent().getSerializableExtra("merchant");
        this.customer = (Customer) getIntent().getSerializableExtra("customer");
        this.payment = (Payment) getIntent().getSerializableExtra("payment");

        setContentView(R.layout.activity_mob_pay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addTab("Card");
        mSectionsPagerAdapter.addTab("Mobile");
        mSectionsPagerAdapter.addTab("Bank");
        mSectionsPagerAdapter.addTab("Verve Paycode");

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
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
