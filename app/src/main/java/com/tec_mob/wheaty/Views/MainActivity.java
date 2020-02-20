package com.tec_mob.wheaty.Views;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
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


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MenuView.ItemView btnFragTemp, btnFragForec;

    public static WheatyDataBase wheatyDataBase;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    private DrawerLayout drawer;

    public void FragTemp(){
        //ragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
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
        //fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
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

                GPS g = new GPS(getApplicationContext());
                Location l = g.getLocation();
                if(l != null){
                    double lat = l.getLatitude();
                    double lon = l.getLongitude();

                    lonT.setText(String.valueOf(lon));
                    latT.setText(String.valueOf(lat));

                    //Toast.makeText(getApplicationContext(),"LAT: "+lat+"\n LON: "+lon, Toast.LENGTH_LONG).show();
                }/* else {
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
                }*/
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
