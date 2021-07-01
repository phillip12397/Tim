package com.developfuture.fortknox.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developfuture.fortknox.FTViewModel;
import com.developfuture.fortknox.FinanceAdapter;
import com.developfuture.fortknox.R;
import com.developfuture.fortknox.spinner.SpinnerAdapter;
import com.developfuture.fortknox.spinner.Transaction;
import com.developfuture.fortknox.spinner.TransaktionTypes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private HomeViewModel homeViewModel;
    private FTViewModel ftViewModel;
    private Spinner spinner;
    private final TransaktionTypes transaktionTypes = new TransaktionTypes();
    private onFragmentBtnSelected listener;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        FinanceAdapter adapter = new FinanceAdapter();
        recyclerView.setAdapter(adapter);

        ftViewModel = new ViewModelProvider(this).get(FTViewModel.class);
        ftViewModel.getAllFinances().observe(getViewLifecycleOwner(), new Observer<List<FinanceTransaction>>() {
            @Override
            public void onChanged(List<FinanceTransaction> financeTransactions) {
                //update RecyclerView
                adapter.setFinances(financeTransactions);
            }
        });

        final TextView textView = root.findViewById(R.id.homeMoney);
        // Button shortcuts
        final ImageButton imageButton1 = root.findViewById(R.id.imageButton1);
        final ImageButton imageButton2 = root.findViewById(R.id.imageButton2);
        final ImageButton imageButton3 = root.findViewById(R.id.imageButton3);
        final ImageButton imageButton4 = root.findViewById(R.id.imageButton4);
        final ImageButton imageButton5 = root.findViewById(R.id.imageButton5);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        //TODO philips floating button, need connection to DB function
        final FloatingActionButton detailedAdd = (FloatingActionButton) root.findViewById(R.id.fab);
        detailedAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog fbDialogue = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
                fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                fbDialogue.setContentView(R.layout.popup_add_transaction);
                fbDialogue.setCanceledOnTouchOutside(true);
                fbDialogue.show();


                final EditText addDate = fbDialogue.findViewById(R.id.addTransactionDate);
                final EditText addPrice = fbDialogue.findViewById(R.id.addTransactionPrice);
                addPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
                final Button addTransaction = fbDialogue.findViewById(R.id.shortkeyAdd);


                spinner = (Spinner) fbDialogue.findViewById(R.id.addTransactionSpinner);
                SpinnerAdapter customAdaptar = new SpinnerAdapter(getContext(), R.layout.custom_spinner_item, TransaktionTypes.getTransactionTypesArrayList());
                spinner.setAdapter(customAdaptar);

//                // Create an ArrayAdapter using the string array and a default spinner layout
//                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.add_transaction_names, android.R.layout.simple_spinner_item);
//                // Specify the layout to use when the list of choices appears
//                adapter.setDropDownViewResource(R.layout.custom_spinner_item);
//                // Apply the adapter to the spinner
//                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                final ImageButton closeButton = fbDialogue.findViewById(R.id.addTransactionClose);

                addTransaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = transaktionTypes.getNameById(spinner.getSelectedItemPosition());
                        String date = addDate.getText().toString();
                        String price = addPrice.getText().toString() + "$";

                        if(name.isEmpty() || date.isEmpty() || price.equals("$")){
                            Toast.makeText(getContext(),"Alle Felder müssen ausgefüllt sein", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            FinanceTransaction ft = new FinanceTransaction(name, date, price);
                            ftViewModel.insert(ft);
                            fbDialogue.dismiss();
                        }
                    }
                });

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fbDialogue.dismiss();
                    }
                });
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove( RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ftViewModel.delete(adapter.getFinanceAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        imageButton1.setOnLongClickListener(this);
        imageButton2.setOnLongClickListener(this);
        imageButton3.setOnLongClickListener(this);
        imageButton4.setOnLongClickListener(this);
        imageButton5.setOnLongClickListener(this);

        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
        imageButton5.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        final Dialog fbDialogue = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        fbDialogue.setContentView(R.layout.popup_home_shortkeys);
        fbDialogue.setCanceledOnTouchOutside(true);
        fbDialogue.show();

        final TextView textView = fbDialogue.findViewById(R.id.shortkeyID);
        final EditText editText = fbDialogue.findViewById(R.id.shortkeyIcon);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        final Button add =fbDialogue.findViewById(R.id.shortkeyAdd);
        final ImageButton closeButton = fbDialogue.findViewById(R.id.shortkeyClose);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbDialogue.dismiss();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                String date = dtf.format(now);

                String name = textView.getText().toString();
                String price = editText.getText().toString() + "$";
                FinanceTransaction ft = new FinanceTransaction(name, date, price);
                ftViewModel.insert(ft);
                fbDialogue.dismiss();
            }
        });

        switch (id){
            case R.id.imageButton1:
                textView.setText("Food");
                break;
            case R.id.imageButton2:
                textView.setText("Games");
                System.out.println("test2");
                break;
            case R.id.imageButton3:
                textView.setText("Gas Station");
                System.out.println("test3");
                break;
            case R.id.imageButton4:
                textView.setText("Party");
                System.out.println("test4");
                break;
            case R.id.imageButton5:
                textView.setText("Birthday");
                System.out.println("test5");
                break;
            default:
                System.out.println("nothing clicked");
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();

        final Dialog fbDialogue = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        fbDialogue.setContentView(R.layout.change_icon);
        fbDialogue.setCanceledOnTouchOutside(true);
        fbDialogue.show();

        final TextView textView = fbDialogue.findViewById(R.id.shortkeyID);
        final ImageButton closeButton = fbDialogue.findViewById(R.id.shortkeyCloseIcons);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbDialogue.dismiss();
            }
        });

        switch (id){
            case R.id.imageButton1:
                System.out.println("test1 Hold");
                return true;
            case R.id.imageButton2:
                System.out.println("test2 Hold");
                return true;
            case R.id.imageButton3:
                System.out.println("test3 Hold");
                return true;
            case R.id.imageButton4:
                System.out.println("test4 Hold");
                return true;
            case R.id.imageButton5:
                System.out.println("test5 Hold");
                return true;
            default:
                System.out.println("nothing clicked");
        }
        return false;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        if(context instanceof onFragmentBtnSelected){
            listener = (onFragmentBtnSelected) context;
        }
    }
    public interface onFragmentBtnSelected {
        void onButtonSelected();
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}