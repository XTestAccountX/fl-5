package com.assignment.networkapp.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeatherForecast {
    private final String cityName;
    private final double temperature;
    private final double wind;
    private final long time;
    private final String disc;
    private final String icon;

    public WeatherForecast(String cityName, double temperature, double wind, long time, String disc, String icon) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.wind = wind;
        this.time = time;
        this.disc = disc;
        this.icon = icon;
    }

    public String getCityName() {
        return cityName;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWind() {
        return wind;
    }
    public String getTime() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm");
        sdf.format(d);

        return sdf.format(d);
    }
    public String getDisc() {
        return disc;
    }

    public String getIcon(){
        return "https://openweathermap.org/img/wn/" + icon + "@2x.png";
    }
}
