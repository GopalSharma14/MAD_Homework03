package com.example.homework03;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private Button currentCity_btn;
    private EditText cityName_ed;
    private EditText countryName_ed;
    private TextView message_tv;
    private TextView topMessage_tv;
    private Button searchCity_btn;
    private ProgressBar progressBar;
    private TextView cityCountry_tv;
    private TextView temperature_tv;
    private ImageView weatherIcon_iv;
    private EditText city_ed;
    private EditText country_ed;
    private int counter = 0;
    String cityname = null;
    String countryname = null;


    String location_url = " https://dataservice.accuweather.com/locations/v1/cities/";
    String weather_url = "https://dataservice.accuweather.com/currentconditions/v1/";
    String imageURL = "https://developer.accuweather.com/sites/default/files/";
    String forecastURL = "https://dataservice.accuweather.com/forecasts/v1/daily/5day/";
    static final String TAG = "demo";
    public static String aKey;

    private void getCityDialog() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.city_search);
        dialog.setTitle("Enter City Details");
        dialog.setCancelable(false);

        final TextView city_ed = dialog.findViewById(R.id.city_ed);
        final TextView country_ed = dialog.findViewById(R.id.country_ed);
        final TextView enterDetails_tv = dialog.findViewById(R.id.enterDetails_tv);

        Button cancel_btn = dialog.findViewById(R.id.cancel_btn);
        Button ok_btn = dialog.findViewById(R.id.ok_btn);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String city_name = city_ed.getText().toString();
                String country_name = country_ed.getText().toString();
                if (!city_name.isEmpty() && !country_name.isEmpty()) {
                    new GetLocation().execute(city_name, country_name);
                } else {
                    Toast.makeText(MainActivity.this, "Enter complete details!!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Weather App");
        topMessage_tv = findViewById(R.id.topMessage_tv);
        currentCity_btn = findViewById(R.id.currentCity_btn);
        cityName_ed = findViewById(R.id.cityName_ed);
        countryName_ed = findViewById(R.id.countryName_ed);
        message_tv = findViewById(R.id.message_tv);
        cityCountry_tv = findViewById(R.id.cityCountry_tv);
        temperature_tv = findViewById(R.id.temperature_tv);
        weatherIcon_iv = findViewById(R.id.weatherIcon_iv);
        searchCity_btn = findViewById(R.id.searchCity_btn);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        cityCountry_tv.setVisibility(View.INVISIBLE);
        temperature_tv.setVisibility(View.INVISIBLE);
        weatherIcon_iv.setVisibility(View.INVISIBLE);
        message_tv.setVisibility(View.VISIBLE);

        currentCity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCityDialog();
            }
        });


        cityCountry_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                getCityDialog();
                return Boolean.parseBoolean(null);
            }
        });

        searchCity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityname = cityName_ed.getText().toString();
                countryname = countryName_ed.getText().toString();
                new GetCities().execute(cityname, countryname);

            }
        });

    }

    class GetCities extends AsyncTask<String, Void, ArrayList> {

        ArrayList<Location> location = new ArrayList<>();
        String countryname = null;
        String cityname = null;

        @Override
        protected ArrayList doInBackground(String... strings) {
            HttpsURLConnection connection = null;
            cityname = strings[0];
            countryname = strings[1];
            try {
                URL url = new URL(location_url
                        + URLEncoder.encode(countryname, "UTF-8")
                        + "/search?apikey=" + "dCrnSvuSm6q6lM7z7FAxSBmS2x1rmstW"
                        + "&q=" + URLEncoder.encode(cityname, "UTF-8"));
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    if (!json.isEmpty()) {
                        JSONArray jarray = new JSONArray(json);
                        for (int i = 0; i < jarray.length(); i++) {
                            String city = jarray.getJSONObject(i).getString("LocalizedName");
                            JSONObject jsonObject = jarray.getJSONObject(i);
                            JSONObject countryJSON = jsonObject.getJSONObject("AdministrativeArea");
                            String state = countryJSON.getString("ID");
                            location.add(new Location(city, state));
                        }
                    } else {
                        return null;
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return location;
        }

        @Override
        protected void onPostExecute(final ArrayList arrayList) {
            super.onPostExecute(arrayList);
            String finalList = null;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arrayList.size(); i++) {
                sb.append(arrayList.get(i));
                sb.append(";");
            }
            final String[] sub_str = sb.toString().split(";");
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Select City")
                    .setItems(sub_str, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String[] str = sub_str[which].split(",");
                            String city = str[0];
                            String state = str[1];
                            new GetLocationforForecast().execute(city, countryname, state);
                        }
                    });
            builder.create().show();

        }
    }

    class GetLocationforForecast extends AsyncTask<String, Void, String> {

        String Key2 = null;

        @Override
        protected String doInBackground(String... strings) {
            HttpsURLConnection connection = null;
            String cityname = strings[0];
            String countryname = strings[1];
            String statename = strings[2];
            try {
                URL url = new URL(location_url
                        + URLEncoder.encode(countryname, "UTF-8")
                        + "/search?apikey=" + "dCrnSvuSm6q6lM7z7FAxSBmS2x1rmstW"
                        + "&q=" + URLEncoder.encode(cityname, "UTF-8"));

                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    if (!json.isEmpty()) {
                        JSONArray jarray = new JSONArray(json);
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jsonObject = jarray.getJSONObject(i);
                            JSONObject jsn = jsonObject.getJSONObject("AdministrativeArea");
                            String state1 = jsn.getString("ID").toString();
                            //Log.d(TAG, "doInBackground: "+state1);
                            if (state1.equals(statename)) {
                                Key2 = jsonObject.getString("Key");
                            }
                        }

                    }
                } else {
                    return null;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Key2;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            aKey = s;
            new GetForecast().execute(aKey);
        }
    }

    class GetForecast extends AsyncTask<String, Void, ArrayList<City>> {

        ArrayList<City> city = new ArrayList<>();
       // ArrayList<String> tempValues = new ArrayList<>();


        @Override
        protected ArrayList<City> doInBackground(String... strings) {
            HttpsURLConnection connection = null;
            String key = strings[0];
            cityname=cityName_ed.getText().toString();
            countryname=countryName_ed.getText().toString();
            try {
                URL url = new URL(forecastURL
                        + URLEncoder.encode(key, "UTF-8")
                        + "?apikey=" + "dCrnSvuSm6q6lM7z7FAxSBmS2x1rmstW");
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    if (!json.isEmpty()) {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("Headline");
                        //String date = jsonObject1.getString("EffectiveDate");
                        String text = jsonObject1.getString("Text");
                        JSONArray jsonArray = jsonObject.getJSONArray("DailyForecasts");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            String date=jsonObject2.getString("Date");
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("Temperature");
                            JSONObject jsonObject4 = jsonObject3.getJSONObject("Minimum");
                            Double valuemin = Double.valueOf(jsonObject4.getString("Value"));
                            String unit = jsonObject4.getString("Unit");
                            JSONObject jsonObject5 = jsonObject3.getJSONObject("Maximum");
                            Double valuemax = Double.valueOf(jsonObject5.getString("Value"));
                            JSONObject jsonObject6 = jsonObject2.getJSONObject("Day");
                            JSONObject jsonObject7 = jsonObject2.getJSONObject("Night");
                            String dayPhrase = jsonObject6.getString("IconPhrase");
                            Integer dayIcon= Integer.valueOf(jsonObject6.getString("Icon"));
                            String nightPhrase = jsonObject7.getString("IconPhrase");
                            Integer nightIcon= Integer.valueOf(jsonObject7.getString("Icon"));
                            String link=jsonObject2.getString("MobileLink");
                            city.add(new City(cityname,countryname,date, text, valuemin, valuemax, unit, dayPhrase,dayIcon, nightPhrase,nightIcon,link));

                        }
                    }else{
                        return null;
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return city;
        }

        @Override
        protected void onPostExecute(ArrayList<City> city) {
            super.onPostExecute(city);
           /* StringBuilder sb = new StringBuilder();
            for (int i=0;i<city.size();i++)
            {
                sb.append(city.get(i));
                sb.append("\n");
            }
            Log.d(TAG, "onPostExecute: "+sb);*/
            Log.d(TAG, "onPostExecute: "+city);
            Intent intent = new Intent(MainActivity.this, CityWeather.class);
            Bundle args=new Bundle();
            args.putSerializable("ArrayList",city);
            intent.putExtra("BUNDLE", args);
            startActivity(intent);
        }
    }

    class GetLocation extends AsyncTask<String, Void, String> {

        String Key1 = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpsURLConnection connection = null;
            cityname = strings[0];
            countryname = strings[1];
            Log.d("demo", cityname + " " + countryname);
            try {
                URL url = new URL(location_url
                        + URLEncoder.encode(countryname, "UTF-8")
                        + "/search?apikey=" + "dCrnSvuSm6q6lM7z7FAxSBmS2x1rmstW"
                        + "&q=" + URLEncoder.encode(cityname, "UTF-8"));

                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    if (!json.isEmpty()) {
                        JSONArray jarray = new JSONArray(json);
                        JSONObject jsonObject = jarray.getJSONObject(0);
                        Key1 = jsonObject.getString("Key");
                    } else {
                        return null;
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Key1;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            aKey = s;
            progressBar.setVisibility(View.INVISIBLE);
            if (s == null) {
                Toast.makeText(MainActivity.this, "No city found.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Current city details saved.", Toast.LENGTH_SHORT).show();
            }
            new GetWeather().execute(aKey);
            Log.d("demo", aKey);
        }

    }

    class GetWeather extends AsyncTask<String, Void, ArrayList<Weather>> {

        ArrayList<Weather> weather = new ArrayList<>();
        String finalImgURL = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            counter = 0;
            weatherIcon_iv.setVisibility(View.INVISIBLE);
        }

        @Override
        protected ArrayList doInBackground(String... strings) {
            HttpsURLConnection connection = null;
            String Key = strings[0];
            try {
                URL url1 = new URL(weather_url + URLEncoder.encode(Key, "UTF-8") + "?apikey=" + "dCrnSvuSm6q6lM7z7FAxSBmS2x1rmstW");
                connection = (HttpsURLConnection) url1.openConnection();
                connection.connect();

                if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONArray jarray = new JSONArray(json);
                    JSONObject jobj = jarray.getJSONObject(0);
                    String localobservationDateTime = jobj.getString("LocalObservationDateTime");
                    String weatherText = jobj.getString("WeatherText");
                    Integer weatherIcon = jobj.getInt("WeatherIcon");
                    JSONObject tempJSON = jobj.getJSONObject("Temperature");
                    JSONObject metricJSON = tempJSON.getJSONObject("Metric");
                    Double value = metricJSON.getDouble("Value");
                    String unit = metricJSON.getString("Unit");

                    weather.add(new Weather(localobservationDateTime, weatherText, weatherIcon, value, unit));
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return weather;
        }

        @Override
        protected void onPostExecute(ArrayList arrayList) {
            super.onPostExecute(arrayList);

            ArrayList<Weather> weather = new ArrayList<>();
            weather = arrayList;
            topMessage_tv.setVisibility(View.INVISIBLE);
            currentCity_btn.setVisibility(View.INVISIBLE);
            temperature_tv.setVisibility(View.VISIBLE);
            message_tv.setVisibility(View.INVISIBLE);
            if (weather.get(counter).weatherIcon < 10) {
                finalImgURL = imageURL + "0" + weather.get(counter).weatherIcon + "-s.png";
            } else {
                finalImgURL = imageURL + weather.get(counter).weatherIcon.toString() + "-s.png";
            }
            Picasso.get().load(finalImgURL.trim()).into(weatherIcon_iv);
            weatherIcon_iv.setVisibility(ImageView.VISIBLE);
            cityCountry_tv.setText(cityname + ", " + countryname + "\n" + weather.get(counter).weatherText);

            cityCountry_tv.setTextSize(30);
            cityCountry_tv.setVisibility(View.VISIBLE);
            PrettyTime t = new PrettyTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = null;
            try {
                date = formatter.parse(weather.get(counter).localobservationDateTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            temperature_tv.setText("Temperature: " + weather.get(counter).value + " " + weather.get(counter).unit + "\n" +
                    "Updated: " + t.format(date));
            temperature_tv.setTextSize(15);
        }
    }

}
