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
import android.widget.ArrayAdapter;
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
import com.developfuture.fortknox.spinner.SpinnerAdapter;
import com.developfuture.fortknox.spinner.TransaktionTypes;
import com.developfuture.fortknox.ui.home.FinanceTransaction;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.webcerebrium.binance.api.BinanceApi;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class InvestmentsFragment extends Fragment {

    private InvestmentsViewModel investmentsViewModel;
    private IViewModel iViewModel;
    private final TransaktionTypes transaktionTypes = new TransaktionTypes();
    private Spinner spinner;
    private BinanceApi api;
    private String btc;
    private String eth;
    private String matic;
    private String doge;

    @SuppressLint("CheckResult")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        investmentsViewModel = new ViewModelProvider(this).get(InvestmentsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_investments, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewInvestmemnts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        InvestmentsAdapter adapter = new InvestmentsAdapter();
        recyclerView.setAdapter(adapter);

        GetCryptoPrice response = new GetCryptoPrice("https://www.binance.com/api/v1/ticker/24hr?symbol=BTCEUR", "BTC");
        response.start();
        response = new GetCryptoPrice("https://www.binance.com/api/v1/ticker/24hr?symbol=ETHEUR", "ETH");
        response.start();
        response = new GetCryptoPrice("https://www.binance.com/api/v1/ticker/24hr?symbol=MATICEUR", "MATIC");
        response.start();
        response = new GetCryptoPrice("https://www.binance.com/api/v1/ticker/24hr?symbol=DOGEEUR", "DOGE");
        response.start();

        iViewModel = new ViewModelProvider(this).get(IViewModel.class);
        iViewModel.getAllInvestments().observe(getViewLifecycleOwner(), new Observer<List<Investments>>() {
            @Override
            public void onChanged(List<Investments> invs) {
                //update RecyclerView
                adapter.setFinances(invs);
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
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                        R.array.add_asset_names, android.R.layout.simple_spinner_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
/*
                spinner = (Spinner) iDialogue.findViewById(R.id.addTransactionSpinner);
                ArrayAdapter customAdaptar = new ArrayAdapter(getContext(), R.layout.custom_spinner_item, android.R.layout.add_ass);
                spinner.setAdapter(customAdaptar);
*/
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
                        String crypto = spinner.getSelectedItem().toString();
                        String stock = addStock.getText().toString();
                        String price = addPrice.getText().toString() + "$";

                        if(crypto.isEmpty() || stock.isEmpty() || price.equals("$")){
                            Toast.makeText(getContext(),"Alle Felder müssen ausgefüllt sein", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Investments ft = new Investments(crypto, stock, price);
                            iViewModel.insert(ft);
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

            }
        });

        return root;
    }

    //gibt dir den Aktuellen Crypto Preis aus von der Url die man übergeben hat
    class GetCryptoPrice extends Thread {
        String url;
        String crypto;

        public GetCryptoPrice(String url, String crypto) {
            this.url = url;
            this.crypto = crypto;
        }

        @Override
        public void run() {
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
                switch(crypto) {

                    case "BTC":
                        btc = Jobject.getString("lastPrice"); break;
                    case "ETH":
                        eth = Jobject.getString("lastPrice"); break;
                    case "MATIC":
                        matic = Jobject.getString("lastPrice"); break;
                    case "DOGE":
                        doge = Jobject.getString("lastPrice"); break;
                    default:
                        System.out.println("looool"); break;
                }

                System.out.println(Jobject.getString("lastPrice"));

            } catch (Exception e) {
                System.out.println("Exception throwing: " + e.getMessage());
            }
        }
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}