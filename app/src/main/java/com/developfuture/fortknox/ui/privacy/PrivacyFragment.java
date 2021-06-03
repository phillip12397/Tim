package com.developfuture.fortknox.ui.privacy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.developfuture.fortknox.R;

public class PrivacyFragment extends Fragment {

    private PrivacyViewModel privacyViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        privacyViewModel = new ViewModelProvider(this).get(PrivacyViewModel.class);
        View root = inflater.inflate(R.layout.fragment_privacy, container, false);
        final TextView textView = root.findViewById(R.id.text_privacy);
        privacyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

}
