package com.tec_mob.wheaty.db.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.tec_mob.wheaty.model.Forecast;

import java.util.List;

@Dao
public interface ForecastDAO {

    @Insert
    public void addForecast(Forecast forecast);

    @Query("SELECT * FROM ( SELECT * FROM forecast ORDER BY id DESC LIMIT 40 ) sub ORDER BY id ASC")
    public List<Forecast> getForecast();

    @Query("DELETE FROM forecast")
    public void deleteAll();
}
