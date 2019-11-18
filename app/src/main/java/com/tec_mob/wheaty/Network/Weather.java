package com.tec_mob.wheaty.Network;

import com.google.gson.Gson;
import com.tec_mob.wheaty.Network.Networking;
import com.tec_mob.wheaty.Network.WeatherDTO;

import java.io.InputStream;

public class Weather {
    private String lat;
    private String lon;
    private String units;
    private String baseApi;
    private String apiKey;
    private String requestMethod;

    public Weather() {
        this.lat = "-31.41";
        this.lon = "-64.18";
        this.units = "metric";
        this.baseApi = "https://api.openweathermap.org/data/2.5/weather";
        this.apiKey = "2ec832996bd0036fa29429b972f694b6";
        this.requestMethod = "GET";
    }

    public Weather(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
        this.units = "metric";
        this.baseApi = "https://api.openweathermap.org/data/2.5/weather";
        this.apiKey = "2ec832996bd0036fa29429b972f694b6";
        this.requestMethod = "GET";
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
}
