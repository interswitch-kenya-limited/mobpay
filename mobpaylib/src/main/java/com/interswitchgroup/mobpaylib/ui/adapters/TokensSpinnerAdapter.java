package com.interswitchgroup.mobpaylib.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.interswitchgroup.mobpaylib.R;
import com.interswitchgroup.mobpaylib.model.CardToken;
import java.util.List;

public class TokensSpinnerAdapter<T extends CardToken> extends BaseAdapter {
  Context context;
  LayoutInflater inflter;
  List<CardToken> objectsAndImagesMap;

  public TokensSpinnerAdapter(Context applicationContext, List<CardToken> objectsAndImagesMap) {
    this.context = applicationContext;
    this.objectsAndImagesMap = objectsAndImagesMap;
    this.inflter = (LayoutInflater.from(applicationContext));
  }

  @Override
  public int getCount() {
    return objectsAndImagesMap.size();
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
    icon.setImageResource(this.objectsAndImagesMap.get(i).getImageResource());
    names.setText(this.objectsAndImagesMap.get(i).toString());
    return view;
  }
}
