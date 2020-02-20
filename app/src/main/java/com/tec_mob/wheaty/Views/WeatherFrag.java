package com.tec_mob.wheaty.Views;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.tec_mob.wheaty.GPS;
import com.tec_mob.wheaty.R;
import com.tec_mob.wheaty.db.WheatyDataBase;
import com.tec_mob.wheaty.model.Forecast;
import com.tec_mob.wheaty.model.User;
import com.tec_mob.wheaty.model.Weather;
import com.tec_mob.wheaty.network.DTO.ForecastDTO;
import com.tec_mob.wheaty.network.DTO.WeatherDTO;
import com.tec_mob.wheaty.network.WeatherService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;


public class WeatherFrag extends Fragment {


    public static WheatyDataBase wheatyDataBase;
    WeatherAsyncTask weatherAsyncTask = new WeatherAsyncTask();
    Boolean unit = true;
    TextView temperature;
    TextView city;
    TextView tempMax;
    TextView tempMin;
    TextView status;
    TextView date;
    TextView wind;
    LottieAnimationView animationView;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_temp, container, false);

        wheatyDataBase = Room.databaseBuilder(container.getContext(), WheatyDataBase.class, "userdb").allowMainThreadQueries().build();

        // Unit
        List<User> user = wheatyDataBase.userDAO().getCurrentUser();
        if (user.size() > 0) {
            unit = user.get(0).getUnidades();
        }

        temperature = (TextView) root.findViewById(R.id.temp);
        city = (TextView) root.findViewById(R.id.city);
        tempMax = (TextView) root.findViewById(R.id.tempMax);
        tempMin = (TextView) root.findViewById(R.id.tempMin);
        status = (TextView) root.findViewById(R.id.status);
        date = (TextView) root.findViewById(R.id.date);
        wind = (TextView) root.findViewById(R.id.wind);
        animationView = (LottieAnimationView) root.findViewById(R.id.animation_view);

        // read BD for data
        List<Weather> weatherList = wheatyDataBase.weatherDAO().getWeather();
        if(weatherList.size() > 0) {
            setData(weatherList.get(0));
        }

        if(isNetworkConnected(container.getContext())) {
            executeAsyncTask();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(container.getContext());
            builder.setMessage("Couldn't establish connection")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setTitle("Ops!")
                    .create();
            builder.show();
        }

        FloatingActionButton floatingActionButton = root.findViewById(R.id.update);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context cnt = container.getContext();
                if(isNetworkConnected(cnt)) {
                    executeAsyncTask();
                }
            }
        });

        // Inflate the layout for this fragment
        return root;

    }

    public void executeAsyncTask(){
        WeatherAsyncTask asyncTask = new WeatherAsyncTask();

        GPS g = new GPS(getActivity().getApplicationContext());
        Location l = g.getLocation();
        if(l != null){
            double latitude = l.getLatitude();
            double longitude = l.getLongitude();
            asyncTask.chageCoord(String.valueOf(latitude), String.valueOf(longitude));
        }

        weatherAsyncTask.changeUnits(unit);

        asyncTask.execute();
    }

    public class WeatherAsyncTask  extends AsyncTask<Void, Void, WeatherDTO> {
        public WeatherService weather;

        public WeatherAsyncTask(){
            this.weather = new WeatherService();
        }

        @Override
        protected WeatherDTO doInBackground(Void... voids) {
            return weather.getData();
        }

        @Override
        protected void onPostExecute(WeatherDTO weatherDTO) {
            // save weatherDTO to DB
            wheatyDataBase.weatherDAO().deleteAll();
            Weather weather = new  Weather(weatherDTO.main.temp, weatherDTO.main.tempMin, weatherDTO.main.tempMax, weatherDTO.weather.get(0).icon, weatherDTO.dt, weatherDTO.name, weatherDTO.weather.get(0).main, weatherDTO.wind.speed);
            wheatyDataBase.weatherDAO().addWeather(weather);

            // read BD for data
            List<Weather> weatherList = wheatyDataBase.weatherDAO().getWeather();
            if(weatherList.size() > 0) {
                setData(weatherList.get(0));
            }

        }

        public void chageCoord(String lat, String lon){
            this.weather.changeCoord(lat, lon);
        }
        public void changeUnits(Boolean units){
            this.weather.changeUnits(units);
        }

    }

    public void setData(Weather weather){
        temperature.setText(String.format("%s°%s", weather.getTemp(), unit?"C":"F"));
        tempMax.setText(String.format("%s°%s", weather.getTempMax(), unit?"C":"F"));
        tempMin.setText(String.format("%s°%s", weather.getTempMin(), unit?"C":"F"));
        city.setText(weather.getCity());
        status.setText(weather.getStatus());
        wind.setText(String.format("%s°%s", weather.wind, unit?"k/m":"p/s"));
        animationView.setAnimation(weather.icon + ".json");
        Date weatherDate = new Date(weather.getDt());
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getDefault());
        date.setText(timeFormat.format(weatherDate));
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

}
