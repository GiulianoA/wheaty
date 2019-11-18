package com.tec_mob.wheaty;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.LinkedHashMap;



public class TempFrag extends Fragment {

    LinkedHashMap<String, View> viewMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_temp, container, false);

        TextView temperature = (TextView) root.findViewById(R.id.temp);
        TextView city = (TextView) root.findViewById(R.id.city);
        TextView tempMax = (TextView) root.findViewById(R.id.tempMax);
        TextView tempMin = (TextView) root.findViewById(R.id.tempMin);
        TextView status = (TextView) root.findViewById(R.id.status);
        TextView date = (TextView) root.findViewById(R.id.date);
        TextView wind = (TextView) root.findViewById(R.id.wind);
        LottieAnimationView animationView = (LottieAnimationView) root.findViewById(R.id.animation_view);

        viewMap = new LinkedHashMap<>();
        viewMap.put("main.temp", temperature);
        viewMap.put("name", city);
        viewMap.put("main.temp_max", tempMax);
        viewMap.put("main.temp_min", tempMin);
        viewMap.put("weather.main", status);
        viewMap.put("date", date);
        viewMap.put("wind.speed", wind);
        viewMap.put("weather.icon", animationView);

        executeAsyncTask();

        FloatingActionButton floatingActionButton = root.findViewById(R.id.update);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                executeAsyncTask();
            }
        });

        // Inflate the layout for this fragment
        return root;

    }

    public void executeAsyncTask(){
        WeatherAsyncTask asyncTask = new WeatherAsyncTask(viewMap);
        //asyncTask.chageCoord(lat, lon);
        asyncTask.execute();
    }

}
