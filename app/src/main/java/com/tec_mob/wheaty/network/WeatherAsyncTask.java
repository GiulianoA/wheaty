package com.tec_mob.wheaty.network;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.tec_mob.wheaty.network.DTO.WeatherDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class WeatherAsyncTask  extends AsyncTask<Void, Void, WeatherDTO> {
    public WeatherService weather;
    private LinkedHashMap<String, View> viewMap;

    public WeatherAsyncTask(LinkedHashMap<String, View> viewMap){
        // TODO
        this.weather = new WeatherService();
        this.viewMap = viewMap;
    }

    @Override
    protected WeatherDTO doInBackground(Void... voids) {
        return weather.getData();
    }

    @Override
    protected void onPostExecute(WeatherDTO weatherDTO) {

        for (LinkedHashMap.Entry<String, View> viewEntry : viewMap.entrySet()) {
            String key = viewEntry.getKey();
            ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(key.split("\\.")));
            if (keyList.get(0).equals("weather")) {
                keyList.remove(0);
                if (keyList.get(0).equals("icon")) {
                    LottieAnimationView animationView = (LottieAnimationView) viewEntry.getValue();
                    animationView.setAnimation(weatherDTO.weather.get(0).getElement(keyList).toString()+".json");
                    animationView.playAnimation();
                } else {
                    TextView textView = (TextView) viewEntry.getValue();
                    textView.setText(weatherDTO.weather.get(0).getElement(keyList).toString());
                }
            } else {
                TextView textView = (TextView) viewEntry.getValue();
                textView.setText(weatherDTO.getElement(keyList).toString());
            }
        }
    }

    public void chageCoord(String lat, String lon){
        this.weather.changeCoord(lat, lon);
    }

}
