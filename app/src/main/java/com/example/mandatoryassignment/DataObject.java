package com.example.mandatoryassignment;

import android.text.format.DateFormat;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

public class DataObject implements Serializable {
    @SerializedName("deviceId")
    private Integer deviceId;

    @SerializedName("pm25")
    private Double pm25;

    @SerializedName("pm10")
    private Double pm10;

    @SerializedName("co2")
    private Integer co2;

    @SerializedName("o3")
    private Integer o3;

    @SerializedName("pressure")
    private Double pressure;

    @SerializedName("temperature")
    private Double temperature;

    @SerializedName("humidity")
    private Double humidity;

    @SerializedName("utc")
    private long utc;

    @SerializedName("latitude")
    private Double latitude;

    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("noise")
    private Integer noise;

    @SerializedName("userId")
    private String userId;

    /**
     * No args constructor for use in serialization
     *
     */
    public DataObject() {
    }

    /**
     *
     * @param humidity
     * @param pressure
     * @param o3
     * @param userId
     * @param utc
     * @param co2
     * @param pm10
     * @param longitude
     * @param latitude
     * @param pm25
     * @param deviceId
     * @param noise
     * @param temperature
     */
    public DataObject(Integer deviceId, Double pm25, Double pm10, Integer co2, Integer o3, Double pressure, Double temperature, Double humidity, long utc, Double latitude, Double longitude, Integer noise, String userId) {
        super();
        this.deviceId = deviceId;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.co2 = co2;
        this.o3 = o3;
        this.pressure = pressure;
        this.temperature = temperature;
        this.humidity = humidity;
        this.utc = utc;
        this.latitude = latitude;
        this.longitude = longitude;
        this.noise = noise;
        this.userId = userId;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Double getPm25() {
        return pm25;
    }

    public void setPm25(Double pm25) {
        this.pm25 = pm25;
    }

    public Double getPm10() {
        return pm10;
    }

    public void setPm10(Double pm10) {
        this.pm10 = pm10;
    }

    public Integer getCo2() {
        return co2;
    }

    public void setCo2(Integer co2) {
        this.co2 = co2;
    }

    public Integer getO3() {
        return o3;
    }

    public void setO3(Integer o3) {
        this.o3 = o3;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public long getUtc() { return utc; }

    public void setUtc(long utc) {
        this.utc = utc;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getNoise() {
        return noise;
    }

    public void setNoise(Integer noise) {
        this.noise = noise;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }




    @Override
    public String toString() {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(utc);
        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
        return "Date: " + date + ", User: " + userId;
    }

}