package com.tec_mob.wheaty.Network;

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
        public float temp_min;
        @SerializedName("temp_max")
        public  float temp_max;
        @SerializedName("humidity")
        public float humidity;

        public Object getElement(ArrayList<String> keyList){
            return WeatherDTO.getRecursiveElement(this, keyList);
        }
    }

    public class WeatherWeather {
        @SerializedName("main")
        public String main;
        @SerializedName("description")
        public String description;
        @SerializedName("icon")
        public String icon;

        public Object getElement(ArrayList<String> keyList){
            return WeatherDTO.getRecursiveElement(this, keyList);
        }
    }

    public class WeatherWind {
        @SerializedName("speed")
        public float speed;
        @SerializedName("deg")
        public float deg;

        public Object getElement(ArrayList<String> keyList){
            return WeatherDTO.getRecursiveElement(this, keyList);
        }
    }

    public class WeatherCoord {
        @SerializedName("lat")
        public String lat;
        @SerializedName("lon")
        public String lon;

        public Object getElement(ArrayList<String> keyList){
            return WeatherDTO.getRecursiveElement(this, keyList);
        }
    }

    public Object getElement(ArrayList<String> keyList){
        return getRecursiveElement(this, keyList);
    }

    // Implementacion resursiva del getter generico, para admitir anidamiento
    private static Object getRecursiveElement(Object obj, ArrayList<String> keyList){
        if(keyList.size() <= 1){
            return getSingleElement(obj, keyList.get(0));
        } else {
            Object temp = getSingleElement(obj, keyList.get(0));
            keyList.remove(0);
            return getRecursiveElement(temp, keyList);
        }
    }
    //Se le pasa en string (key) el nombre del atributo de un objeto y te devuelve su valor. (Es un getter generico)
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
