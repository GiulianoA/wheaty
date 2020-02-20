package com.tec_mob.wheaty.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "forecast")
public class Forecast {

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
    public String dt;

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

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public Forecast(float temp, float tempMin, float tempMax, String icon, String dt) {
        this.temp = temp;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.icon = icon;
        this.dt = dt;
    }
}
