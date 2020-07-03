package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText inputAddressText;
    Button submitAddressBtn, moreDateBtn;
    TextView addressCityText, addressCountryText,
            dateText, temperatureText, statusText, humidityText, windText, cloudText;
    ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locateItems();

    }

    private void locateItems() {
        inputAddressText = (EditText)findViewById(R.id.inputAddressText);
        submitAddressBtn = (Button)findViewById(R.id.submitAddressBtn);
        moreDateBtn = (Button)findViewById(R.id.moreDateBtn);
        addressCityText = (TextView)findViewById(R.id.addressCityText);
        addressCountryText = (TextView)findViewById(R.id.addressCountryText);
        dateText = (TextView)findViewById(R.id.dateText);
        temperatureText = (TextView)findViewById(R.id.temperatureText);
        statusText = (TextView)findViewById(R.id.statusText);
        humidityText = (TextView)findViewById(R.id.humidityText);
        windText = (TextView)findViewById(R.id.windText);
        cloudText = (TextView)findViewById(R.id.cloudText);
        weatherIcon = (ImageView)findViewById(R.id.weatherIcon);
    }

    public void getCurrentWeatherData(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + data + "&appid=9579ee9c6316f5e11933af440464ac98&units=metric&lang=vi";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("address", data);
//                        Log.d("result", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String dateInfo = jsonObject.getString("dt");
                            long l = Long.valueOf(dateInfo);
                            Date dateParse = new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");
                            String date = simpleDateFormat.format(dateParse);
                            dateText.setText(date);

                            String nameInfo = jsonObject.getString("name");
                            addressCityText.setText(nameInfo);

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);

                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.with(MainActivity.this).load("" + icon);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error + "");
                    }
                });
        requestQueue.add(stringRequest);
    }


    public void submitAddress(View view) {
        String city = null;
        city = inputAddressText.getText().toString();
        getCurrentWeatherData(city);
    }
}
