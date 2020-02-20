package com.tec_mob.wheaty.network.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastDTO {

    @SerializedName("list")
    public List<ForecastList> list;

    public class ForecastList {
        @SerializedName("main")
        public ForecastListMain main;
        @SerializedName("weather")
        public List<ForecastListWeather> weather;
        @SerializedName("dt_txt")
        public String dt;
    }

    public class ForecastListMain {
        @SerializedName("temp")
        public float temp;
        @SerializedName("temp_min")
        public float tempMin;
        @SerializedName("temp_max")
        public  float tempMax;
    }

    public class ForecastListWeather {
        @SerializedName("icon")
        public String icon;
    }
}