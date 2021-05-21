package com.developfuture.fortknox.ui.home;

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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.developfuture.fortknox.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener{

    private HomeViewModel homeViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final ImageButton imageButton1 = root.findViewById(R.id.imageButton1);
        final ImageButton imageButton2 = root.findViewById(R.id.imageButton2);
        final ImageButton imageButton3 = root.findViewById(R.id.imageButton3);
        final ImageButton imageButton4 = root.findViewById(R.id.imageButton4);
        final ImageButton imageButton5 = root.findViewById(R.id.imageButton5);
        //final ImageButton imageButton6 = root.findViewById(R.id.imageButton6);

        final FloatingActionButton detailedAdd = (FloatingActionButton) root.findViewById(R.id.fab);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

        });

        detailedAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog fbDialogue = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
                fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                fbDialogue.setContentView(R.layout.add_transaction);
                fbDialogue.setCanceledOnTouchOutside(true);
                fbDialogue.show();

                Spinner spinner = fbDialogue.findViewById(R.id.addTransactionSpinner);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.add_transaction_names, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String text = parent.getItemAtPosition(position).toString();
                        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                final ImageButton closeButton = fbDialogue.findViewById(R.id.addTransactionClose);

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fbDialogue.dismiss();
                    }
                });
            }
        });

        /*
            Spinner spinner = findViewById(R.id.addTransactionSpinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.add_transaction_names, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
         */

        imageButton1.setOnLongClickListener(this);
        imageButton2.setOnLongClickListener(this);
        imageButton3.setOnLongClickListener(this);
        imageButton4.setOnLongClickListener(this);
        imageButton5.setOnLongClickListener(this);
        //imageButton6.setOnLongClickListener(this);

        imageButton1.setOnClickListener(this);
        imageButton2.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
        imageButton5.setOnClickListener(this);
        //imageButton6.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        final Dialog fbDialogue = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        fbDialogue.setContentView(R.layout.popup_home_shortkeys);
        fbDialogue.setCanceledOnTouchOutside(true);
        fbDialogue.show();

        final TextView textView = fbDialogue.findViewById(R.id.shortkeyID);
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
                fbDialogue.dismiss();
            }
        });

        switch (id){
            case R.id.imageButton1:
                textView.setText("Food");
                System.out.println("test1");
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
            //case R.id.imageButton6:
            //    System.out.println("test6");
            //    break;
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
            //case R.id.imageButton6:
            //    System.out.println("test6");
            //    break;
            default:
                System.out.println("nothing clicked");
        }
        return false;
    }
}