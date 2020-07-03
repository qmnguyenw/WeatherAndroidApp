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
            dateText, temperatureText, statusText, humidityText, windText, cloudText, realTempText;
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
        realTempText = (TextView)findViewById(R.id.realTempText);
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
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MMM-dd HH:mm:ss");
                            String date = simpleDateFormat.format(dateParse);
                            dateText.setText(date);

                            String city = jsonObject.getString("name");
//                            addressCityText.setText(city);

                            // 1st way to get data lower-level json
                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);

                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/wn/" + icon + "@2x.png").into(weatherIcon);

                            String status = jsonObjectWeather.getString("description"); // "main" if want show shorter status
                            // TODO: uppercase the first character in each word
                            statusText.setText(status);

                            // 2nd way to get data lower-level json
                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");

                            String temp = jsonObjectMain.getString("temp");
                            Double a = Double.valueOf(temp);
                            String temperature = String.valueOf(a.intValue());
                            temperatureText.setText(temperature + "°C");

                            String tempLike = jsonObjectMain.getString("feels_like");
                            Double b = Double.valueOf(tempLike);
                            String feelsLike = String.valueOf(b.intValue());
                            realTempText.setText("Feels Like " + feelsLike + "°C");

                            String humidity = jsonObjectMain.getString("humidity");
                            humidityText.setText(humidity + "%");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String wind = jsonObjectWind.getString("speed");
                            windText.setText(wind + " m/s");

                            JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                            String cloud = jsonObjectCloud.getString("all");
                            cloudText.setText(cloud + "%");

                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");
//                            addressCountryText.setText(country);
                            addressCityText.setText(city + ", " + country);
                            addressCountryText.setText("");


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
