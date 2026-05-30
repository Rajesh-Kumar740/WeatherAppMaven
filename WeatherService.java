package com.weatherapp.service;

import com.weatherapp.model.WeatherData;
import com.weatherapp.util.ApiClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherService {

    private static final String API_KEY = "e6ad5b5284aa9e02eb559cc475fbc63c";

    public static WeatherData getCurrentWeather(String city) {

        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + city + "&appid=" + API_KEY + "&units=metric";

        String response = ApiClient.getResponse(url);

        if (response == null) return null;

        JsonObject obj = JsonParser.parseString(response).getAsJsonObject();

        JsonObject main = obj.getAsJsonObject("main");

        double temp = main.get("temp").getAsDouble();
        int humidity = main.get("humidity").getAsInt();

        String condition = obj.getAsJsonArray("weather")
                .get(0).getAsJsonObject()
                .get("description").getAsString();

        return new WeatherData(temp, humidity, condition, 0);
    }
}