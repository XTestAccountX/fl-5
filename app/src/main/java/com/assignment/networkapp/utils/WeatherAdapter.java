package com.assignment.networkapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.assignment.networkapp.R;
import com.assignment.networkapp.entity.Weather5Days;
import com.assignment.networkapp.entity.WeatherForecast;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final Weather5Days forecasts;

    public WeatherAdapter(Context context, Weather5Days forecasts) {
        this.forecasts = forecasts;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.weather_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherAdapter.ViewHolder holder, int position) {
        WeatherForecast forecast = forecasts.list.get(position);
        new DownloadImageTask(holder.flagView).execute(forecast.getIcon());

        holder.nameView.setText("Дата: "+forecast.getTime() + ": " + forecast.getDisc());
        holder.weatherIView.setText("Температура: "+Double.toString(forecast.getTemperature())+" °C"
        + " Ветер:" +Double.toString(forecast.getWind())+" М/С");
    }

    @Override
    public int getItemCount() {
        return forecasts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView flagView;
        final TextView nameView, weatherIView;
        ViewHolder(View view){
            super(view);
            flagView = view.findViewById(R.id.icon);
            nameView = view.findViewById(R.id.name);
            weatherIView = view.findViewById(R.id.tvWeatherInfo);
        }
    }
}
