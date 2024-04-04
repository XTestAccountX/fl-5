package com.assignment.networkapp.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Weather5Days {

    public ArrayList<WeatherForecast> list = new ArrayList<>();

    public Weather5Days() {

    }

    public void AddNew(WeatherForecast wf){
        list.add(wf);
    }

    public int size(){
        return list.size();
    }

}
