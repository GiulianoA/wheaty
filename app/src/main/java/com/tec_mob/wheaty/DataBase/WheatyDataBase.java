package com.tec_mob.wheaty.DataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {User.class}, version = 1)
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
}
