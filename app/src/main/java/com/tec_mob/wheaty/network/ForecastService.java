package com.tec_mob.wheaty.network;
import java.io.InputStream;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.tec_mob.wheaty.network.DTO.ForecastDTO;

public class ForecastService {
    private String lat;
    private String lon;
    private String units;
    private String baseApi;
    private String apiKey;
    private String requestMethod;

    public ForecastService() {
        this.baseApi = "https://api.openweathermap.org/data/2.5/forecast";
        this.apiKey = "2ec832996bd0036fa29429b972f694b6";
        this.requestMethod = "GET";
        this.changeUnits(true);
        this.changeCoord("-31.41", "-64.18");
    }

    public ForecastService(String lat, String lon, Boolean units) {
        this.baseApi = "https://api.openweathermap.org/data/2.5/forecast";
        this.apiKey = "2ec832996bd0036fa29429b972f694b6";
        this.requestMethod = "GET";
        this.changeUnits(units);
        this.changeCoord(lat, lon);
    }

    public ForecastDTO getData() {
        InputStream inputStream = Networking.httpGetStream(this.getUrl(), this.requestMethod);
        String inputStreamString = Networking.convertStreamToString(inputStream);
        return new Gson().fromJson(inputStreamString, ForecastDTO.class);
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
