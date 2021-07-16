package com.developfuture.fortknox.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developfuture.fortknox.FTViewModel;
import com.developfuture.fortknox.FinanceAdapter;
import com.developfuture.fortknox.IViewModel;
import com.developfuture.fortknox.IconViewModel;
import com.developfuture.fortknox.R;
import com.developfuture.fortknox.spinner.SpinnerAdapter;
import com.developfuture.fortknox.spinner.TransaktionTypes;
import com.developfuture.fortknox.utiles.regex;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    private HomeViewModel homeViewModel;
    private FTViewModel ftViewModel;
    private Spinner spinner;
    private final TransaktionTypes transaktionTypes = new TransaktionTypes();
    private onFragmentBtnSelected listener;
    private regex regex;
    private FirebaseUser user;
    private IconViewModel iconViewModel;
    private View changeIconActivity;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        changeIconActivity = inflater.inflate(R.layout.change_icon, null);
        user = FirebaseAuth.getInstance().getCurrentUser();

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        FinanceAdapter adapter = new FinanceAdapter();
        recyclerView.setAdapter(adapter);
        iconViewModel = new ViewModelProvider(this).get(IconViewModel.class);

        regex = new regex();
        ftViewModel = new ViewModelProvider(this).get(FTViewModel.class);
        ftViewModel.getAllFinances().observe(getViewLifecycleOwner(), financeTransactions -> {
            //update RecyclerView
            List<FinanceTransaction> trueFt = new ArrayList<>();

            double price = 0;
            for (FinanceTransaction ft : financeTransactions) {
                if (user.getUid().equals(ft.getUserUID())) {
                    trueFt.add(ft);
                    price += Double.parseDouble(ft.getPrice().substring(0, ft.getPrice().length() - 1));
                }
            }
            double roundedValue = (double)((int)(price*100))/100;
            homeViewModel.setText(roundedValue +"€");
            adapter.setFinances(trueFt);
        });

        final TextView currentMoneyValue = root.findViewById(R.id.homeMoney);
        // Button shortcuts
        final ImageButton imageButton1 = root.findViewById(R.id.imageButton1);
        final ImageButton imageButton2 = root.findViewById(R.id.imageButton2);
        final ImageButton imageButton3 = root.findViewById(R.id.imageButton3);
        final ImageButton imageButton4 = root.findViewById(R.id.imageButton4);
        final ImageButton imageButton5 = root.findViewById(R.id.imageButton5);
        iconViewModel.getAllIconModels().observe(getViewLifecycleOwner(), iconModels -> {
            for (IconModel i : iconModels) {
                ImageButton imageButton = root.findViewById(i.getViewId());
                ImageButton referenceButton = changeIconActivity.findViewById(i.getReferenceId());
                if (imageButton == null || referenceButton == null)
                    continue;
                imageButton.setBackground(referenceButton.getBackground());
                imageButton.setImageDrawable(referenceButton.getDrawable());
            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                currentMoneyValue.setText(s);
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
                addPrice.setInputType(InputType.TYPE_CLASS_PHONE);
                final Button addTransaction = fbDialogue.findViewById(R.id.shortkeyAdd);


                spinner = (Spinner) fbDialogue.findViewById(R.id.addTransactionSpinner);
                SpinnerAdapter customAdaptar = new SpinnerAdapter(getContext(), R.layout.custom_spinner_item, TransaktionTypes.getTransactionTypesArrayList());
                spinner.setAdapter(customAdaptar);

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
                        String price = addPrice.getText().toString();
                        addNewTransaction(name, date, price, fbDialogue);
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
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
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
        final Button add = fbDialogue.findViewById(R.id.shortkeyAdd);
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
                String price = editText.getText().toString();
                String name = textView.getText().toString();
                addNewTransaction(name, date, price, fbDialogue);
            }
        });

        switch (id) {
            case R.id.imageButton1:
                textView.setText("Food");
                break;
            case R.id.imageButton2:
                textView.setText("Games");
                break;
            case R.id.imageButton3:
                textView.setText("Gas Station");
                break;
            case R.id.imageButton4:
                textView.setText("Party");
                break;
            case R.id.imageButton5:
                textView.setText("Birthday");
                break;
            default:
                System.out.println("nothing clicked");
        }
    }

    private void addNewTransaction(String name, String date, String price, Dialog fbDialogue) {
        if (name.isEmpty() || date.isEmpty() || price.equals("€")) {
            Toast.makeText(getContext(), "Bitte fülle alle Felder aus.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!regex.isValidInputValue(price)) {
            Toast.makeText(getContext(), "Bitte gib einen positiven oder negativen Wert ein. z.B (+5 oder -5)", Toast.LENGTH_SHORT).show();
            return;
        } else {
            price += "€";
            FinanceTransaction ft = new FinanceTransaction(name, date, price, user.getUid());
            ftViewModel.insert(ft);
            fbDialogue.dismiss();
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

        for (ImageButton i : findAllIcons(fbDialogue)) {
            i.setOnClickListener(view -> {
                if (view instanceof ImageButton && v instanceof ImageButton) {
                    ImageButton replaceButton = (ImageButton) view;
                    ImageButton button = (ImageButton) v;

                    IconModel iconModel = iconViewModel.getAllIconModels().getValue().stream()
                                                       .filter(icon -> icon.getViewId() == button.getId()).findFirst().orElse(null);
                    if (iconModel == null) {
                        iconModel = new IconModel(button.getId(), replaceButton.getId());
                        iconViewModel.insert(iconModel);
                    } else {
                        iconModel.setReferenceId(replaceButton.getId());
                        iconViewModel.update(iconModel);
                    }


                    fbDialogue.dismiss();
                }
            });
        }

        final TextView textView = fbDialogue.findViewById(R.id.shortkeyID);
        final ImageButton closeButton = fbDialogue.findViewById(R.id.shortkeyCloseIcons);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbDialogue.dismiss();
            }
        });

        switch (id) {
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

    private Set<ImageButton> findAllIcons(Dialog view) {
        List<ImageButton> result = new ArrayList<>(); //TODO Get images dynamically
        result.add(view.findViewById(R.id.shortkeyIcon1));
        result.add(view.findViewById(R.id.shortkeyIcon2));
        result.add(view.findViewById(R.id.shortkeyIcon3));
        result.add(view.findViewById(R.id.shortkeyIcon4));
        result.add(view.findViewById(R.id.shortkeyIcon5));
        result.add(view.findViewById(R.id.shortkeyIcon6));
        return result.stream().filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentBtnSelected) {
            listener = (onFragmentBtnSelected) context;
        }
    }

    public interface onFragmentBtnSelected {
        void onButtonSelected();
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }
}