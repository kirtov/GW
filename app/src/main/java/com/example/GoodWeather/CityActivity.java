package com.example.GoodWeather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.github.clans.fab.FloatingActionButton;

import java.util.concurrent.ExecutionException;

public class CityActivity extends Activity {
    private final String EN = "en";

    String ruWord;
    DBAdapter db;
    ListView listView;
    TextView text;
    String[] cities;
    FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city);
        listView = (ListView) findViewById(R.id.listView);
        fab = (FloatingActionButton) findViewById(R.id.add_city_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programAddCity();
            }
        });
        db = new DBAdapter(this);
        text = (TextView) findViewById(R.id.textView);
        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.sbis_blue_dark));
        getActionBar().setTitle("Выберите город");
        makeList();
    }

    private void makeList() {
        cities = db.getAllCities();
        if (cities == null) {
            cities = new String[0];
        }
        for (int i = 0; i < cities.length; i++) {
            cities[i] = cities[i];
        }

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.listitem, R.id.textView, cities);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> l, View view,
                                    int position, long id) {
                Intent intent = new Intent(CityActivity.this, WeatherActivity.class);
                intent.putExtra(DBAdapter.RUCITYNAME, cities[position]);
                startActivity(intent);
            }
        });
        listView.setDividerHeight(0);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CityActivity.this);
                builder.setTitle(getResources().getString(R.string.delete));
                final int pos = position;
                builder.setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteCity(cities[pos]);
                        makeList();
                    }
                });
                builder.setNeutralButton(getResources().getString(R.string.nope), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                return false;
            }
        });
        listView.setAdapter(adapter);
    }

    public void programAddCity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        final EditText edit = new EditText(this);
        builder.setView(edit);
        builder.setTitle(getResources().getString(R.string.title));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ruWord = edit.getText().toString();
                ruWord = ruWord.replace(" ", "");
                String enWord = "";
                if (!ruWord.equals("")) {
                    TranslateCity trans = new TranslateCity(ruWord, EN);
                    try {
                        enWord = trans.async.execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                if (!db.check(ruWord) && !ruWord.isEmpty()) {
                    db.insert(ruWord, enWord);
                    makeList();
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
