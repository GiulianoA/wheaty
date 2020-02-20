package com.tec_mob.wheaty.Views;



import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tec_mob.wheaty.R;
import com.tec_mob.wheaty.model.User;
import com.tec_mob.wheaty.db.WheatyDataBase;

public class RegisterActivity extends AppCompatActivity {

    public static WheatyDataBase wheatyDataBase;

    EditText name, lastName, email, password, confirmPassword;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        wheatyDataBase = Room.databaseBuilder(getApplicationContext(), WheatyDataBase.class, "userdb").allowMainThreadQueries().build();

        name = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);

        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = name.getText().toString();
                String userLastName = lastName.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String userConfirmaPassword = confirmPassword.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(userName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Complete name field", Toast.LENGTH_SHORT).show();
                }else if(userLastName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Complete last name field", Toast.LENGTH_SHORT).show();
                }else if(userEmail.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Complete email field", Toast.LENGTH_SHORT).show();
                }else if(userEmail.trim().matches(emailPattern)){
                    if(userPassword.isEmpty() || userConfirmaPassword.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Complete password field", Toast.LENGTH_SHORT).show();
                    }else{
                        if(userPassword.equals(userConfirmaPassword)){
                            User user = new User();
                            user.setName(userName);
                            user.setLastName(userLastName);
                            user.setEmail(userEmail);
                            user.setPassword(userPassword);
                            user.setHora("08");
                            user.setMinutos("00");
                            user.setNotificaciones(false);
                            user.setUnidades(true);
                            user.setRememberMe(false);

                            wheatyDataBase.userDAO().addUser(user);

                            Toast.makeText(getApplicationContext(), "Successful register", Toast.LENGTH_SHORT).show();

                            name.setText("");
                            lastName.setText("");
                            email.setText("");
                            password.setText("");
                            confirmPassword.setText("");

                            Intent i = new Intent(RegisterActivity.this, LogInActivity.class);
                            startActivity(i);

                        }else{
                            Toast.makeText(getApplicationContext(), "Incorrects Passwords", Toast.LENGTH_SHORT).show();
                            password.setText("");
                            confirmPassword.setText("");
                        }
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Email is not valid", Toast.LENGTH_SHORT).show();
                }





            }
        });
    }
}
