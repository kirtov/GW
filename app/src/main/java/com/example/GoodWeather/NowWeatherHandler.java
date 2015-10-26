package com.example.GoodWeather;

import com.example.GoodWeather.Forecast.Forecast;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Calendar;

public class NowWeatherHandler extends DefaultHandler {
    private final String WEATHER = "weather";
    private final String WINDDIR = "direction";
    private final String LASTUPD = "lastupdate";
    private final String WINDSPEED = "speed";
    private final String HUMIDITY = "humidity";
    private final String PRESSURE = "pressure";
    private final String TEMPERATURE = "temperature";
    private final String CLOUDS = "clouds";
    private final String RESPONSE = "nowWeatherResponse";
    StringBuilder sb = null;
    Forecast forecast;

    NowWeatherHandler(Forecast forecast) {
        super();
        this.forecast = forecast;
        sb = new StringBuilder();
    }

    String tempr, cond, imgCode, mps, hum, pres, clouds, cloudsVal;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
        super.startElement(uri, localName, qName, attr);
        if (qName.equals(WEATHER)) {
            cond = attr.getValue(1);
            imgCode = attr.getValue(2);
        } else if (qName.equals(WINDSPEED)) {
            mps = attr.getValue(0);
        } else if (qName.equals(TEMPERATURE)) {
            tempr = attr.getValue(0);
        } else if (qName.equals(PRESSURE)) {
            pres = attr.getValue(0);
        } else if (qName.equals(HUMIDITY)) {
            hum = attr.getValue(0);
        } else if (qName.equals(CLOUDS)) {
            clouds = attr.getValue(1);
            cloudsVal = attr.getValue(0);
        } else if (qName.equals(LASTUPD)) {
            forecast.setNewInfo(Calendar.getInstance(), cond, imgCode, mps, pres, hum, clouds, cloudsVal, tempr);
        }
    }


    }


