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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developfuture.fortknox.FTViewModel;
import com.developfuture.fortknox.IHViewModel;
import com.developfuture.fortknox.IViewModel;
import com.developfuture.fortknox.InvestmentsAdapter;
import com.developfuture.fortknox.InvestmentsHistoryAdapter;
import com.developfuture.fortknox.R;
import com.developfuture.fortknox.spinner.InvestmentTypes;
import com.developfuture.fortknox.spinner.SpinnerAdapterInvestmants;
import com.developfuture.fortknox.ui.home.FinanceTransaction;
import com.developfuture.fortknox.utiles.utiles;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InvestmentsFragment extends Fragment {

    private InvestmentsViewModel investmentsViewModel;
    private IViewModel iViewModel;
    private IHViewModel ihViewModel;
    private FTViewModel ftViewModel;
    private final InvestmentTypes investmentTypes = new InvestmentTypes();
    private Spinner spinner;
    private TextView textViewHomeMoney;
    private String btc;
    private String eth;
    private String matic;
    private String doge;
    private TextView prozent;
    private List<Investments> investmentsList = new ArrayList<>();
    private List<InvestmentsHistory> investmentsHistoriesList = new ArrayList<>();
    private FirebaseUser user;
    private utiles utiles;


    @SuppressLint("CheckResult")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        utiles = new utiles();
        investmentsViewModel = new ViewModelProvider(this).get(InvestmentsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_investments, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerViewInvestmemnts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        InvestmentsAdapter iAdapter = new InvestmentsAdapter();
        InvestmentsHistoryAdapter ihAdapter = new InvestmentsHistoryAdapter();
        recyclerView.setAdapter(iAdapter);


        RadioGroup radio = (RadioGroup) root.findViewById(R.id.radioGroupBtn);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yourAsset:
                        recyclerView.setAdapter(iAdapter);
                        break;
                    case R.id.history:
                        recyclerView.setAdapter(ihAdapter);
                        break;
                    default:
                        Toast.makeText(getContext(), "Allahu Akbar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        iViewModel = new ViewModelProvider(this).get(IViewModel.class);

        textViewHomeMoney = root.findViewById(R.id.homeMoneyInvestment);
        prozent = root.findViewById(R.id.prozent);

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runQuery("https://www.binance.com/api/v1/ticker/24hr?symbol=BTCEUR", "BTC");
                runQuery("https://www.binance.com/api/v1/ticker/24hr?symbol=ETHEUR", "ETH");
                runQuery("https://www.binance.com/api/v1/ticker/24hr?symbol=MATICEUR", "MATIC");
                runQuery("https://www.binance.com/api/v1/ticker/24hr?symbol=DOGEEUR", "DOGE");
                updateInvestmentPrices();
                startTimerThread();
            }
        }, 0, 10, TimeUnit.SECONDS);

        iViewModel.getAllInvestments().observe(getViewLifecycleOwner(), new Observer<List<Investments>>() {
            @Override
            public void onChanged(List<Investments> invs) {
                //update RecyclerView
                List<Investments> trueInv = new ArrayList<>();

                investmentsList = iViewModel.getAllInvestments().getValue();
                double sum = 0;

                for (Investments investment : investmentsList) {
                    if(user.getUid().equals(investment.getUserUID())){
                        trueInv.add(investment);
                        sum += investment.getPrice();
                    }
                }
                iAdapter.setFinances(trueInv);
                textViewHomeMoney.setText(String.valueOf(utiles.df.format(sum)) + "€");
                prozent.setText("0.00%");
            }
        });

        ftViewModel = new ViewModelProvider(this).get(FTViewModel.class);
        ihViewModel = new ViewModelProvider(this).get(IHViewModel.class);
        ihViewModel.getAllInvestmentsHistory().observe(getViewLifecycleOwner(), new Observer<List<InvestmentsHistory>>() {
            @Override
            public void onChanged(List<InvestmentsHistory> ihs) {
                //update RecyclerView
                List<InvestmentsHistory> trueIhs = new ArrayList<>();

                for (InvestmentsHistory investment : ihs) {
                    if(user.getUid().equals(investment.getUserUID())){
                        trueIhs.add(investment);
                    }
                }
                ihAdapter.setFinances(trueIhs);
            }
        });

        //final TextView textView = root.findViewById(R.id.text_gallery);
        investmentsViewModel.getText().observe(getViewLifecycleOwner(), s -> {
            //textView.setText(s);
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
                SpinnerAdapterInvestmants customAdapter = new SpinnerAdapterInvestmants(getContext(), R.layout.investments_custom_spinner_item, InvestmentTypes.getInvestmantTypesArrayList());
                spinner.setAdapter(customAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String price = getSelectedAsset(investmentTypes.getNameById(spinner.getSelectedItemPosition()));
                        if (price == null)
                            addPrice.setText("ungültiger Asset");
                        else
                            addPrice.setText(price);
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
                        double stock = Double.parseDouble(addStock.getText().toString());
                        String price = addPrice.getText().toString() + "€";

                        Investments inv = iAdapter.getIdByAsset(crypto);

                        if (crypto.isEmpty() || stock == 0 || price.equals("€")) {
                            Toast.makeText(getContext(), "Alle Felder müssen ausgefüllt sein", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            double priceForAssets = Double.parseDouble(addPrice.getText().toString()) * Double.parseDouble(addStock.getText().toString());

                            List<Investments> investmentsList = iViewModel.getAllInvestments().getValue();
                            List<Investments> trueInvestmentList = new ArrayList<>();

                            assert investmentsList != null;
                            for(Investments invs : investmentsList){
                                if(user.getUid().equals(invs.getUserUID())){
                                    trueInvestmentList.add(invs);
                                }
                            }

                            Investments ft = new Investments(crypto, stock, priceForAssets, 0, user.getUid());
                            InvestmentsHistory ih = new InvestmentsHistory(crypto, stock, priceForAssets, 0, user.getUid());

                            LocalDateTime now = LocalDateTime.now();
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                            String date = dtf.format(now);
                            double roundedValue = (double)((int)(priceForAssets*stock*100))/100;
                            FinanceTransaction transaction = new FinanceTransaction("Investiert in " + crypto, date, "-"+ roundedValue + "€", user.getUid());
                            ftViewModel.insert(transaction);

                            assert trueInvestmentList != null;
                            if (trueInvestmentList.stream().noneMatch(investment -> investment.getAsset().equals(crypto))) {
                                iViewModel.insert(ft);
                                ihViewModel.insert(ih);

                            } else {
                                if (inv != null) {
                                    ft.setStock(ft.getStock() + inv.getStock());
                                    ft.setPrice(ft.getPrice() + inv.getPrice());
                                    ft.setId(inv.getId());
                                    iViewModel.update(ft);

                                    ih = new InvestmentsHistory(crypto, stock, priceForAssets, 0, user.getUid());
                                    ihViewModel.insert(ih);
                                }
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
                        Investments inv;

                        switch (investmentTypes.getNameById(spinner.getSelectedItemPosition())) {
                            case "Btc":
                                addPrice.setText(btc);
                                inv = iAdapter.getIdByAsset("Btc");
                                if (inv != null) addStock.setText(String.valueOf(inv.getStock()));
                                else addStock.setText("0");
                                break;
                            case "Eth":
                                addPrice.setText(eth);
                                inv = iAdapter.getIdByAsset("Eth");
                                if (inv != null) addStock.setText(String.valueOf(inv.getStock()));
                                else addStock.setText("0");
                                break;
                            case "Matic":
                                addPrice.setText(matic);
                                inv = iAdapter.getIdByAsset("Matic");
                                if (inv != null) addStock.setText(String.valueOf(inv.getStock()));
                                else addStock.setText("0");
                                break;
                            case "Doge":
                                addPrice.setText(doge);
                                inv = iAdapter.getIdByAsset("Doge");
                                if (inv != null) addStock.setText(String.valueOf(inv.getStock()));
                                else addStock.setText("0");
                                break;
                            default:
                                addPrice.setText("ungültiger Asset");
                        }
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
                        Investments inv = iAdapter.getIdByAsset(crypto);

                        if (crypto.isEmpty() || stockToSell == 0) {
                            Toast.makeText(getContext(), "Alle Felder müssen ausgefüllt sein", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            List<Investments> investmentsList = iViewModel.getAllInvestments().getValue();

                            assert investmentsList != null;
                            if (investmentsList.stream().noneMatch(investment -> investment.getAsset().equals(crypto))) {
                                Toast.makeText(getContext(), "Kann nicht gelöscht werden, da es nicht vorhanden ist.", Toast.LENGTH_SHORT).show();
                            } else {

                                assert inv != null;
                                double priceForAssets = Double.parseDouble(addPrice.getText().toString()) * Double.parseDouble(addStock.getText().toString());
                                InvestmentsHistory investmentsHistory = new InvestmentsHistory(crypto, stockToSell, priceForAssets, 1, user.getUid());

                                LocalDateTime now = LocalDateTime.now();
                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                                String date = dtf.format(now);
                                double roundedValue = (double)((int)(priceForAssets*stockToSell*100))/100;
                                FinanceTransaction transaction = new FinanceTransaction("Verkauft " + crypto, date, "+" + roundedValue + "€", user.getUid());
                                ftViewModel.insert(transaction);

                                if (inv.getStock() > stockToSell) {
                                    double newStock = inv.getStock() - stockToSell;
                                    Investments investment = new Investments(crypto, newStock, newStock * Double.parseDouble(addPrice.getText().toString()), 1, user.getUid());
                                    investment.setId(inv.getId());
                                    iViewModel.update(investment);
                                    ihViewModel.insert(investmentsHistory);
                                } else if (inv.getStock() == stockToSell) {
                                    iViewModel.delete(inv);
                                    ihViewModel.insert(investmentsHistory);
                                } else {
                                    Toast.makeText(getContext(), "Sie haben nicht genug Stock um dies zu tun. ", Toast.LENGTH_SHORT).show();
                                }
                            }

                            iDialogue.dismiss();
                        }
                    }
                });
            }
        });

        return root;
    }

    private void updateInvestmentPrices() {
        List<Investments> investments = iViewModel.getAllInvestments().getValue();
        for (Investments investment : investmentsList) {

            String value = getSelectedAsset(investment.getAsset());
            if (value != null) {
                Double price = Double.parseDouble(value);
                double roundedValue = (double)((int)(price*investment.getStock()*100))/100;
                investment.setPrice(roundedValue);
                iViewModel.update(investment);
            }
        }

    }

    private void startTimerThread() {
        try {
            Thread th = new Thread(new Runnable() {
                public void run() {
                    investmentsList = iViewModel.getAllInvestments().getValue();
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                double sum = 0;
                                double price = 0;
                                double sumWhenBought = 0;

                                for (Investments investment : investmentsList) {
                                    if(user.getUid().equals(investment.getUserUID())) {
                                        switch (investment.getAsset()) {
                                            case "Btc":
                                                price = investment.getStock() * Double.parseDouble(btc);
                                                sumWhenBought += investment.getPrice();
                                                break;
                                            case "Eth":
                                                price = investment.getStock() * Double.parseDouble(eth);
                                                sumWhenBought += investment.getPrice();
                                                break;
                                            case "Matic":
                                                price = investment.getStock() * Double.parseDouble(matic);
                                                sumWhenBought += investment.getPrice();
                                                break;
                                            case "Doge":
                                                price = investment.getStock() * Double.parseDouble(doge);
                                                sumWhenBought += investment.getPrice();
                                                break;
                                            default:
                                                System.out.println("Do Nothing");
                                        }
                                        sum += price;
                                    }
                                }
                                double proz = ((sum - sumWhenBought) / sumWhenBought) * 100;

                                if (proz < 0) {
                                    prozent.setTextColor(Color.parseColor("#FF906D"));
                                } else {
                                    prozent.setTextColor(Color.parseColor("#3DBBAA"));
                                }
                                if (sumWhenBought == 0) System.out.println("Ich bin null");
                                DecimalFormat df = new DecimalFormat("#.##");
                                textViewHomeMoney.setText(String.valueOf(df.format(sum)) + "€");
                                if(sumWhenBought == 0) {
                                    prozent.setText("0.00%");
                                } else {
                                    prozent.setText(String.valueOf(df.format(proz)) + "%");
                                }
                            }
                        });
                    }
                }
            });
            th.start();
        } catch (Exception e) {
            System.out.println("Error by updating Current Balance: " + e.getMessage());
        }
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

    public String getSelectedAsset(String selectedItemName) {
        switch (selectedItemName.toUpperCase()) {
            case "BTC":
                return btc;
            case "ETH":
                return eth;
            case "MATIC":
                return matic;
            case "DOGE":
                return doge;
            default:
                return null;
        }
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}