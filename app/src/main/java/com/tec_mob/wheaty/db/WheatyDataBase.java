package com.tec_mob.wheaty.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.tec_mob.wheaty.db.DAO.ForecastDAO;
import com.tec_mob.wheaty.db.DAO.UserDAO;
import com.tec_mob.wheaty.db.DAO.WeatherDAO;
import com.tec_mob.wheaty.model.Forecast;
import com.tec_mob.wheaty.model.User;
import com.tec_mob.wheaty.model.Weather;

@Database(entities = {User.class, Forecast.class, Weather.class}, version = 1, exportSchema = false)
public abstract class WheatyDataBase extends RoomDatabase {

    private static volatile WheatyDataBase INSTANCE;

    static WheatyDataBase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WheatyDataBase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WheatyDataBase.class, "WheatyDataBase").build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract UserDAO userDAO();
    public abstract ForecastDAO forecastDAO();
    public abstract WeatherDAO weatherDAO();
}
