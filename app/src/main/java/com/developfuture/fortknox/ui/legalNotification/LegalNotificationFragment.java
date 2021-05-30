package com.developfuture.fortknox.ui.legalNotification;

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

public class LegalNotificationFragment extends Fragment {

    private LegalNotificationViewModel legalNotificationViewModel;

    public View onCreatView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        legalNotificationViewModel = new ViewModelProvider(this).get(LegalNotificationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_legalnotification, container, false);
        final TextView textView = root.findViewById(R.id.text_legalNotifitication);
        final TextView textView2 = root.findViewById(R.id.text_legal);


        textView2.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Adipiscing id dui ut nec, odio adipiscing pellentesque. Turpis blandit maecenas ornare volutpat hendrerit sed tellus mauris. \n" +
                "Non ut id orci ac dictum adipiscing commodo nec. \n" +
                "\n" +
                "Velit nisl lorem rutrum nunc, eu. \n" +
                "Enim fermentum varius imperdiet cursus lorem odio facilisi nisi. \n" +
                "A at amet est vitae posuere in nisl facilisis. \n" +
                "\n" +
                "Tempor, eleifend malesuada semper sed sem non integer mauris. \n" +
                "\n" +
                "Odio amet dolor iaculis vulputate scelerisque eget urna iaculis mauris.\n" +
                " Ultrices habitant ut consectetur nunc, cursus phasellus.\n" +
                " \n" +
                "Porta sit nibh vel viverra. Accumsan urna, vestibulum neque tellus risus gravida.");

        legalNotificationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        return root;
    }
}
