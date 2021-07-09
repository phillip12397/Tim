package com.developfuture.fortknox.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.developfuture.fortknox.R;

import java.util.List;

public class SpinnerAdapterInvestmants extends ArrayAdapter<Investments> {

    LayoutInflater layoutInflater;
    public SpinnerAdapterInvestmants(@NonNull Context context, int resource, @NonNull List<Investments> investmentTypes) {
        super(context, resource, investmentTypes);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View rowView = layoutInflater.inflate(R.layout.investments_custom_spinner_item, null, true);
        Investments investmentTypes = getItem(position);
        TextView textView = (TextView)rowView.findViewById(R.id.investment_spinner_text_view);

        textView.setText(investmentTypes.getName());

        return rowView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.investments_custom_spinner_item, parent, false);
        }
        Investments investmentTypes = getItem(position);
        TextView textView = (TextView)convertView.findViewById(R.id.investment_spinner_text_view);

        textView.setText(investmentTypes.getName());

        return convertView;
    }
}
