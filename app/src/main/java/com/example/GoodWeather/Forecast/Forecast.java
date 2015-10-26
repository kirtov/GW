package com.example.GoodWeather.Forecast;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by 1 on 06.08.2015.
 */
public class Forecast implements Serializable {
    public Calendar date;
    public String conditionName;
    public String imageCode;
    public double windSpeed;
    public int pressure;
    public int humidity;
    public String cloudsName;
    public int cloudsPer;
    public int tempr;
    public boolean notNull = false;

    public Forecast() {

    }

    public Forecast(Calendar calen, String... info) {
        notNull = true;
        date = (Calendar)calen.clone();
        conditionName = info[0];
        imageCode = info[1];
        windSpeed = Double.parseDouble(info[2]);
        pressure = (int)(Double.parseDouble(info[3]) * 0.750064);
        humidity = (int)(Double.parseDouble(info[4]));
        cloudsName = info[5];
        cloudsPer = (int)(Double.parseDouble(info[6]));
        double t = Double.parseDouble(info[7]);
        tempr = (int)(t + 0.5);
    }

    public void setNewInfo(Calendar calen, String... info) {
        notNull = true;
        date = (Calendar) calen.clone();
        conditionName = info[0];
        imageCode = info[1];
        windSpeed = Double.parseDouble(info[2]);
        pressure = (int)(Double.parseDouble(info[3]) * 0.750064);
        humidity = (int)(Double.parseDouble(info[4]));
        cloudsName = info[5];
        cloudsPer = (int)(Double.parseDouble(info[6]));
        double t = Double.parseDouble(info[7]);
        tempr = (int)(t + 0.5);
    }

}
