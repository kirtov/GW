package com.example.GoodWeather;
import android.app.IntentService;
import android.content.Intent;

import com.example.GoodWeather.Forecast.DayForecast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;
import java.io.StringReader;
import java.util.ArrayList;


public class WeatherService extends IntentService {
    private final String URL = "url";
    private final String ERROR = "Error";
    private final String RESPONSE = "weatherResponse";
    private final String WEATHER = "weather";

    String url = null;
    public WeatherService() {
        super("sax");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        url = intent.getStringExtra(URL);
        ArrayList<DayForecast> dfs = new ArrayList<DayForecast>();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser parser = factory.newSAXParser();
            HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(url));
            HttpEntity httpEntity = httpResponse.getEntity();
            String xml = EntityUtils.toString(httpEntity, "UTF-8");
            InputSource input = new InputSource(new StringReader(xml));
            parser.parse(input, new WeatherHandler(dfs));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent response = new Intent();
        response.setAction(RESPONSE);
        response.addCategory(Intent.CATEGORY_DEFAULT);
        response.putExtra(WEATHER, dfs);
        sendBroadcast(response);
    }
}

