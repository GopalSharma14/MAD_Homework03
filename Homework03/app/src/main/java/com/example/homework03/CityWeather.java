package com.example.homework03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.security.cert.TrustAnchor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class CityWeather extends AppCompatActivity {
        //implements WeatherAdapter.ForeCastInterface {

    private TextView tv_cityCountry;
    private TextView tv_weatherText;
    private TextView tv_forecast;
    private TextView daytext_tv;
    private TextView nighttext_tv;
    private TextView clickDetails_tv;


    private Button saveCity_btn;
    private Button setAsCurrent_btn;

    private ImageView day_iv;
    private ImageView night_iv;

    private static final String TAG = "CityWeather";

    RecyclerView myRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    String imageURL = "https://developer.accuweather.com/sites/default/files/";
    String countryName;
    String cityName;
    Double temperature;
    Boolean dup;
    String cityKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_weather);

        tv_cityCountry=findViewById(R.id.tv_cityCountry);
        tv_weatherText=findViewById(R.id.tv_weatherText);
        tv_forecast=findViewById(R.id.tv_forecast);
        daytext_tv=findViewById(R.id.daytext_tv);
        nighttext_tv=findViewById(R.id.nighttext_tv);
        saveCity_btn=findViewById(R.id.saveCity_btn);
        setAsCurrent_btn=findViewById(R.id.setAsCurrent);
        day_iv=findViewById(R.id.day_iv);
        night_iv=findViewById(R.id.night_iv);
        clickDetails_tv=findViewById(R.id.clickDetails_tv);
        myRecyclerView=findViewById(R.id.myRecyclerView);

        setTitle("City Weather");

        Intent intent=getIntent();
        Bundle args=intent.getBundleExtra("BUNDLE");
        ArrayList<City> cityArrayList = (ArrayList<City>) args.getSerializable("ArrayList");
        Log.d(TAG, "onCreate: "+cityArrayList);
        SimpleDateFormat format1=new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat newformat=new SimpleDateFormat("MM-dd-yyyy");
        String todaysDate=null;
        String currentDate=null;
        String weatherIconImageDay,weatherIconImageNight=null;
        Date date=new Date();
        try {
            todaysDate=newformat.format(format1.parse(String.valueOf(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }  for (int i = 0; i < cityArrayList.size(); i++) {
            try {
                currentDate=newformat.format(format2.parse(cityArrayList.get(i).date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
           // if(currentDate.equals(todaysDate)){
                tv_cityCountry.setVisibility(View.VISIBLE);
                tv_cityCountry.setText(cityArrayList.get(i).cityname+", "+cityArrayList.get(i).countryName);
                tv_weatherText.setText(cityArrayList.get(i).weatherText);
                try {
                    tv_forecast.setText("Forecast on "+new SimpleDateFormat("MMM dd yyyy").format(newformat.parse(currentDate))+"\n" +"Temperature "+
                            cityArrayList.get(i).valueMax+"/"+cityArrayList.get(i).valueMin+" F");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (cityArrayList.get(i).dayIcon < 10) {
                    weatherIconImageDay = imageURL + "0" + cityArrayList.get(i).dayIcon + "-s.png";
                } else {
                    weatherIconImageDay = imageURL + cityArrayList.get(i).dayIcon.toString() + "-s.png";
                }
                if (cityArrayList.get(i).nightIcon < 10) {
                    weatherIconImageNight = imageURL + "0" + cityArrayList.get(i).nightIcon + "-s.png";
                } else {
                    weatherIconImageNight = imageURL + cityArrayList.get(i).nightIcon.toString() + "-s.png";
                }
                Picasso.get().load(weatherIconImageDay.trim()).into(day_iv);
                Picasso.get().load(weatherIconImageNight.trim()).into(night_iv);
                daytext_tv.setText(cityArrayList.get(i).dayPhrase);
                nighttext_tv.setText(cityArrayList.get(i).nightPhrase);
                final String mLink=cityArrayList.get(i).mlink;
                clickDetails_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(mLink));
                        startActivity(intent);
                    }
                });

            //}
            saveCity_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Forecast forecast = (Forecast) getIntent().getExtras().getSerializable("ForecastClass");
                    Intent data = new Intent();
                    data.putExtra("cityKey",cityKey);
                    data.putExtra("countryName", countryName);
                    data.putExtra("cityName", cityName);
                    data.putExtra("temperature",temperature);
                    data.putExtra("duplicate",dup);
                    data.putExtra("forecastdetails", forecast);
                    setResult(200, data);
                    finish();
                }
            });
            myRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
            myRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter= new WeatherAdapter(cityArrayList);
            myRecyclerView.setAdapter(mAdapter);
        }
    }

   /* @Override
    public void getDetails(City city, int position) {
        arrayListPosition = position;
        loadScreen(city);
    }*/
}
