package com.tec_mob.wheaty.network;

import android.arch.persistence.room.Room;

import com.google.gson.Gson;
import com.tec_mob.wheaty.db.WheatyDataBase;
import com.tec_mob.wheaty.network.DTO.WeatherDTO;

import java.io.InputStream;

public class WeatherService {
    private String lat;
    private String lon;
    private String units;
    private String baseApi;
    private String apiKey;
    private String requestMethod;

    public static WheatyDataBase wheatyDataBase;

    public WeatherService() {
        this.baseApi = "https://api.openweathermap.org/data/2.5/weather";
        this.apiKey = "2ec832996bd0036fa29429b972f694b6";
        this.requestMethod = "GET";
        this.changeUnits(true);
        this.changeCoord("-31.41", "-64.18");
    }

    public WeatherService(String lat, String lon, Boolean units) {
        this.baseApi = "https://api.openweathermap.org/data/2.5/weather";
        this.apiKey = "2ec832996bd0036fa29429b972f694b6";
        this.requestMethod = "GET";
        this.changeUnits(units);
        this.changeCoord(lat, lon);
    }

    public WeatherDTO getData(){
        InputStream inputStream = Networking.httpGetStream(this.getUrl(), this.requestMethod);
        String inputStreamString = Networking.convertStreamToString(inputStream);
        return new Gson().fromJson(inputStreamString, WeatherDTO.class);
    }

    private String getUrl(){
        return  this.baseApi + "?lat=" + this.lat + "&lon=" + this.lon + "&units=" + this.units + "&appid=" + this.apiKey;
    }

    public void changeCoord(String lat, String lon){
        this.lat = lat;
        this.lon = lon;
    }

    public void changeUnits(Boolean units){
        this.units = units ? "metric" : "imperial";
    }
}
