package com.example.GoodWeather.Forecast;

import java.io.Serializable;
import java.util.ArrayList;

public class DayForecast implements Serializable {
    public ArrayList<Forecast> forecasts;

    public DayForecast() {
        forecasts = new ArrayList<Forecast>();
    }

    public void add(Forecast forc) {
        forecasts.add(forc);
    }
}
