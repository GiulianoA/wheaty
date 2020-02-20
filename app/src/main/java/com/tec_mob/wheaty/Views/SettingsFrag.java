package com.tec_mob.wheaty.Views;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tec_mob.wheaty.Notification.AlarmReceiver;
import com.tec_mob.wheaty.R;
import com.tec_mob.wheaty.model.User;
import com.tec_mob.wheaty.db.WheatyDataBase;

import java.util.Calendar;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;


public class SettingsFrag extends Fragment {

    public static WheatyDataBase wheatyDataBase;
    EditText chooseTimee;
    Switch unidadesSwitch, notificacionesSwitch;

    EditText chooseTime;
    int alarmID = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Leemos la BD
        wheatyDataBase = Room.databaseBuilder(container.getContext(), WheatyDataBase.class, "userdb").allowMainThreadQueries().build();
        final List<User> currentUser = wheatyDataBase.userDAO().getCurrentUser();

        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_settings, container, false);

        unidadesSwitch = (Switch) root.findViewById(R.id.unidades);

        notificacionesSwitch = (Switch) root.findViewById(R.id.notificaciones);
        notificacionesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean switchState = notificacionesSwitch.isChecked();
            }
        });

        chooseTime = root.findViewById(R.id.hora);

        // Seteamos valores de la BD
        if(currentUser.size() > 0){
            unidadesSwitch.setChecked(currentUser.get(0).getUnidades());
            notificacionesSwitch.setChecked(currentUser.get(0).getNotificaciones());
            chooseTime.setText(currentUser.get(0).getHora() + ":" + currentUser.get(0).getMinutos());
        }

        notificacionesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean switchState = notificacionesSwitch.isChecked();
                // Actualizamos la BD
                if(currentUser.size() > 0){
                    wheatyDataBase.userDAO().setNotificaciones(switchState, currentUser.get(0).getEmail());
                }
            }
        });

        unidadesSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean switchState = unidadesSwitch.isChecked();
                // Actualizamos la BD
                // Actualizamos la BD
                if(currentUser.size() > 0){
                    wheatyDataBase.userDAO().setUnidades(switchState, currentUser.get(0).getEmail());
                }
            }
        });

        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String finalHour, finalMinute;

                        finalHour = "" + selectedHour;
                        finalMinute = "" + selectedMinute;
                        if (selectedHour < 10) finalHour = "0" + selectedHour;
                        if (selectedMinute < 10) finalMinute = "0" + selectedMinute;
                        chooseTime.setText(finalHour + ":" + finalMinute);

                        Calendar today = Calendar.getInstance();

                        today.set(Calendar.HOUR_OF_DAY, selectedHour);
                        today.set(Calendar.MINUTE, selectedMinute);
                        today.set(Calendar.SECOND, 0);

                        //Toast.makeText(view.getContext(), getString("Alarm Changed", finalHour + ":" + finalMinute), Toast.LENGTH_LONG).show();
                        setAlarm(alarmID, today.getTimeInMillis(), getContext());
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Choose time");
                mTimePicker.show();


            }
        });


        // Inflate the layout for this fragment
        return root;
    }

    //Alarma-Notificacion
    private static void setAlarm(int i, Long timestamp, Context ctx) {

        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }

}