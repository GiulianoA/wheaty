package com.tec_mob.wheaty.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "weather")
public class Weather {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "temp")
    public float temp;
    @ColumnInfo(name = "temp_min")
    public float tempMin;
    @ColumnInfo(name = "temp_max")
    public  float tempMax;
    @ColumnInfo(name = "icon")
    public String icon;
    @ColumnInfo(name = "dt_txt")
    public Long dt;
    @ColumnInfo(name = "city")
    public String city;
    @ColumnInfo(name = "status")
    public String status;
    @ColumnInfo(name = "wind")
    public Float wind;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(float tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(float tempMax) {
        this.tempMax = tempMax;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getWind() {
        return wind;
    }

    public void setWind(Float wind) {
        this.wind = wind;
    }

    public Weather(float temp, float tempMin, float tempMax, String icon, Long dt, String city, String status, Float wind) {
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.icon = icon;
        this.dt = dt;
        this.city = city;
        this.status = status;
        this.wind = wind;
    }
}
