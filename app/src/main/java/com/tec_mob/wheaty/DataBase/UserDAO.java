package com.tec_mob.wheaty.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    public void addUser(User user);

    @Query("select * from users where user_email== :email ")
    public List<User> getUsers(String email);
}
