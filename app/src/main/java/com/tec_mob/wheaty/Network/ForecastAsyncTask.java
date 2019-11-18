package com.tec_mob.wheaty.Network;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class ForecastAsyncTask extends AsyncTask<Void, Void, ForecastDTO> {
    public Forecast forecast;
    private LinkedHashMap<String, View> viewMap;
    private int index;

    public ForecastAsyncTask(LinkedHashMap<String, View> viewMap, int index){
        this.forecast = new Forecast();
        this.viewMap = viewMap;
        this.index = index;
    }

    @Override
    protected ForecastDTO doInBackground(Void... voids) {
        return forecast.getData();
    }

    @Override
    protected void onPostExecute(ForecastDTO forecastDTO) {

        for (LinkedHashMap.Entry<String, View> viewEntry : viewMap.entrySet()) {
            String key = viewEntry.getKey();
            ArrayList<String> keyList = new ArrayList<String>(Arrays.asList(key.split("\\.")));
            if(keyList.get(0).equals("list")){
                keyList.remove(0);
                if(keyList.get(0).equals("weather")){
                    keyList.remove(0);
                    if(keyList.get(0).equals("icon")){
                        LottieAnimationView animationView = (LottieAnimationView) viewEntry.getValue();
                        animationView.setAnimation(forecastDTO.list.get(this.index).weather.get(0).getElement(keyList).toString()+".json");
                        animationView.playAnimation();
                    }else{
                        TextView textView = (TextView) viewEntry.getValue();
                        textView.setText(forecastDTO.list.get(this.index).weather.get(0).getElement(keyList).toString());
                    }
                }else {
                    TextView textView = (TextView) viewEntry.getValue();
                    textView.setText(forecastDTO.list.get(this.index).getElement(keyList).toString());
                }
            }else{
                TextView textView = (TextView) viewEntry.getValue();
                textView.setText(forecastDTO.getElement(keyList).toString());
            }

        }
    }

    public void chageCoord(String lat, String lon){
        this.forecast.changeCoord(lat, lon);
    }

}
