package com.developfuture.fortknox.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.developfuture.fortknox.R;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<Transaction> {

    LayoutInflater layoutInflater;
    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Transaction> transactionsTypes) {
        super(context, resource, transactionsTypes);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.custom_spinner_item, null, true);
        Transaction transaktionTypes = getItem(position);
        TextView textView = (TextView)rowView.findViewById(R.id.spinner_text_view);
        ImageView imageView = (ImageView)rowView.findViewById(R.id.spinner_icon_view);

        textView.setText(transaktionTypes.getName());
        imageView.setImageResource(transaktionTypes.getImage());

        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_spinner_item, parent, false);
        }
        Transaction transaktionTypes = getItem(position);
        TextView textView = (TextView)convertView.findViewById(R.id.spinner_text_view);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.spinner_icon_view);

        textView.setText(transaktionTypes.getName());
        imageView.setImageResource(transaktionTypes.getImage());

        return convertView;
    }
}
