package com.experiment.todolist;

import com.google.gson.Gson;
import com.google.gson.Weather;

import org.json.JSONArray;
import org.json.JSONObject;

public class Utility {

    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather6");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
