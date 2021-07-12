package com.developfuture.fortknox.ui.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.developfuture.fortknox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private View root = null;
    private RegisterViewModel loginViewModel;
    private FirebaseAuth mAuth;
    private Snackbar mySnackbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        loginViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        root = inflater.inflate(R.layout.fragment_register, container, false);

        // Email password and register button
        final TextView emailEntry = root.findViewById(R.id.editTextEmailAddress);
        final TextView passwordEntry = root.findViewById(R.id.editTextPassword);
        final Button registerButton = root.findViewById(R.id.register_button);

        // Go to login button
        final TextView tertiaryLoginButton = root.findViewById(R.id.tertiaryLoginButton);

        // Register and data protection buttons
        final TextView tertiaryRegisterLegalButton = root.findViewById(R.id.legalRegisterText);
        final TextView tertiaryRegisterDataButton = root.findViewById(R.id.dataProtectionRegisterText);

        loginViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {

            @Override
            public void onChanged(@Nullable String string) {
                emailEntry.setText(string);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailEntry.getText().toString();
                String password = passwordEntry.getText().toString();

                if(isEmailPasswordNotEmpty(email,password)) {
                    createAccount(email, password);
                }
                else {
                    mySnackbar = Snackbar.make(root, "email and password may not be empty to register.", 4000);
                    mySnackbar.show();
                }
            }
        });

        tertiaryLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.navigateRegisterToLogin);
            }
        });

        tertiaryRegisterLegalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.navigationRegisterToLegal);
            }
        });

        tertiaryRegisterDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.navigationRegisterToPrivacy);
            }
        });

        return root;
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            sendEmailVerification();
                            Navigation.findNavController(root).navigate(R.id.navigationRegisterToHome);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private boolean isEmailPasswordNotEmpty(String email, String password) {
        return !TextUtils.isEmpty(password) && !TextUtils.isEmpty(email);
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Email sent
                    }
                });
        // [END send_email_verification]
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) { }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
}