package com.example.openweather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText cityNameEt;
    TextView temperatureTv;
    Button getWeatherBtn;
    String url = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
    String apiKey = "a89d19a80f5acc227a735f6807aad693";
    String baseUrl = "https://api.openweathermap.org/data/2.5/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityNameEt = findViewById(R.id.et);
        temperatureTv = findViewById(R.id.tv);

        getWeatherBtn = findViewById(R.id.get_weather_btn);

        getWeatherBtn.setOnClickListener(v -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            WeatherApi weatherApi = retrofit.create(WeatherApi.class);
            Call<Example> exampleCall = weatherApi.getWeather(cityNameEt.getText().toString().trim(), apiKey);
            exampleCall.enqueue(new Callback<Example>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NonNull Call<Example> call, @NonNull Response<Example> response) {
                    if (response.code() == 404) {
                        Toast.makeText(MainActivity.this, "Please Enter a valid City", Toast.LENGTH_SHORT).show();
                    } else if (!(response.isSuccessful())) {
                        Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    }else {
                        try {
                            Example myData = response.body();
                            assert myData != null;
                            Main main = myData.getMain();
                            Double temp = main.getTemp();
                            Integer temperature = (int) (temp - 273.15);
                            temperatureTv.setText(temperature + " C");
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Example> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });

    }
}