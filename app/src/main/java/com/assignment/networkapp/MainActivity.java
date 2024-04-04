package com.assignment.networkapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.networkapp.entity.NetworkCallback;
import com.assignment.networkapp.entity.NetworkTask;
import com.assignment.networkapp.entity.Weather5Days;
import com.assignment.networkapp.utils.NetworkUtils;
import com.assignment.networkapp.utils.WeatherAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// lat=50.450001&lon=30.523333

public class MainActivity extends AppCompatActivity implements NetworkCallback {

    private EditText editLatitude;
    private EditText editLongitude;
    private Button btnGetForecast;
    public Switch sw;
    private TextView textWeatherForecast;
    private TextView textCity;
    private RecyclerView layout;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPerm();
        editLatitude = findViewById(R.id.edit_latitude);
        editLongitude = findViewById(R.id.edit_longitude);
        btnGetForecast = findViewById(R.id.btn_get_forecast);
        textWeatherForecast = findViewById(R.id.text_weather_forecast);
        textCity = findViewById(R.id.textCity);
        sw = findViewById(R.id.swForecast);
        layout = findViewById(R.id.rvWeather);

        getPickCity();

        btnGetForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCoords();
                //Делать прогноз или нужная только текущая погода?
                boolean forecast = sw.isChecked();

                if (NetworkUtils.isNetworkAvailable(MainActivity.this)) {
                    NetworkTask networkTask = new NetworkTask(MainActivity.this,
                            Double.parseDouble(editLatitude.getText().toString().replace(',', '.')),
                            Double.parseDouble(editLongitude.getText().toString().replace(',', '.')),
                            !forecast);
                    networkTask.execute();
                } else {
                    // Интернета нет - погоду не узнаем
                    Toast.makeText(MainActivity.this, "Подключение к интернету отсутствует...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDataLoaded(Weather5Days data) {

        changeCity(data.list.get(0).getCityName());

        WeatherAdapter wa = new WeatherAdapter(this, data);

        RecyclerView rv = findViewById(R.id.rvWeather);
        rv.setAdapter(wa);


//        String forecastText = "Погода на: " + data.list.get(0).getTime()
//                + "\nОписание: " + data.list.get(0).getDisc()
//                + "\nТемпература: " + data.list.get(0).getTemperature() + "°C"
//                + "\nВетер: " + data.list.get(0).getWind() + "М/С"
//                + "\n______________________________";
//
//        for (int i = 1; i < data.size(); i++) {
//
//            forecastText += "\nПогода на: " + data.list.get(i).getTime()
//                    + "\nОписание: " + data.list.get(i).getDisc()
//                    + "\nТемпература: " + data.list.get(i).getTemperature() + "°C"
//                    + "\nВетер: " + data.list.get(i).getWind() + "М/С"
//                    + "\n______________________________";
//        }

        //textWeatherForecast.setText(forecastText);
    }


    @Override
    public void onDataLoadFailed() {
        textWeatherForecast.setText("Failed to load weather forecast.");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Permissions granted..", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, "Please provide the permissions", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void checkCoords() {
        if (!(checkField(editLatitude) && checkField(editLongitude))) {
            Toast.makeText(MainActivity.this, "Координаты вписаны неверно... Считываю GPS...",
                    Toast.LENGTH_SHORT).show();


            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPerm();
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            double latitude = 59.6033400;
            double longitude = 60.5787000;
            if (location == null){
                Toast.makeText(MainActivity.this,
                            "Последнее местоположение не найдено! Использован город Серов",
                                Toast.LENGTH_SHORT).show();
                editLatitude.setText(String.format("%f",latitude));
                editLongitude.setText(String.format("%f",longitude));
            }else {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                editLatitude.setText(String.format("%f",latitude));
                editLongitude.setText(String.format("%f",longitude));
            }

        }
    }

    public boolean checkField(TextView tv){
        Pattern p = Pattern.compile("^(-?\\d+(\\.\\d+)?),\\s*(-?\\d+(\\.\\d+)?)$");
        Matcher m = p.matcher(tv.getText().toString());

        boolean b = m.matches();

        return b;
    }

    public void setCoords(String lat, String lon){
        editLatitude.setText(lat);
        editLongitude.setText(lon);
    }

    public void launchPickCity(View view){
        Intent i = new Intent(this, PickCityActivity.class);
        startActivity(i);
    }

    public void getPickCity(){
        try {
            Bundle arguments = getIntent().getExtras();
            if (!arguments.isEmpty()){
                String name = arguments.get("name").toString();
                String lat = arguments.get("lat").toString().replace('.',',');
                String lon = arguments.get("lon").toString().replace('.',',');

                setCoords(lat, lon);

                changeCity(name);
            }
        }
        catch (Exception ex){

        }
    }

    public void changeCity(String name){
        textCity.setText("Ваш город: "+ name);
    }

    public void requestPerm(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        while (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
    }
}
