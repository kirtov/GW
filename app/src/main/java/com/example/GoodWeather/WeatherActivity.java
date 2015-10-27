package com.example.GoodWeather;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.GoodWeather.Forecast.DayForecast;
import com.example.GoodWeather.Forecast.Forecast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WeatherActivity extends Activity {
    private final String APPID = "e5e545b5cf7e46134a1646fe5233aec8";
    private final String RESPONSE = "weatherResponse";
    private final String RESPONSE2 = "nowWeatherResponse";
    private final String URL = "url";
    private String[] enConditions, ruConditions;
    TextView  time;
    private String w1;
    DBAdapter db;
    String city;
    DBCodes dbc;

    ArrayList<DayForecast> currentDfs;
    Date currentTime;
    int currentViewDay;

    ListView weatherView;
    String[] from = new String[]{"time","temperature","image"};
    int[] to = new int[]{R.id.time_text, R.id.temp_text, R.id.weather_image};

    ImageView curImage;
    TextView curTemp, cityName, condition;
    Typeface type1;

    //widget
    TextView l1_day, l2_day, l3_day, l4_day;
    TextView l1_temp, l2_temp, l3_temp, l4_temp;
    ImageView l1_img, l2_img, l3_img, l4_img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        type1 =  Typeface.createFromAsset(getAssets(), "fonts/2.ttf");
        initializeComponents();
        currentTime = new Date();
        currentViewDay = 0;
        db = new DBAdapter(this);
        time = (TextView) findViewById(R.id.curTime);
        dbc = new DBCodes(this);
        weatherView = (ListView) findViewById(R.id.listView2);
        w1 = getResources().getString(R.string.wurl1);
        city = getIntent().getStringExtra(DBAdapter.RUCITYNAME);
        cityName.setText(city);
        ruConditions = getResources().getStringArray(R.array.ruConditions);
        enConditions = getResources().getStringArray(R.array.enConditions);
        register();
        currentDfs = null;
        getWeather();
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.sbis_blue));
    }

    private void setTypeFace() {
        curTemp.setTypeface(type1);
        l1_day.setTypeface(type1);
        l2_day.setTypeface(type1);
        l3_day.setTypeface(type1);
        l4_day.setTypeface(type1);
        l1_temp.setTypeface(type1);
        l2_temp.setTypeface(type1);
        l3_temp.setTypeface(type1);
        l4_temp.setTypeface(type1);
        cityName.setTypeface(type1);
        condition.setTypeface(type1);
    }

    private void initializeComponents() {
        DisplayMetrics dm;
        dm = getResources().getDisplayMetrics();
        RelativeLayout l1 = (RelativeLayout) findViewById(R.id.l1);
        RelativeLayout l2 = (RelativeLayout) findViewById(R.id.l2);
        RelativeLayout l3 = (RelativeLayout) findViewById(R.id.l3);
        RelativeLayout l4 = (RelativeLayout) findViewById(R.id.l4);
        int wid = (dm.widthPixels);
        wid = wid / 4;
        l1.getLayoutParams().width = wid;
        l2.getLayoutParams().width = wid;
        l3.getLayoutParams().width = wid;
        l4.getLayoutParams().width = wid;
        l1.getLayoutParams().height = wid;
        l2.getLayoutParams().height = wid;
        l3.getLayoutParams().height = wid;
        l4.getLayoutParams().height = wid;
        curImage = (ImageView) findViewById(R.id.curImage);
        curTemp = (TextView) findViewById(R.id.tempr);
        cityName = (TextView) findViewById(R.id.cityName);
        condition = (TextView) findViewById(R.id.condition);
        //widget
        l1_day = (TextView)findViewById(R.id.l1_t1);
        l2_day = (TextView)findViewById(R.id.l2_t1);
        l3_day = (TextView)findViewById(R.id.l3_t1);
        l4_day = (TextView)findViewById(R.id.l4_t1);
        l1_temp = (TextView)findViewById(R.id.l1_t2);
        l2_temp = (TextView)findViewById(R.id.l2_t2);
        l3_temp = (TextView)findViewById(R.id.l3_t2);
        l4_temp = (TextView)findViewById(R.id.l4_t2);
        l1_img = (ImageView)findViewById(R.id.l1_i1);
        l2_img = (ImageView)findViewById(R.id.l2_i1);
        l3_img = (ImageView)findViewById(R.id.l3_i1);
        l4_img = (ImageView)findViewById(R.id.l4_i1);
        //setTypeFace();
    }

    private void setTime() {
        Calendar date;
        try {
            date = currentDfs.get(currentViewDay).forecasts.get(0).date;
            time.setText(getWeekDay(date.get(Calendar.DAY_OF_WEEK)));
            if (currentViewDay == 0) {
                time.setText(time.getText() + " Сегодня");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private String getMonthName(int n) {
        switch (n) {
            case 0:
                return "Янв";
            case 1:
                return "Фев";
            case 2:
                return "Мар";
            case 3:
                return "Апр";
            case 4:
                return "Мая";
            case 5:
                return "Июн";
            case 6:
                return "Июл";
            case 7:
                return "Авг";
            case 8:
                return "Сен";
            case 9:
                return "Окт";
            case 10:
                return "Ноя";
            case 11:
                return "Дек";
        }
        return "Месяц";
    }

    private String getWeekDay(int day) {
        switch (day) {
            case 1:
                return "Пн";
            case 2:
                return "Вт";
            case 3:
                return "Ср";
            case 4:
                return "Чт";
            case 5:
                return "Пт";
            case 6:
                return "Сб";
            case 7:
                return "Вс";
        }
        return "День";
    }

    private void getWeather() {
        getCurWeather();
        Intent intent = new Intent(this, WeatherService.class);
        intent.putExtra(URL, w1 + city + "&mode=xml&APPID=" + APPID);
        startService(intent);
    }

    private void getCurWeather() {
        Intent intent = new Intent(this, NowWeatherService.class);
        intent.putExtra(URL, getResources().getString(R.string.curWurl) + city + "&mode=xml&APPID=" + APPID);
        startService(intent);
    }

    private void register() {
        WeatherReceiver wet = new WeatherReceiver();
        IntentFilter filter = new IntentFilter(RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(wet, filter);
        WeatherReceiver wet2 = new WeatherReceiver();
        IntentFilter filter2 = new IntentFilter(RESPONSE2);
        filter2.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(wet2, filter2);
    }

    private void changeConditionToRu() {
        String cond = condition.getText().toString();
        int index = -1;
        for (int i = 0; i < enConditions.length; i++) {
            if (cond.equals(enConditions[i])) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            condition.setText("");
        } else {
            condition.setText(ruConditions[index]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            getWeather();
        }
        if (item.getItemId() == R.id.add) {
            startActivity(new Intent(this, CityActivity.class));
            this.finish();
        }
        return true;
    }

    private void setNewWeatherInfo(ArrayList<DayForecast> dfsArr) {
        currentDfs = dfsArr;
        setTime();
        ArrayList<Forecast> curForecasts = currentDfs.get(0).forecasts;
        try {
            curForecasts = currentDfs.get(currentViewDay).forecasts;
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> m;
        for (int i = 0; i < curForecasts.size(); i++) {
            m = new HashMap<String, Object>();
            m.put("time",curForecasts.get(i).date.get(Calendar.HOUR_OF_DAY) + ":00");
            m.put("temperature",curForecasts.get(i).tempr);
            m.put("image", getImageByCode(curForecasts.get(i).imageCode));
            data.add(m);
        }

        SimpleAdapter sAdapter = new SimpleAdapter(this, data, R.layout.weather_iten,
                from, to);

        weatherView.setAdapter(sAdapter);
    }

    private void setWidgetInfo(ArrayList<DayForecast> dfsArr) {
        int temp, curDay;
        if (dfsArr.size() > 1) {
            curDay = 1;
            l1_day.setText(getWeekDay(dfsArr.get(curDay).forecasts.get(0).date.get(Calendar.DAY_OF_WEEK)));
            temp = dfsArr.get(curDay).getMediumTemprature();
            l1_temp.setText("" + temp);
            l1_img.setImageResource(getImageByCode(dfsArr.get(curDay).getMediumImageCode()));
        }
        if (dfsArr.size() > 2) {
            curDay = 2;
            l2_day.setText(getWeekDay(dfsArr.get(curDay).forecasts.get(0).date.get(Calendar.DAY_OF_WEEK)));
            temp = dfsArr.get(curDay).getMediumTemprature();
            l2_temp.setText("" + temp);
            l2_img.setImageResource(getImageByCode(dfsArr.get(curDay).getMediumImageCode()));
        }
        if (dfsArr.size() > 3) {
            curDay = 3;
            l3_day.setText(getWeekDay(dfsArr.get(curDay).forecasts.get(0).date.get(Calendar.DAY_OF_WEEK)));
            temp = dfsArr.get(curDay).getMediumTemprature();
            l3_temp.setText("" + temp);
            l3_img.setImageResource(getImageByCode(dfsArr.get(curDay).getMediumImageCode()));
        }
        if (dfsArr.size() > 4) {
            curDay = 4;
            l4_day.setText(getWeekDay(dfsArr.get(curDay).forecasts.get(0).date.get(Calendar.DAY_OF_WEEK)));
            temp = dfsArr.get(curDay).getMediumTemprature();
            l4_temp.setText("" + temp);
            l4_img.setImageResource(getImageByCode(dfsArr.get(curDay).getMediumImageCode()));
        }
    }

    private void setNewNowWeather(Forecast forecast) {
        int temp = forecast.tempr - 273;
        if (forecast.tempr > 0) {
            curTemp.setText("+" + temp + "°");
        } else {
            curTemp.setText(temp + "°");
        }
        curImage.setImageResource(getImageByCode(forecast.imageCode));
        condition.setText(forecast.conditionName);
        changeConditionToRu();
    }

    private int getImageByCode(String code) {
        if (code.equals("01d")) {
            return R.drawable.a25;
        } else if (code.equals("01n")) {
            return R.drawable.a24;
        } else if (code.equals("02d")) {
            return R.drawable.a23;
        } else if (code.equals("02n")) {
            return R.drawable.a22;
        } else if (code.equals("03d")) {
            return R.drawable.a19;
        } else if (code.equals("03n")) {
            return R.drawable.a19;
        } else if (code.equals("04d")) {
            return R.drawable.a21;
        } else if (code.equals("04n")) {
            return R.drawable.a20;
        } else if (code.equals("09d")) {
            return R.drawable.a8;
        } else if (code.equals("09n")) {
            return R.drawable.a8;
        } else if (code.equals("10d")) {
            return R.drawable.a28;
        } else if (code.equals("10n")) {
            return R.drawable.a31;
        } else if (code.equals("11d")) {
            return R.drawable.a27;
        } else if (code.equals("11n")) {
            return R.drawable.a33;
        } else if (code.equals("13d")) {
            return R.drawable.a29;
        } else if (code.equals("13n")) {
            return R.drawable.a32;
        } else if (code.equals("50d")) {
            return R.drawable.a14;
        } else if (code.equals("50n")) {
            return R.drawable.a16;
        }
        return R.drawable.a25;
    }

    public class WeatherReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RESPONSE2)) {
                Forecast nowForecast = (Forecast) intent.getSerializableExtra("weather");
                if (nowForecast != null && nowForecast.notNull) {
                    setNewNowWeather(nowForecast);
                } else {
                    Toast.makeText(WeatherActivity.this, "Погода для данного города сейчас недоступна, попробуйте позднее", Toast.LENGTH_SHORT).show();
                }
            } else {
                ArrayList<DayForecast> dfs = (ArrayList<DayForecast>) intent.getSerializableExtra("weather");
                if (dfs.size() == 0) {
                    Toast.makeText(WeatherActivity.this, "Погода для данного города сейчас недоступна, попробуйте позднее", Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    WeatherActivity.this.finish();
                }
                currentViewDay = 0;
                setNewWeatherInfo(dfs);
                setWidgetInfo(dfs);
            }
            }
    }

    public void on1Click(View v) {
        currentViewDay = 1;
        setNewWeatherInfo(currentDfs);
    }

    public void on2Click(View v) {
        currentViewDay = 2;
        setNewWeatherInfo(currentDfs);
    }

    public void on3Click(View v) {
        currentViewDay = 3;
        setNewWeatherInfo(currentDfs);
    }

    public void on4Click(View v) {
        currentViewDay = 4;
        setNewWeatherInfo(currentDfs);
    }
}
