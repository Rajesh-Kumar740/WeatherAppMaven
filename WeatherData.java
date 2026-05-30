package com.weatherapp.model;

public class WeatherData {
    public double temperature;
    public int humidity;
    public String condition;
    public double windSpeed;

    public WeatherData(double temperature, int humidity, String condition, double windSpeed) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.condition = condition;
        this.windSpeed = windSpeed;
    }
}