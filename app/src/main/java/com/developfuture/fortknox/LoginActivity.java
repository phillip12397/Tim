package com.developfuture.fortknox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

public class LoginActivity extends AppCompatActivity {

    private Button button = null;
    private View mainView = findViewById(R.id.navigate_to_home);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        button = (Button) findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInsideActivity4();
            }
        });
    }

    public void openInsideActivity4()
    {
        Navigation.findNavController( mainView );
    }
}
