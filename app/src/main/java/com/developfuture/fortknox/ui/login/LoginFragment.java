package com.developfuture.fortknox.ui.login;

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
import android.widget.Toast;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.developfuture.fortknox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private View root = null;
    private LoginViewModel loginViewModel;
    private FirebaseAuth mAuth;
    private Snackbar mySnackbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        root = inflater.inflate(R.layout.fragment_login, container, false);

        // Email password and login button
        final TextView emailEntry = root.findViewById(R.id.editTextEmailAddress);
        final TextView passwordEntry = root.findViewById(R.id.editTextPassword);
        final Button loginButton = root.findViewById(R.id.register_button);

        // Go to register button
        final TextView tertiaryRegisterButton = root.findViewById(R.id.tertiaryLoginButton);
        final TextView forgotPasswordButton = root.findViewById(R.id.forgotPassword);

        // Register and data protection buttons
        final TextView tertiaryLegalButton = root.findViewById(R.id.legalLoginText);
        final TextView tertiaryRegisterDataButton = root.findViewById(R.id.dataProtectionLoginText);

        loginViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String string) {
                emailEntry.setText(string);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = emailEntry.getText().toString();
                String password = passwordEntry.getText().toString();

                if (isEmailPasswordNotEmpty(mail, password)) {
                    signIn(mail, password);
                } else {
                    mySnackbar = Snackbar.make(root, "Email and password may not be empty to login.", 4000);
                    mySnackbar.show();
                }
            }
        });

        tertiaryRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.navigateLoginToRegister);
            }
        });

        tertiaryLegalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.navigateLoginToLegal);
            }
        });

        tertiaryRegisterDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.navigateLoginToPrivacy);
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = emailEntry.getText().toString();

                if (!TextUtils.isEmpty(mail)) {
                    resetPassword(mail);
                } else {
                    mySnackbar = Snackbar.make(root, "Please fill the email entry.", 4000);
                    mySnackbar.show();
                }
            }
        });

        return root;
    }

    private boolean isEmailPasswordNotEmpty(String email, String password) {
        return !TextUtils.isEmpty(password) && !TextUtils.isEmpty(email);
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Navigation.findNavController(root).navigate(R.id.navigateLoginToHome);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void resetPassword(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email hase been sent.");
                            mySnackbar = Snackbar.make(root, "Email hase been sent.", 4000);
                            mySnackbar.show();
                        } else {
                            Log.d(TAG, "Sorry, we have a problem.");
                            mySnackbar = Snackbar.make(root, "Sorry, we have a problem.", 4000);
                            mySnackbar.show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }

    private void reload() {
    }

    private void updateUI(FirebaseUser user) {
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }
}