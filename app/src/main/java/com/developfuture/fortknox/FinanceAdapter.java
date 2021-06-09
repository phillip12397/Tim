package com.developfuture.fortknox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developfuture.fortknox.ui.home.FinanceTransaction;

import java.util.ArrayList;
import java.util.List;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.FinanceHolder> {
    private List<FinanceTransaction> fts = new ArrayList<>();

    @NonNull
    @Override
    public FinanceHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new FinanceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull  FinanceAdapter.FinanceHolder holder, int position) {
        FinanceTransaction ft = fts.get(position);
        holder.financeName.setText(ft.getName());
        holder.financeDate.setText(ft.getDate());
        holder.financePrice.setText(ft.getPrice());
    }

    @Override
    public int getItemCount() {
        return fts.size();
    }

    public void setFinances(List<FinanceTransaction> fts){
        this.fts = fts;
        notifyDataSetChanged();
    }

    public FinanceTransaction getFinanceAt(int position) {
        return fts.get(position);
    }

    class FinanceHolder extends RecyclerView.ViewHolder{
        private TextView financeName;
        private TextView financeDate;
        private TextView financePrice;

        public FinanceHolder(View itemView){
            super(itemView);
            financeName = itemView.findViewById(R.id.financeName);
            financeDate = itemView.findViewById(R.id.financeDate);
            financePrice = itemView.findViewById(R.id.financePrice);

        }
    }
}
