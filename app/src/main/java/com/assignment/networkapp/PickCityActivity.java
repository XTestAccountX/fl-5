package com.assignment.networkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.assignment.networkapp.entity.City;

import java.util.ArrayList;

public class PickCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_city);

        ListView listCities = findViewById(R.id.listCities);
        ArrayAdapter<String> adap = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getCities());
        listCities.setAdapter(adap);

        listCities.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                // по позиции получаем выбранный элемент
                String selectedItem = getCities()[position];

                ArrayList<City> cities = getList();
                for (int i = 0; i < cities.size(); i++) {
                    if (cities.get(i).name == selectedItem){
                        goToMainActivity(cities.get(i));
                    }
                }
            }
        });
    }

    public ArrayList<City> getList(){
        ArrayList<City> cities = new ArrayList<>();

        cities.add(new City("Ханты-Мансийск", 61.015536,69.059082));
        cities.add(new City("Серов", 59.6033400,60.5787000));
        cities.add(new City("Москва", 55.7522200,37.6155600));
        cities.add(new City("Югорск", 61.3122600,63.3306700));
        cities.add(new City("Тюмень", 57.1613,65.52502));
        cities.add(new City("Екат", 56.50,60.35));

        return cities;
    }

    public String[] getCities(){
        return new String[]{"Ханты-Мансийск","Серов","Москва","Югорск","Тюмень","Екат"};
    }

    public void goToMainActivity(City c){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", c.name);
        intent.putExtra("lat", c.lat);
        intent.putExtra("lon", c.lon);
        startActivity(intent);
    }
}