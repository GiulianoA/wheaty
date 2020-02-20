package com.tec_mob.wheaty.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_email")
    private String email;
    @ColumnInfo(name = "user_name")
    private String name;
    @ColumnInfo(name = "user_lastName")
    private String lastName;
    @ColumnInfo(name = "user_password")
    private String password;
    @ColumnInfo(name = "unidades")
    private Boolean unidades;
    @ColumnInfo(name = "notificaciones")
    private Boolean notificaciones;
    @ColumnInfo(name = "hora")
    private String hora;
    @ColumnInfo(name = "minutos")
    private String minutos;
    @ColumnInfo(name = "remember_me")
    private Boolean rememberMe;
    @ColumnInfo(name = "current_user")
    private Boolean currentUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getUnidades() {
        return unidades;
    }

    public void setUnidades(Boolean unidades) {
        this.unidades = unidades;
    }

    public Boolean getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(Boolean notificaciones) {
        this.notificaciones = notificaciones;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMinutos() {
        return minutos;
    }

    public void setMinutos(String minutos) {
        this.minutos = minutos;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public Boolean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Boolean currentUser) {
        this.currentUser = currentUser;
    }
}
