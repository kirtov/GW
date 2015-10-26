package com.example.GoodWeather;
import android.app.IntentService;
import android.content.Intent;

import com.example.GoodWeather.Forecast.Forecast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;
import java.io.StringReader;


public class NowWeatherService extends IntentService {
    private final String URL = "url";
    private final String RESPONSE = "nowWeatherResponse";
    private final String WEATHER = "weather";

    String url = null;
    public NowWeatherService() {
        super("sax2");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        url = intent.getStringExtra(URL);
        Forecast nowForecast = new Forecast();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            javax.xml.parsers.SAXParser parser = factory.newSAXParser();
            HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(url));
            HttpEntity httpEntity = httpResponse.getEntity();
            String xml = EntityUtils.toString(httpEntity, "UTF-8");
            InputSource input = new InputSource(new StringReader(xml));
            parser.parse(input, new NowWeatherHandler(nowForecast));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent response = new Intent();
        response.setAction(RESPONSE);
        response.addCategory(Intent.CATEGORY_DEFAULT);
        response.putExtra(WEATHER, nowForecast);
        sendBroadcast(response);
//        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 200000, 200000, pi);
    }
}

