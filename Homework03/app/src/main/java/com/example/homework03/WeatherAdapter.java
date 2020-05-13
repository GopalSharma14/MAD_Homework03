package com.example.homework03;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    ArrayList<City> cityData;

    public WeatherAdapter(ArrayList<City> cityData) {
        this.cityData = cityData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dailyweather, parent, false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    City city=cityData.get(position);
        try {
            holder.dailyDate_tv.setText(new SimpleDateFormat("dd MMM yy").format(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(city.date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dayIconImage= String.valueOf(city.dayIcon);
        if(city.dayIcon<10){
dayIconImage= "0"+dayIconImage;
        }
        try {
            Picasso.get().load("https://developer.accuweather.com/sites/default/files/" + URLEncoder.encode(dayIconImage,"UTF-8")  + "-s.png")
                    .into(holder.dailyImage_iv, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            //Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return cityData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView dailyImage_iv;
        TextView dailyDate_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dailyImage_iv=itemView.findViewById(R.id.dayImage_iv);
            dailyDate_tv=itemView.findViewById(R.id.dailyDate_tv);
        }
    }
    public interface ForeCastInterface{
        //        public void selectedItem(int position);
        public void getDetails(City city, int position);
    }

}
