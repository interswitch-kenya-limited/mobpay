package com.interswitchgroup.mobpaylib.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.R;

/**
 * A simple {@link Fragment} subclass to be used as a placeholder
 * Use the {@link PlaceHolderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceHolderFragment extends Fragment {
    private static final String FRAGMENT_TITLE = "param1";
    private TextView titleTv;
    private String fragmentTitle;

    public PlaceHolderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided title.
     *
     * @param fragmentTitle Fragment title.
     * @return A new instance of fragment PlaceHolderFragment.
     */
    public static PlaceHolderFragment newInstance(String fragmentTitle) {
        PlaceHolderFragment fragment = new PlaceHolderFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TITLE, fragmentTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragmentTitle = getArguments().getString(FRAGMENT_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
        this.titleTv = rootView.findViewById(R.id.placeholder_fragment_title);
        titleTv.setText(fragmentTitle);
        titleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MobPay.getInstance(getActivity(),"IKIA264751EFD43881E84150FDC4D7F0717AD27C4E64", "J3e432fg5qdpFXDsjlinBPGs/CgCNaUs5BHLFloO3/U=", null).doSomeHover(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

}
