package com.tec_mob.wheaty.Network;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ForecastDTO {

    @SerializedName("list")
    public List<ForecastList> list;
    @SerializedName("city")
    public ForecastListCity city;
    @SerializedName("date")
    public String date = new  SimpleDateFormat("HH:mm:ss").format(new Date());

    public class ForecastList {
        @SerializedName("dt")
        public Long dt;
        @SerializedName("main")
        public ForecastListMain main;
        @SerializedName("weather")
        public List<ForecastListWeather> weather;
        @SerializedName("wind")
        public ForecastListWind wind;
        @SerializedName("dt_txt")
        public String dt_txt;

        public Object getElement(ArrayList<String> keyList){
            return ForecastDTO.getRecursiveElement(this, keyList);
        }
    }

    public class ForecastListMain {
        @SerializedName("temp")
        public float temp;
        @SerializedName("temp_min")
        public float temp_min;
        @SerializedName("temp_max")
        public  float temp_max;
        @SerializedName("humidity")
        public float humidity;

        public Object getElement(ArrayList<String> keyList){
            return ForecastDTO.getRecursiveElement(this, keyList);
        }
    }

    public class ForecastListWeather {
        @SerializedName("main")
        public String main;
        @SerializedName("description")
        public String description;
        @SerializedName("icon")
        public String icon;

        public Object getElement(ArrayList<String> keyList){
            return ForecastDTO.getRecursiveElement(this, keyList);
        }
    }

    public class ForecastListWind {
        @SerializedName("speed")
        public float speed;
        @SerializedName("deg")
        public float deg;

        public Object getElement(ArrayList<String> keyList){
            return ForecastDTO.getRecursiveElement(this, keyList);
        }
    }

    public class ForecastListCity {
        @SerializedName("name")
        public String name;
        @SerializedName("coord")
        public ForecastListCityCoord coord;

        public Object getElement(ArrayList<String> keyList){
            return ForecastDTO.getRecursiveElement(this, keyList);
        }
    }

    public class ForecastListCityCoord {
        @SerializedName("lat")
        public String lat;
        @SerializedName("lon")
        public String lon;

        public Object getElement(ArrayList<String> keyList){
            return ForecastDTO.getRecursiveElement(this, keyList);
        }
    }

    public Object getElement(ArrayList<String> keyList){
        return getRecursiveElement(this, keyList);
    }

    private static Object getRecursiveElement(Object obj, ArrayList<String> keyList){
        if(keyList.size() <= 1){
            return getSingleElement(obj, keyList.get(0));
        } else {
            Object temp = getSingleElement(obj, keyList.get(0));
            keyList.remove(0);
            return getRecursiveElement(temp, keyList);
        }
    }

    private static Object getSingleElement(Object obj, String key){
        try{
            return obj.getClass().getDeclaredField(key).get(obj);
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return "-";
    }
}