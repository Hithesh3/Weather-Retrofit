package com.example.weatherretrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    final String TAG = "debug";
    final String API_KEY = "fd55820afa2417ce0280a467c49f8dd0";
    final String URL = "https://api.openweathermap.org/data/2.5/";
    TextView tvTemperature, tvCity, tvType, tvTemp;
    EditText etCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: ");

        tvTemperature = (TextView) findViewById(R.id.tvTemperature);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvType = (TextView) findViewById(R.id.tvType);
        tvTemp = (TextView) findViewById(R.id.tvTemp);
        etCity = (EditText) findViewById(R.id.etCity);
    }

    public void searchCity(View view) {

        String city = etCity.getText().toString().trim();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        Call<WeatherModel> call = weatherApi.getWeatherModel(city, API_KEY);

        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: failure");
                    Toast.makeText(MainActivity.this, "Enter valid City", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, "onResponse: succcess");
                WeatherModel list = response.body();
                tvCity.setText(list.getName());
                tvTemperature.setText((list.getMain().getTemp().intValue() - 273) + "°C");
                tvTemp.setText("Feels like - " + (list.getMain().getFeelsLike().intValue() - 273) + "°C" +
                        "\n\n" + "Min Temperature - " + (list.getMain().getTempMin().intValue() - 273) + "°C" +
                        "\n\n" + "Max Temperature - " + (list.getMain().getTempMax().intValue() - 273) + "°C");

                List<Weather> list1 = list.getWeather();
                for (Weather weather : list1) {
                    tvType.setText(weather.getMain());
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
                Toast.makeText(MainActivity.this, "failed inside failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}