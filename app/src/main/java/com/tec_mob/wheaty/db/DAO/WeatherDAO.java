package com.tec_mob.wheaty.db.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.tec_mob.wheaty.model.Weather;

import java.util.List;

@Dao
public interface WeatherDAO {

    @Insert
    public void addWeather(Weather weather);

    @Query("SELECT * FROM ( SELECT * FROM weather ORDER BY id DESC LIMIT 1 ) sub ORDER BY id ASC")
    public List<Weather> getWeather();

    @Query("DELETE FROM weather")
    public void deleteAll();
}
