package com.tec_mob.wheaty.network.DTO;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WeatherDTO {
    @SerializedName("dt")
    public Long dt;
    @SerializedName("main")
    public WeatherDTO.WeatherMain main;
    @SerializedName("weather")
    public List<WeatherDTO.WeatherWeather> weather;
    @SerializedName("wind")
    public WeatherDTO.WeatherWind wind;
    @SerializedName("name")
    public String name;
    @SerializedName("coord")
    public WeatherDTO.WeatherCoord coord;
    @SerializedName("date")
    public String date = new  SimpleDateFormat("HH:mm:ss").format(new Date());

    public class WeatherMain {
        @SerializedName("temp")
        public float temp;
        @SerializedName("temp_min")
        public float tempMin;
        @SerializedName("temp_max")
        public  float tempMax;
        @SerializedName("humidity")
        public float humidity;
    }

    public class WeatherWeather {
        @SerializedName("main")
        public String main;
        @SerializedName("description")
        public String description;
        @SerializedName("icon")
        public String icon;
    }

    public class WeatherWind {
        @SerializedName("speed")
        public float speed;
        @SerializedName("deg")
        public float deg;
    }

    public class WeatherCoord {
        @SerializedName("lat")
        public String lat;
        @SerializedName("lon")
        public String lon;
    }
}
