package com.tec_mob.wheaty;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.Button;
import android.widget.Toast;


public class LogInActivity extends AppCompatActivity {

    private Button registerButton;
    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        signIn();
        register();
    }

    public void signIn(){

        signIn = findViewById(R.id.sign_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogInActivity.this, MainActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Welcome to Wheaty!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    public void register(){

        registerButton = findViewById(R.id.click_here);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

    }

}
