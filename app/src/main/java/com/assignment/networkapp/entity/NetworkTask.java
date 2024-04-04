package com.assignment.networkapp.entity;

import android.os.AsyncTask;
import android.util.Log;

import com.assignment.networkapp.service.WeatherService;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

public class NetworkTask extends AsyncTask<Void, Void, Weather5Days> {
    private static final String TAG = NetworkTask.class.getSimpleName();

    private final WeatherService weatherService;
    private final NetworkCallback callback;
    private boolean flagForecast = false;
    private final double latitude;
    private final double longitude;

    public NetworkTask(NetworkCallback callback, double latitude, double longitude, boolean forecast) {
        weatherService = new WeatherService();
        this.callback = callback;
        this.latitude = latitude;
        this.longitude = longitude;
        this.flagForecast = forecast;
    }

    @Override
    protected Weather5Days doInBackground(Void... voids) {

        try {

            Response response = weatherService.fetchWeather(latitude, longitude, flagForecast);

            if (response.isSuccessful()) {
                String responseData = response.body().string();
                if (flagForecast){

                    Weather5Days w5 = weatherService.parseWeather5Days(responseData);
                    return w5;
                }else {
                    WeatherForecast curr = weatherService.parseWeatherCurr(responseData);
                    Weather5Days w5 = new Weather5Days();
                    w5.AddNew(curr);
                    return w5;
                }

            } else {
                Log.e(TAG, "Unsuccessful response: " + response.code());
            }
        } catch (IOException e) {
            Log.e(TAG, "Network failure: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Weather5Days forecast) {
        if (forecast.size() == 1) {
            callback.onDataLoaded(forecast);
        } else if (forecast.size() > 1) {
            callback.onDataLoaded(forecast);
            //Много данных
        }else{
            callback.onDataLoadFailed();
        }
    }



}
