package com.tec_mob.wheaty.Views;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.tec_mob.wheaty.R;
import com.tec_mob.wheaty.model.User;
import com.tec_mob.wheaty.db.WheatyDataBase;

import java.util.List;


public class LogInActivity extends AppCompatActivity {

    public static WheatyDataBase wheatyDataBase;

    private Button registerButton;
    private Button signIn;

    EditText email, password;
    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        wheatyDataBase = Room.databaseBuilder(getApplicationContext(), WheatyDataBase.class, "userdb").allowMainThreadQueries().build();

        List<User> sessionUser = wheatyDataBase.userDAO().getSessionUser();
        if(sessionUser.size() > 0){
            Intent i = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(i);
            Toast.makeText(getApplicationContext(), "Welcome to Wheaty " + sessionUser.get(0).getName() + "!", Toast.LENGTH_LONG).show();
            finish();
        }

        signIn();
        register();
    }

    public void signIn(){



        signIn = findViewById(R.id.sign_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = findViewById(R.id.logInEmail);
                password = findViewById(R.id.logInPassword);
                remember = findViewById(R.id.rememberme);

                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                Boolean rememberMe = remember.isChecked();

                if(userEmail.isEmpty() && userPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
                }else {
                    try{
                        List<User> user = wheatyDataBase.userDAO().getUsers(userEmail);

                        if (userEmail.equals(user.get(0).getEmail()) && userPassword.equals(user.get(0).getPassword())) {
                            wheatyDataBase.userDAO().setRememberMe(rememberMe, user.get(0).getEmail());
                            wheatyDataBase.userDAO().setCurrentUser(user.get(0).getEmail());
                            Intent i = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), "Welcome to Wheaty " + user.get(0).getName() + "!", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
                            email.setText("");
                            password.setText("");
                        }
                    }catch (IndexOutOfBoundsException e){
                        Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
                    }

                }


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
