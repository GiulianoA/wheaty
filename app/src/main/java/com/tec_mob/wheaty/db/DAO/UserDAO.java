package com.tec_mob.wheaty.db.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.tec_mob.wheaty.model.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    public void addUser(User user);

    @Query("select * from users where user_email== :email ")
    public List<User> getUsers(String email);

    @Query("update users set remember_me = :remember where user_email == :email")
    public void setRememberMe(Boolean remember, String email);

    @Query("update users set unidades = :unidades where user_email == :email")
    public void setUnidades(Boolean unidades, String email);

    @Query("update users set notificaciones = :notificaciones where user_email == :email")
    public void setNotificaciones(Boolean notificaciones, String email);

    @Query("update users set hora = :hora, minutos = :minutos where user_email == :email")
    public void setTiempo(String hora, String minutos, String email);

    @Query("select * from users where remember_me == 1")
    public  List<User> getSessionUser();

    @Query("select * from users where current_user == 1")
    public  List<User> getCurrentUser();

    @Query("update users set current_user = 1 where user_email == :email")
    public void setCurrentUser(String email);

    @Query("update users set user_password = :password where user_email == :email")
    public void changePassword(String email, String password);

    @Query("update users set remember_me = 0, current_user = 0")
    public void logOut();
}