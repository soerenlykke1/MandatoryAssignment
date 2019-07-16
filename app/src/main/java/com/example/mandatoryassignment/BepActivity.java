package com.example.mandatoryassignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import java.util.Calendar;
import java.util.Locale;

public class BepActivity extends AppCompatActivity {
    public static final String DATA = "DATA";
    private DataObject dataObject;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bep);



        Intent intent = getIntent();
        dataObject = (DataObject) intent.getSerializableExtra(DATA);

        TextView userId = findViewById(R.id.DataActivityUserIdTextView);
        userId.setText("User: " + dataObject.getUserId());

        TextView deviceId = findViewById(R.id.DataActivityDeviceIdTextView);
        deviceId.setText("DeviceId: " + dataObject.getDeviceId());

        TextView pm25 = findViewById(R.id.DataActivityPm25TextView);
        pm25.setText("Pm25 = " + dataObject.getPm25());

        TextView pm10 = findViewById(R.id.DataActivityPm10TextView);
        pm10.setText("Pm10 = " + dataObject.getPm10());

        TextView co2 = findViewById(R.id.DataActivityCo2TextView);
        co2.setText("Co2 = " + dataObject.getCo2());

        TextView o3 = findViewById(R.id.DataActivityO3TextView);
        o3.setText("O3 = " + dataObject.getO3());

        TextView pressure = findViewById(R.id.DataActivityPressureTextView);
        pressure.setText("Pressure = " + dataObject.getPressure());

        TextView temperature = findViewById(R.id.DataActivityTemperatureTextView);
        temperature.setText("Temperature = " + dataObject.getTemperature());

        TextView humidity = findViewById(R.id.DataActivityHumidityTextView);
        humidity.setText("Humidity: " + dataObject.getHumidity());

        TextView latitude = findViewById(R.id.DataActivityLatitudeTextView);
        latitude.setText("Latitude = " + dataObject.getLatitude());

        TextView longitude = findViewById(R.id.DataActivityLongitudeTextView);
        longitude.setText("Longitude = " + dataObject.getLongitude());

        TextView noise = findViewById(R.id.DataActivityNoiseTextView);
        noise.setText("Noise = " + dataObject.getNoise());

        long time = dataObject.getUtc();
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();

        TextView utc = findViewById(R.id.DataActivityDateTextView);
        utc.setText("Date: " + date);

        Toolbar toolbar = findViewById(R.id.BepToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(date);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    public void back(View view) {
        finish();
    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;
    }

}