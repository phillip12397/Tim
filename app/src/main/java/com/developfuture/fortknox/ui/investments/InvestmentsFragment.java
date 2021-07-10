package com.developfuture.fortknox.ui.investments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developfuture.fortknox.IViewModel;
import com.developfuture.fortknox.InvestmentsAdapter;
import com.developfuture.fortknox.R;
import com.developfuture.fortknox.spinner.InvestmentTypes;
import com.developfuture.fortknox.spinner.SpinnerAdapterInvestmants;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class InvestmentsFragment extends Fragment {

    private InvestmentsViewModel investmentsViewModel;
    private IViewModel iViewModel;
    private final InvestmentTypes investmentTypes = new InvestmentTypes();
    private Spinner spinner;
    private TextView textViewHomeMoney;
    private String btc;
    private String eth;
    private String matic;
    private String doge;
    private List<Investments> investmentsList = new ArrayList<>();

    @SuppressLint("CheckResult")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        investmentsViewModel = new ViewModelProvider(this).get(InvestmentsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_investments, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewInvestmemnts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        InvestmentsAdapter adapter = new InvestmentsAdapter();
        recyclerView.setAdapter(adapter);

        textViewHomeMoney = root.findViewById(R.id.homeMoney);

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runQuery("https://www.binance.com/api/v1/ticker/24hr?symbol=BTCEUR", "BTC");
                runQuery("https://www.binance.com/api/v1/ticker/24hr?symbol=ETHEUR", "ETH");
                runQuery("https://www.binance.com/api/v1/ticker/24hr?symbol=MATICEUR", "MATIC");
                runQuery("https://www.binance.com/api/v1/ticker/24hr?symbol=DOGEEUR", "DOGE");
            }
        }, 0, 10, TimeUnit.SECONDS);

        iViewModel = new ViewModelProvider(this).get(IViewModel.class);
        iViewModel.getAllInvestments().observe(getViewLifecycleOwner(), new Observer<List<Investments>>() {
            @Override
            public void onChanged(List<Investments> invs) {
                //update RecyclerView
                adapter.setFinances(invs);

                investmentsList = iViewModel.getAllInvestments().getValue();
                double sum = 0;

                for (Investments investment : investmentsList){
                    sum += investment.getPrice();
                }
                textViewHomeMoney.setText(String.valueOf(sum));
            }
        });

        //final TextView textView = root.findViewById(R.id.text_gallery);
        investmentsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        //Ruft das Popup auf um Assets zu kaufen
        final Button addAsset = (Button) root.findViewById(R.id.addAsset);
        addAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog iDialogue = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
                iDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                iDialogue.setContentView(R.layout.popup_new_asset);
                iDialogue.setCanceledOnTouchOutside(true);
                iDialogue.show();

                EditText addStock = iDialogue.findViewById(R.id.assetAddStock);
                EditText addPrice = iDialogue.findViewById(R.id.assetAddPrice);

                spinner = iDialogue.findViewById(R.id.assetSpinner);
                SpinnerAdapterInvestmants customAdaptar = new SpinnerAdapterInvestmants(getContext(), R.layout.investments_custom_spinner_item, InvestmentTypes.getInvestmantTypesArrayList());
                spinner.setAdapter(customAdaptar);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setSelectedAsset(addPrice, investmentTypes.getNameById(spinner.getSelectedItemPosition()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                final ImageButton closeButton = iDialogue.findViewById(R.id.addAssetClose);

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iDialogue.dismiss();
                    }
                });

                final Button addPopupAsset = iDialogue.findViewById(R.id.addAssetPopup);

                addPopupAsset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String crypto = investmentTypes.getNameById(spinner.getSelectedItemPosition());
                        //String crypto = spinner.getSelectedItem().toString();
                        double stock = Double.parseDouble(addStock.getText().toString());
                        String price = addPrice.getText().toString() + "$";

                        if (crypto.isEmpty() || stock == 0 || price.equals("$")) {
                            Toast.makeText(getContext(), "Alle Felder müssen ausgefüllt sein", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            double priceForAssets = Double.parseDouble(addPrice.getText().toString()) * Double.parseDouble(addStock.getText().toString());

                            List<Investments> investmentsList = iViewModel.getAllInvestments().getValue();
                            Investments ft = new Investments(crypto, stock, priceForAssets);

                            assert investmentsList != null;
                            if (investmentsList.stream().noneMatch(investment -> investment.getAsset().equals(crypto))) {
                                iViewModel.insert(ft);
                            } else {
                                //TODO update funktioniert noch nicht
                                iViewModel.update(ft);
                            }

                            iDialogue.dismiss();
                        }
                    }
                });
            }
        });

        //Ruft das Popup auf um Assets zu verkaufen
        final Button sellAsset = (Button) root.findViewById(R.id.sellAsset);
        sellAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog iDialogue = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
                iDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                iDialogue.setContentView(R.layout.popup_sell_asset);
                iDialogue.setCanceledOnTouchOutside(true);
                iDialogue.show();

                final ImageButton closeButton = iDialogue.findViewById(R.id.sellAssetClose);

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        iDialogue.dismiss();
                    }
                });

                EditText addStock = iDialogue.findViewById(R.id.assetSellStock);
                EditText addPrice = iDialogue.findViewById(R.id.assetSellPrice);

                spinner = iDialogue.findViewById(R.id.assetSpinner);
                SpinnerAdapterInvestmants customAdaptar = new SpinnerAdapterInvestmants(getContext(), R.layout.investments_custom_spinner_item, InvestmentTypes.getInvestmantTypesArrayList());
                spinner.setAdapter(customAdaptar);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        setSelectedAsset(addPrice, investmentTypes.getNameById(spinner.getSelectedItemPosition()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final Button addPopupAsset = iDialogue.findViewById(R.id.sellAssetPopup);

                addPopupAsset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String crypto = investmentTypes.getNameById(spinner.getSelectedItemPosition());
                        double stockToSell = Double.parseDouble(addStock.getText().toString());

                        if (crypto.isEmpty() || stockToSell == 0) {
                            Toast.makeText(getContext(), "Alle Felder müssen ausgefüllt sein", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            List<Investments> investmentsList = iViewModel.getAllInvestments().getValue();
                            Investments ft = new Investments(crypto, stockToSell, 0);

                            assert investmentsList != null;
                            if (investmentsList.stream().noneMatch(investment -> investment.getAsset().equals(crypto))) {
                                System.out.println("Kann nicht gelöscht werden, da es noch nicht existiert");
                            } else {
                                iViewModel.delete(ft);
                                double oldStock = iViewModel.getAllInvestments().getValue().stream()
                                        .filter(investments -> investments.getAsset().equals(crypto)).collect(Collectors.toList()).get(0).getStock();
                                double newStock = oldStock - stockToSell;
                                Investments newInvestment = new Investments(crypto, newStock, newStock*Double.parseDouble(addPrice.getText().toString()));
                                iViewModel.insert(newInvestment);
                            }

                            iDialogue.dismiss();
                        }
                    }
                });
            }
        });

        return root;
    }

    private void runQuery(String url, String crypto) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response responses = null;

            try {
                responses = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert responses != null;
            String jsonData = responses.body().string();

            JSONObject Jobject = new JSONObject(jsonData);
            switch (crypto) {

                case "BTC":
                    btc = Jobject.getString("lastPrice");
                    break;
                case "ETH":
                    eth = Jobject.getString("lastPrice");
                    break;
                case "MATIC":
                    matic = Jobject.getString("lastPrice");
                    break;
                case "DOGE":
                    doge = Jobject.getString("lastPrice");
                    break;
                default:
                    System.out.println("looool");
                    break;
            }

            System.out.println(Jobject.getString("lastPrice"));

        } catch (Exception e) {
            System.out.println("Exception throwing: " + e.getMessage());
        }
    }

    public void setSelectedAsset(EditText addPrice, String selectedItemName) {
        switch (selectedItemName) {
            case "Btc":
                addPrice.setText(btc);
                break;
            case "Eth":
                addPrice.setText(eth);
                break;
            case "Matic":
                addPrice.setText(matic);
                break;
            case "Doge":
                addPrice.setText(doge);
                break;
            default:
                addPrice.setText("ungültiger Asset");
        }
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}