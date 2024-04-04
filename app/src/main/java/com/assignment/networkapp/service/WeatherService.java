package com.assignment.networkapp.service;

import android.icu.util.Calendar;
import android.util.Log;

import com.assignment.networkapp.entity.Weather5Days;
import com.assignment.networkapp.entity.WeatherForecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherService {
    private static final String API_KEY = "c19f8778962481164de7d96059b267b9";
    private final OkHttpClient httpClient;

    public WeatherService() {
        httpClient = new OkHttpClient();
    }

    public Response fetchWeather(double latitude, double longitude, boolean forecast) throws IOException {
        String apiUrl = "";
        if (!forecast){
            apiUrl = buildApiUrlCurr(latitude, longitude);
        }else {
            apiUrl = buildApiUrl5Days(latitude, longitude);
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        return client.newCall(request).execute();
    }


    private String buildApiUrlCurr(double latitude, double longitude) {
        return "https://api.openweathermap.org/data/2.5/weather?" +
                "lat=" + latitude +
                "&lon=" + longitude +
                "&appid=" + API_KEY +
                "&lang=ru"+
                "&units=metric";
    }

    private String buildApiUrl5Days(double latitude, double longitude) {

        String s = "https://api.openweathermap.org/data/2.5/forecast?" +
                "lat=" + latitude +
                "&lon=" + longitude +
                "&appid=" + API_KEY +
                "&lang=ru"+
                "&units=metric";

        return s;
    }

    public WeatherForecast parseWeatherCurr(String responseData) {
        try {
            JSONObject responseJson = new JSONObject(responseData);

            String cityName = responseJson.getString("name");
            if (cityName.equals("")){
                cityName = "Не найдено";
            }

            JSONObject mainJson = responseJson.getJSONObject("main");
            double temperature = mainJson.getDouble("temp");

            JSONObject windJson = responseJson.getJSONObject("wind");
            double wind = windJson.getDouble("speed");

            long dt = responseJson.getLong("dt") * 1000;

            JSONArray weather = responseJson.getJSONArray("weather");
            JSONObject descri = weather.getJSONObject(0);
            String desc = descri.getString("description");
            String icon = descri.getString("icon");

            return new WeatherForecast(cityName, temperature, wind, dt, desc, icon);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Weather5Days parseWeather5Days(String responseData) {
        try {
            JSONObject responseJson = new JSONObject(responseData);

            JSONArray jsonArray = new JSONArray();
            JSONArray list = responseJson.getJSONArray("list");
            Weather5Days w5 = new Weather5Days();

            Long tsLong = System.currentTimeMillis();
            JSONObject city = responseJson.getJSONObject("city");
            String cityname = city.getString("name");
            if (cityname.equals("")){
                cityname = "Не найдено";
            }

            for (int i = 0; i < 40; i++) {

                JSONObject curr = list.getJSONObject(i);

                JSONObject mainJson = curr.getJSONObject("main");
                double temperature = mainJson.getDouble("temp");

                JSONObject windJson = curr.getJSONObject("wind");
                double wind = windJson.getDouble("speed");

                long dt = curr.getLong("dt") * 1000;

                JSONArray weather = curr.getJSONArray("weather");
                JSONObject descri = weather.getJSONObject(0);
                String desc = descri.getString("description");
                String icon = descri.getString("icon");

                w5.AddNew(new WeatherForecast(cityname, temperature, wind, dt, desc, icon));
            }

            return w5;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
