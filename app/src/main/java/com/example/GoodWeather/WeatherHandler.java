package com.example.GoodWeather;

import com.example.GoodWeather.Forecast.DayForecast;
import com.example.GoodWeather.Forecast.Forecast;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class WeatherHandler extends DefaultHandler {
    private final String SYMBOL = "symbol";
    private final String WINDDIR = "windDirection";
    private final String WINDSPEED = "windSpeed";
    private final String HUMIDITY = "humidity";
    private final String PRESSURE = "pressure";
    private final String TEMPERATURE = "temperature";
    private final String CLOUDS = "clouds";
    private final String TIME = "time";
    StringBuilder sb = null;

    ArrayList<DayForecast> dfs;

    WeatherHandler(ArrayList<DayForecast> dfs) {
        super();
        this.dfs = dfs;
        sb = new StringBuilder();
    }

    String tempr, cond, imgCode, mps, hum, pres, clouds, cloudsVal, tm;
    Calendar curcal;
    Date curDay = null, time2;


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
        super.startElement(uri, localName, qName, attr);
        if (qName.equals(TIME)) {
            tm = attr.getValue(0);
            tm = tm.replace("T", " ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar cal = null;
            try {
                time2 = sdf.parse(tm);
                cal = Calendar.getInstance();
                cal.setTime(time2);
                TimeZone tz = cal.getTimeZone();
                cal.add(Calendar.MILLISECOND, tz.getRawOffset());
                time2 = cal.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (curDay == null) {
                dfs.add(new DayForecast());
                curDay = (Date)time2.clone();
            } else if (nextDay(time2)) {
                addNewForecast();
                dfs.add(new DayForecast());
                curDay = (Date)time2.clone();
            } else {
                addNewForecast();
            }
            curcal = (Calendar)cal.clone();
        } else if (qName.equals(SYMBOL)) {
            cond = attr.getValue(1);
            imgCode = attr.getValue(2);
        } else if (qName.equals(WINDSPEED)) {
            mps = attr.getValue(0);
        } else if (qName.equals(TEMPERATURE)) {
            tempr = attr.getValue(1);
        } else if (qName.equals(PRESSURE)) {
            pres = attr.getValue(1);
        } else if (qName.equals(HUMIDITY)) {
            hum = attr.getValue(0);
        } else if (qName.equals(CLOUDS)) {
            clouds = attr.getValue(0);
            cloudsVal = attr.getValue(1);
        }
    }

    private Date getDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-DD-MM HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private boolean nextDay(Date date) {
        return (date.getYear() > curDay.getYear() || date.getMonth() > curDay.getMonth() || date.getDate() > curDay.getDate());
    }

    private void addNewForecast() {
        Forecast forecast = new Forecast(curcal, cond, imgCode, mps, pres, hum, clouds, cloudsVal, tempr);
        dfs.get(dfs.size() - 1).add(forecast);
    }

}

