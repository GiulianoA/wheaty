package com.tec_mob.wheaty;
import java.io.InputStream;
import java.util.AbstractSet;

import com.google.gson.Gson;

public class Forecast {
    private String lat;
    private String lon;
    private String units;
    private String baseApi;
    private String apiKey;
    private String requestMethod;

    public Forecast() {
        lat = "-31.41";
        lon = "-64.18";
        units = "metric";
        baseApi = "https://api.openweathermap.org/data/2.5/forecast";
        apiKey = "2ec832996bd0036fa29429b972f694b6";
        requestMethod = "GET";
    }

    public Forecast(String lat, String lon) {
        this.lat = lat;
        this.lon = lon;
        this.units = "metric";
        this.baseApi = "https://api.openweathermap.org/data/2.5/forecast";
        this.apiKey = "2ec832996bd0036fa29429b972f694b6";
        this.requestMethod = "GET";
    }

    public ForecastDTO getData(){
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
}
