package com.tec_mob.wheaty.Views;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tec_mob.wheaty.R;
import com.tec_mob.wheaty.db.WheatyDataBase;
import com.tec_mob.wheaty.network.Smtp;

import java.util.concurrent.TimeUnit;

public class RecoverPasswordActivity extends AppCompatActivity {

    public static WheatyDataBase wheatyDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_recover_password);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        wheatyDataBase = Room.databaseBuilder(getApplicationContext(), WheatyDataBase.class, "userdb").allowMainThreadQueries().build();

        final EditText m = findViewById(R.id.recover_password);
        final Button confirm = findViewById(R.id.confirm_recover);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = m.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(mail.trim().matches(emailPattern)){
                    m.setText("");
                    if(wheatyDataBase.userDAO().getUsers(mail).size() > 0){
                        String newPassword = randomAlphaNumeric(8);
                        wheatyDataBase.userDAO().changePassword(mail, newPassword);
                        Smtp.resetPassword(mail, newPassword);
                        Toast.makeText(getApplicationContext(), "Email sent, check inbox!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(RecoverPasswordActivity.this, LogInActivity.class);
                        startActivity(i);

                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Email is not valid", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String randomAlphaNumeric(int count) {

        StringBuilder builder = new StringBuilder();

        while (count-- != 0) {

            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());

            builder.append(ALPHA_NUMERIC_STRING.charAt(character));

        }

        return builder.toString();

    }
}
