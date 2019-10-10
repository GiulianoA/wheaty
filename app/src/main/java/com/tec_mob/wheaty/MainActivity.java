package com.tec_mob.wheaty;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    MenuView.ItemView btnFragTemp, btnFragForec;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private DrawerLayout drawer;

    public void FragTemp(){
        fragmentManager = getSupportFragmentManager();

        TempFrag tempFrag = (TempFrag)fragmentManager.findFragmentByTag("tempFrag");
        ForecastFrag forecastFrag = (ForecastFrag)fragmentManager.findFragmentByTag("forecastFrag");


        if(tempFrag == null || !tempFrag.isVisible()) {

            if(forecastFrag != null && !forecastFrag.isHidden()){
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(forecastFrag);
                fragmentTransaction.commit();
                fragmentManager.popBackStack();
            }
            fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if(tempFrag == null)
                tempFrag = new TempFrag();
            else
                fragmentTransaction.show(tempFrag);
            fragmentTransaction.replace(R.id.contenedor, tempFrag,"tempFrag");

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public void FragForec(){
        fragmentManager = getSupportFragmentManager();

        ForecastFrag forecastFrag = (ForecastFrag)fragmentManager.findFragmentByTag("forecastFrag");
        TempFrag tempFrag = (TempFrag)fragmentManager.findFragmentByTag("tempFrag");

        if(forecastFrag == null || !forecastFrag.isVisible()){

            if(tempFrag != null && !tempFrag.isHidden()){
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(tempFrag);
                fragmentTransaction.commit();
                fragmentManager.popBackStack();
            }
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if(forecastFrag == null)
                forecastFrag = new ForecastFrag();
            else
                fragmentTransaction.show(forecastFrag);

            fragmentTransaction.replace(R.id.contenedor, forecastFrag, "forecastFrag");

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public void email(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"galessandro515@alumnos.iua.edu.ar"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Wheaty App report");
        Intent mailer = Intent.createChooser(intent, "Send email using: ");
        startActivity(mailer);
    }

    public void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message_login)
                .setPositiveButton(R.string.dialog_positive_login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(MainActivity.this, LogInActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setTitle("Logout")
                .create();
        builder.show();

    }

    public void gps(){
        FloatingActionButton floatingActionButton = findViewById(R.id.gps);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.dialog_message_gps)
                        .setPositiveButton(R.string.dialog_positive_gps, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setTitle("GPS")
                        .create();
                builder.show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        btnFragTemp = findViewById(R.id.btnNavFragTemp);
        ((View) btnFragTemp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragTemp();
            }
        });

        btnFragForec = findViewById(R.id.btnNavFragForec);
        ((View) btnFragForec).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               FragForec();
           }
       });

        gps();

    }

    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.contact:
                email();
                break;

            case R.id.logout:
                logout();
                break;
        }

        return true;
    }
}
