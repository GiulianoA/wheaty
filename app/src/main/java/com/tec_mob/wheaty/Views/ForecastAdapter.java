package com.tec_mob.wheaty.Views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.tec_mob.wheaty.R;
import com.tec_mob.wheaty.db.WheatyDataBase;
import com.tec_mob.wheaty.model.Forecast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    public static WheatyDataBase wheatyDataBase;
    private String unit;

    private List<Forecast> forecastList;

    public ForecastAdapter(List<Forecast> forecastList, Boolean unit) {
        this.forecastList = forecastList;
        this.unit = unit ? "C" : "F";
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view, null, false);

        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder forecastViewHolder, int i) {
        forecastViewHolder.animationView.setAnimation(forecastList.get(i).icon + ".json");

        try {
            Date forecastDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(forecastList.get(i).dt);

            DateFormat dateFormat = new SimpleDateFormat("EEEE d MMM");
            dateFormat.setTimeZone(TimeZone.getDefault());
            forecastViewHolder.fecha.setText(dateFormat.format(forecastDate));

            DateFormat timeFormat = new SimpleDateFormat("hh hs");
            timeFormat.setTimeZone(TimeZone.getDefault());
            forecastViewHolder.hora.setText(timeFormat.format(forecastDate));
        } catch (ParseException e) {
            forecastViewHolder.fecha.setText("");
            forecastViewHolder.hora.setText("");
        }
        forecastViewHolder.temp.setText(String.format("%s°%s", forecastList.get(i).temp, unit));
        forecastViewHolder.min.setText(String.format("%s°%s", forecastList.get(i).tempMin, unit));
        forecastViewHolder.max.setText(String.format("%s°%s", forecastList.get(i).tempMax, unit));
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public class ForecastViewHolder extends RecyclerView.ViewHolder {

        LottieAnimationView animationView;
        TextView fecha;
        TextView hora;
        TextView temp;
        TextView min;
        TextView max;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);

            animationView = (LottieAnimationView) itemView.findViewById(R.id.animation_view);
            fecha = itemView.findViewById(R.id.fecha);
            hora = itemView.findViewById(R.id.hora);
            temp = itemView.findViewById(R.id.temperatura);
            min = itemView.findViewById(R.id.min);
            max = itemView.findViewById(R.id.max);
        }
    }
}
