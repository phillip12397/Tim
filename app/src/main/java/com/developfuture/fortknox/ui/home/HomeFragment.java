package com.developfuture.fortknox.ui.home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.developfuture.fortknox.R;

public class HomeFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

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
        switch (id){
            case R.id.imageButton1:
                System.out.println("test1");
                break;
            case R.id.imageButton2:
                System.out.println("test2");
                break;
            case R.id.imageButton3:
                System.out.println("test3");
                break;
            case R.id.imageButton4:
                System.out.println("test4");
                break;
            case R.id.imageButton5:
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