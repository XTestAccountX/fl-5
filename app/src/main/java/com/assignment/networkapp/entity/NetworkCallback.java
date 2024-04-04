package com.assignment.networkapp.entity;

import com.assignment.networkapp.entity.WeatherForecast;

public interface NetworkCallback {
    void onDataLoaded(Weather5Days data);

    void onDataLoadFailed();
}

