package com.developfuture.fortknox;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developfuture.fortknox.ui.investments.Investments;

import java.util.ArrayList;
import java.util.List;

public class InvestmentsAdapter extends RecyclerView.Adapter<InvestmentsAdapter.InvestmentsHolder> {

    private List<Investments> invs = new ArrayList<>();

    @NonNull
    @Override
    public InvestmentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new InvestmentsHolder((itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull  InvestmentsAdapter.InvestmentsHolder holder, int position) {
        Investments inv = invs.get(position);
        holder.asset.setText(inv.getAsset());
        holder.stock.setText(inv.getStock());
        holder.price.setText(inv.getPrice());
    }

    @Override
    public int getItemCount() {
        return invs.size();
    }

    public void setFinances(List<Investments> invs){
        this.invs = invs;
        notifyDataSetChanged();
    }

    public Investments getInvestmentAt(int position) {
        return invs.get(position);
    }

    public class InvestmentsHolder extends RecyclerView.ViewHolder {
        private TextView asset;
        private TextView stock;
        private TextView price;

        public InvestmentsHolder(View itemView){
            super(itemView);
            asset = itemView.findViewById(R.id.recyclerView1);
            stock = itemView.findViewById(R.id.recyclerView2);
            price = itemView.findViewById(R.id.recyclerView3);

        }
    }
}
