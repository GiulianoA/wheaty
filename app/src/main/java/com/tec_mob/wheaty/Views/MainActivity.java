package com.tec_mob.wheaty.Views;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tec_mob.wheaty.GPS;
import com.tec_mob.wheaty.R;
import com.tec_mob.wheaty.db.WheatyDataBase;
import com.tec_mob.wheaty.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MenuView.ItemView btnFragTemp, btnFragForec;

    public static WheatyDataBase wheatyDataBase;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private DrawerLayout drawer;

    public void FragTemp(){
        //ragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        WeatherFrag weatherFrag = (WeatherFrag)fragmentManager.findFragmentByTag("weatherFrag");
        ForecastFrag forecastFrag = (ForecastFrag)fragmentManager.findFragmentByTag("forecastFrag");


        if(weatherFrag == null || !weatherFrag.isVisible()) {

            if(forecastFrag != null && !forecastFrag.isHidden()){
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(forecastFrag);
                fragmentTransaction.commit();
                fragmentManager.popBackStack();
            }
            fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if(weatherFrag == null)
                weatherFrag = new WeatherFrag();
            else
                fragmentTransaction.show(weatherFrag);
            fragmentTransaction.replace(R.id.contenedor, weatherFrag,"weatherFrag");

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }



    public void FragForec(){
        //fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
        ForecastFrag forecastFrag = (ForecastFrag)fragmentManager.findFragmentByTag("forecastFrag");
        WeatherFrag weatherFrag = (WeatherFrag)fragmentManager.findFragmentByTag("weatherFrag");

        if(forecastFrag == null || !forecastFrag.isVisible()){

            if(weatherFrag != null && !weatherFrag.isHidden()){
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.hide(weatherFrag);
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

    public void FragSettings(){

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentManager.popBackStack();

        fragmentTransaction.replace(R.id.contenedor, new SettingsFrag(), "settingsFrag");

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

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

        wheatyDataBase = Room.databaseBuilder(getApplicationContext(), WheatyDataBase.class, "userdb").allowMainThreadQueries().build();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_message_login)
                .setPositiveButton(R.string.dialog_positive_login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wheatyDataBase.userDAO().logOut();
                        Intent i = new Intent(MainActivity.this, LogInActivity.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setTitle("Logout")
                .create();
        builder.show();

    }

    public void GPSButton(){
        FloatingActionButton floatingActionButton = findViewById(R.id.gps);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView lonT = findViewById(R.id.lon);
                TextView latT = findViewById(R.id.lat);
                TextView city = findViewById(R.id.city);

                GPS g = new GPS(getApplicationContext());
                Location l = g.getLocation();
                if(l != null) {
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();

                    lonT.setText(String.valueOf(lon));
                    latT.setText(String.valueOf(lat));

                    Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = null;
                    try {
                        addresses = gcd.getFromLocation(lat, lon, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addresses != null && addresses.size() > 0) {
                        String locality = addresses.get(0).getLocality();
                        String country = addresses.get(0).getCountryName();
                        city.setText(locality+", "+country);
                    }

                }


            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PackageManager.GET_PERMISSIONS);

        }

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

        GPSButton();



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

            case R.id.settings:
                FragSettings();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
