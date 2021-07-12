package com.developfuture.fortknox;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developfuture.fortknox.ui.investments.Investments;
import com.developfuture.fortknox.ui.investments.InvestmentsHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class InvestmentsHistoryAdapter extends RecyclerView.Adapter<InvestmentsHistoryAdapter.InvestmentsHistoryHolder> {
    private List<InvestmentsHistory> ihs = new ArrayList<>();
    private FirebaseUser user;

    @Override
    public InvestmentsHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new InvestmentsHistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InvestmentsHistoryHolder holder, int position) {
        InvestmentsHistory ih = ihs.get(position);
        holder.asset.setText(ih.getAsset());
        holder.stock.setText(String.valueOf(ih.getStock()));
        if (ih.getSellOrBuy() == 0) {
            holder.price.setTextColor(Color.parseColor("#3DBBAA"));
            holder.price.setText(String.valueOf(ih.getPrice()) + "€");
        } else {
            holder.price.setTextColor(Color.parseColor("#FF906D"));
            holder.price.setText(String.valueOf(ih.getPrice()) + "€");
        }
    }

    @Override
    public int getItemCount() {
        return ihs.size();
    }

    public void setFinances(List<InvestmentsHistory> ihs){
        this.ihs = ihs;
        notifyDataSetChanged();
    }

    public class InvestmentsHistoryHolder extends RecyclerView.ViewHolder {
        private final TextView asset;
        private final TextView stock;
        private final TextView price;

        public InvestmentsHistoryHolder(@NonNull View itemView) {
            super(itemView);
            asset = itemView.findViewById(R.id.recyclerView1);
            stock = itemView.findViewById(R.id.recyclerView2);
            price = itemView.findViewById(R.id.recyclerView3);
        }
    }
}
