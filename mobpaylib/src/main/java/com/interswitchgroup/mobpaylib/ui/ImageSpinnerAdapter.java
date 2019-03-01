package com.interswitchgroup.mobpaylib.ui;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.interswitchgroup.mobpaylib.R;

import java.util.List;

public class ImageSpinnerAdapter<T extends Enum<T>> extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<Pair<T, Integer>> namesAndImagesMap;

    public ImageSpinnerAdapter(Context applicationContext, List<Pair<T, Integer>> namesAndImagesMap) {
        this.context = applicationContext;
        this.namesAndImagesMap = namesAndImagesMap;
        this.inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return namesAndImagesMap.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_custom_layout, null);
        ImageView icon = view.findViewById(R.id.imageView);
        TextView names = view.findViewById(R.id.textView);
        icon.setImageResource(this.namesAndImagesMap.get(i).second);
        names.setText(this.namesAndImagesMap.get(i).first.toString());
        return view;
    }
}
