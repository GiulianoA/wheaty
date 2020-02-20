package com.tec_mob.wheaty.Views;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tec_mob.wheaty.GPS;
import com.tec_mob.wheaty.R;
import com.tec_mob.wheaty.db.WheatyDataBase;
import com.tec_mob.wheaty.model.Forecast;
import com.tec_mob.wheaty.model.User;
import com.tec_mob.wheaty.network.DTO.ForecastDTO;
import com.tec_mob.wheaty.network.ForecastService;

import java.util.List;


public class ForecastFrag extends Fragment {

    public static WheatyDataBase wheatyDataBase;
    ForecastAsyncTask forecastAsyncTask = new ForecastAsyncTask();
    RecyclerView recyclerView;
    Boolean unit = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        wheatyDataBase = Room.databaseBuilder(container.getContext(), WheatyDataBase.class, "userdb").allowMainThreadQueries().build();

        View vista = inflater.inflate(R.layout.fragment_forecast, container, false);

        recyclerView = vista.findViewById(R.id.forecastRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Unit
        List<User> user = wheatyDataBase.userDAO().getCurrentUser();
        if (user.size() > 0) {
            unit = user.get(0).getUnidades();
        }

        // read BD for data
        List<Forecast> forecastList = wheatyDataBase.forecastDTO().getForecast();
        ForecastAdapter forecastAdapter = new ForecastAdapter(forecastList, unit);
        recyclerView.setAdapter(forecastAdapter);

        if(isNetworkConnected(container.getContext())) {
            executeAsyncTask();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
            builder.setMessage("Error de conexion")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setTitle("Ops!")
                    .create();
            builder.show();
        }

        return vista;
    }

    public void executeAsyncTask(){

        GPS g = new GPS(getActivity().getApplicationContext());
        Location l = g.getLocation();
        if(l != null){
            double latitude = l.getLatitude();
            double longitude = l.getLongitude();
            forecastAsyncTask.changeCoord(String.valueOf(latitude), String.valueOf(longitude));
        }

        forecastAsyncTask.changeUnits(unit);

        forecastAsyncTask.execute();
    }

    public class ForecastAsyncTask extends AsyncTask<Void, Void, ForecastDTO> {
        public ForecastService forecast;

        public ForecastAsyncTask(){
            this.forecast = new ForecastService();
        }

        @Override
        protected ForecastDTO doInBackground(Void... voids) {
            return forecast.getData();
        }

        @Override
        protected void onPostExecute(ForecastDTO forecastDTO) {
            // save forecastDTO to DB
            wheatyDataBase.forecastDTO().deleteAll();
            for(ForecastDTO.ForecastList fc : forecastDTO.list){
                Forecast forecast = new Forecast(fc.main.temp, fc.main.tempMin, fc.main.tempMax, fc.weather.get(0).icon, fc.dt);
                wheatyDataBase.forecastDTO().addForecast(forecast);
            }

            // read BD for data
            List<Forecast> forecastList = wheatyDataBase.forecastDTO().getForecast();
            ForecastAdapter forecastAdapter = new ForecastAdapter(forecastList, unit);
            recyclerView.setAdapter(forecastAdapter);
        }

        public void changeCoord(String lat, String lon){
            this.forecast.changeCoord(lat, lon);
        }
        public void changeUnits(Boolean units){
            this.forecast.changeUnits(units);
        }

    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
