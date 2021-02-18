package com.auto.Autowallpaperchanger.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.auto.Autowallpaperchanger.R;
import com.auto.Autowallpaperchanger.models.SpinnerItem;

import java.util.ArrayList;

public class SpinnerItemAdapter extends ArrayAdapter<SpinnerItem> {
    private Context context;
    private ArrayList<SpinnerItem> itemList;

    public SpinnerItemAdapter(Context context2, int resource, ArrayList<SpinnerItem> list) {
        super(context2, resource, list);
        this.context = context2;
        this.itemList = list;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = LayoutInflater.from(this.context).inflate(R.layout.spinner_row, parent, false);
        }
        SpinnerItem current = this.itemList.get(position);
        ((ImageView) row.findViewById(R.id.profile)).setBackgroundResource(current.getProfile());
        ((TextView) row.findViewById(R.id.title)).setText(current.getTitle());
        return row;
    }
}
