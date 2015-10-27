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

    public int getMediumTemprature() {
        int size = forecasts.size();
        int t1 = size / 2;
        int t2 = Math.min(t1 + 1, size);
        int tempr = (forecasts.get(t1).tempr + forecasts.get(t2).tempr)/2;
        return tempr;
    }

    public String getMediumImageCode() {
        return forecasts.get(forecasts.size()/2).imageCode;
    }
}
