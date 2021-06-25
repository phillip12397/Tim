package com.developfuture.fortknox.ui.settings;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.developfuture.fortknox.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    private TextView userNameEntry;
    private TextView passwordEntry;
    private TextView emailEntry;
    private ImageView profilePhotoUri;

    private String username;
    private String password;
    private String email;

    private Button updateUserButton;
    private Button logoutButton;

    private Snackbar mySnackbar;

    FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        final TextView textView = root.findViewById(R.id.text_profile_header);
        userNameEntry = root.findViewById(R.id.userNameEntry);
        passwordEntry = root.findViewById(R.id.newPasswordEntry);
        emailEntry = root.findViewById(R.id.emailEntry);

        updateUserButton = root.findViewById(R.id.updateUserButton);
        logoutButton = root.findViewById(R.id.logoutButton);

        updateUserButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                username = userNameEntry.getText().toString();
                password = passwordEntry.getText().toString();
                email = emailEntry.getText().toString();

                if(verifyPasswordEmailUser(username, password, email)) {

                    updateProfile();
                    updateEmail();
                    //updatePassword();
                    mySnackbar = Snackbar.make(root, "Email, user name and password has been saved.", 4000);
                    mySnackbar.show();
                }
                else if(verifyEmailUser(username, email)) {

                    updateProfile();
                    updateEmail();
                    mySnackbar = Snackbar.make(root, "Email and user name has been saved.", 4000);
                    mySnackbar.show();
                }
                else {
                    mySnackbar = Snackbar.make(root, "User and email may not be empty to save the profile.", 4000);
                    mySnackbar.show();
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Navigation.findNavController(root).navigate(R.id.action_nav_settings_to_loginFragment);
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();

        getUser();

        settingsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    /***
     * Method to validate user email and password.
     * @param username
     * @param email
     * @param password
     * @return
     */
    private boolean verifyPasswordEmailUser(String username, String email, String password) {
        return isValidEmail(email) && isValidPassword(password) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(email);
    }

    /***
     * Method to validate user and email.
     * @param username
     * @param email
     * @return
     */
    private boolean verifyEmailUser(String username, String email) {
        return isValidEmail(email) && !TextUtils.isEmpty(username) && !TextUtils.isEmpty(email);
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PasswordFormat = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        pattern = Pattern.compile(PasswordFormat);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
    public boolean isValidEmail(final String email) {

        Pattern pattern;
        Matcher matcher;

        final String EmailFormat = "^([\\w\\.\\-]+)@([\\w\\-]+)((\\.(\\w){2,})+)$";

        pattern = Pattern.compile(EmailFormat);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private void getUser(){
        if (user != null) {
            userNameEntry.setText(user.getDisplayName());
            emailEntry.setText(user.getEmail());
            //profilePhotoUri.setImageURI(user.getPhotoUrl());
        } else {

        }
    }

    public void updateProfile() {
        // [START update_profile]
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(userNameEntry.getText().toString())
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
        // [END update_profile]
    }

    public void updateEmail() {
        // [START update_email]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail("user@example.com")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    }
                });
        // [END update_email]
    }

    public void updatePassword() {
        // [START update_password]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = "SOME-SECURE-PASSWORD";

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
        // [END update_password]
    }

    public void signOut() {
        // [START auth_sign_out]
        FirebaseAuth.getInstance().signOut();
        // [END auth_sign_out]
    }

    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
