package com.tec_mob.wheaty.Notification;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;

import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;

import com.tec_mob.wheaty.R;
import com.tec_mob.wheaty.Views.MainActivity;
import com.tec_mob.wheaty.db.WheatyDataBase;
import com.tec_mob.wheaty.model.User;
import com.tec_mob.wheaty.model.Weather;
import com.tec_mob.wheaty.network.DTO.WeatherDTO;
import com.tec_mob.wheaty.network.WeatherService;

import java.util.Calendar;
import java.util.List;

public class NotificationService extends IntentService {

    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;
    private static int NOTIFICATION_ID = 1;
    Notification notification;

    Boolean unit = true;

    Boolean notifications = false;

    public static WheatyDataBase wheatyDataBase;

    public NotificationService(String name) {
        super(name);
    }

    public NotificationService() {
        super("SERVICE");
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(Intent intent2) {
        String NOTIFICATION_CHANNEL_ID = getApplicationContext().getString(R.string.app_name);
        Context context = this.getApplicationContext();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this, MainActivity.class);
        Resources res = this.getResources();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        wheatyDataBase = Room.databaseBuilder(context, WheatyDataBase.class, "userdb").allowMainThreadQueries().build();

        // Unit
        List<User> user = wheatyDataBase.userDAO().getCurrentUser();
        if (user.size() > 0) {
            unit = user.get(0).getUnidades();
            notifications = user.get(0).getNotificaciones();
        }

        if(isNetworkConnected(context)) {
            WeatherService weatherService = new WeatherService();
            weatherService.changeUnits(unit);
            // save weatherDTO to DB
            WeatherDTO weatherDTO = weatherService.getData();
            wheatyDataBase.weatherDAO().deleteAll();
            Weather weather = new  Weather(weatherDTO.main.temp, weatherDTO.main.tempMin, weatherDTO.main.tempMax, weatherDTO.weather.get(0).icon, weatherDTO.dt, weatherDTO.name, weatherDTO.weather.get(0).main, weatherDTO.wind.speed);
            wheatyDataBase.weatherDAO().addWeather(weather);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Error de conexion")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setTitle("Ops!")
                    .create();
            builder.show();
        }

        String message = "";

        // read BD for data
        List<Weather> weatherList = wheatyDataBase.weatherDAO().getWeather();
        if(weatherList.size() > 0) {
            message = String.format("La temperatura para hoy es de %s°%s", weatherList.get(0).getTemp(), unit?"C":"F");
        }

        if(!message.isEmpty() && notifications) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                final int NOTIFY_ID = 0; // ID of notification
                String id = NOTIFICATION_CHANNEL_ID; // default_channel_id
                String title = NOTIFICATION_CHANNEL_ID; // Default Channel
                PendingIntent pendingIntent;
                NotificationCompat.Builder builder;
                NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (notifManager == null) {
                    notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                }
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = notifManager.getNotificationChannel(id);
                if (mChannel == null) {
                    mChannel = new NotificationChannel(id, title, importance);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notifManager.createNotificationChannel(mChannel);
                }
                builder = new NotificationCompat.Builder(context, id);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentTitle(getString(R.string.app_name)).setCategory(Notification.CATEGORY_SERVICE)
                        .setSmallIcon(R.drawable.wheaty_logo2)   // required
                        .setContentText(message)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.wheaty_logo2))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setSound(soundUri)

                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                Notification notification = builder.build();
                notifManager.notify(NOTIFY_ID, notification);

                startForeground(1, notification);

            } else {
                pendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification = new NotificationCompat.Builder(this)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.wheaty_logo2)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.wheaty_logo2))
                        .setSound(soundUri)
                        .setAutoCancel(true)
                        .setContentTitle(getString(R.string.app_name)).setCategory(Notification.CATEGORY_SERVICE)
                        .setContentText(message).build();
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
        }
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}