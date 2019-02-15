package com.interswitchgroup.mobpaylib.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.interswitchgroup.mobpaylib.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CardPaymentFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */

    public CardPaymentFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CardPaymentFragment newInstance() {
        return new CardPaymentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_payment, container, false);
    }
}

