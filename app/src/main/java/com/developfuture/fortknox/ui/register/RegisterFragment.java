package com.developfuture.fortknox.ui.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.developfuture.fortknox.R;
import com.developfuture.fortknox.ui.login.LoginViewModel;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private RegisterViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        loginViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        View root = inflater.inflate(R.layout.fragment_register, container, false);

        final TextView emailEntry = root.findViewById(R.id.editTextTextEmailAddress);
        final Button loginButton = root.findViewById(R.id.register_button);
        final TextView tertyaryLoginButton = root.findViewById(R.id.tertiaryLoginButton);

        loginViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {

            @Override
            public void onChanged(@Nullable String string) {
                emailEntry.setText(string);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(root).navigate(R.id.navigationRegisterToHome);
            }
        });

        tertyaryLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.navigateRegisterToLogin);
            }
        });

        return root;
    }
}