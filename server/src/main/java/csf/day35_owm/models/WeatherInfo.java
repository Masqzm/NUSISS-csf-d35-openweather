package csf.day35_owm.models;

import java.io.StringReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class WeatherInfo {
    private List<Weather> weathers;

    private String city;
    private double temp;        // °C
    private double feels_like;  // °C
    private double temp_min;    // °C
    private double temp_max;    // °C
    private double wind_spd;    // m/s
    private double wind_deg;    // deg
    private int pressure;       // hPa
    private int humidity;       // %
    private int visibility;     // m
    private LocalDateTime measuredDateTime;    // dt

    private boolean isFromCache = false;

    // https://openweathermap.org/weather-conditions
    private static final String ICON_URL_PREFIX = "https://openweathermap.org/img/wn/";
    private static final String ICON_URL_SUFFIX = "@2x.png";

    public static WeatherInfo jsonToWeatherInfo(String json) {
        if(json == null)
            return null;
            
        WeatherInfo wInfo = new WeatherInfo();

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject jsonObj = reader.readObject();
        JsonArray weatherArr = jsonObj.getJsonArray("weather");
        List<Weather> conditionsList = new ArrayList<>();

        for(int i = 0; i < weatherArr.size(); i++) {
            Weather wCondition = new Weather();
            JsonObject j = weatherArr.getJsonObject(i);

            wCondition.setMain(j.getString("main"));
            wCondition.setDescription(j.getString("description"));            
            wCondition.setIcon(ICON_URL_PREFIX + j.getString("icon") + ICON_URL_SUFFIX);

            conditionsList.add(wCondition);
        }

        wInfo.setWeathers(conditionsList);

        wInfo.setCity(jsonObj.getString("name"));

        JsonObject mainInfo = jsonObj.getJsonObject("main");
        wInfo.setTemp(mainInfo.getJsonNumber("temp").doubleValue());
        wInfo.setFeels_like(mainInfo.getJsonNumber("feels_like").doubleValue());
        wInfo.setTemp_min(mainInfo.getJsonNumber("temp_min").doubleValue());
        wInfo.setTemp_max(mainInfo.getJsonNumber("temp_max").doubleValue());
        wInfo.setPressure(mainInfo.getInt("pressure"));
        wInfo.setHumidity(mainInfo.getInt("humidity"));
        wInfo.setVisibility(jsonObj.getInt("visibility"));

        JsonObject windInfo = jsonObj.getJsonObject("wind");
        wInfo.setWind_spd(windInfo.getJsonNumber("speed").doubleValue());
        wInfo.setWind_deg(windInfo.getJsonNumber("deg").doubleValue());
        
        wInfo.setMeasuredDateTime(unixToLocalDT(jsonObj.getJsonNumber("dt").longValue()));

        return wInfo;
    } 

    public String toJson() {
        JsonArrayBuilder jArrWeathers = Json.createArrayBuilder();
        for (Weather w : weathers) 
            jArrWeathers.add(w.toJson());   

        JsonObject json = Json.createObjectBuilder()
                        .add("weathers", jArrWeathers.build())
                        .add("city", city)
                        .add("temp", temp)
                        .add("feels_like", feels_like)
                        .add("temp_min", temp_min)
                        .add("temp_max", temp_max)
                        .add("wind_spd", wind_spd)
                        .add("wind_deg", wind_deg) 
                        .add("pressure", pressure) 
                        .add("humidity", humidity) 
                        .add("visibility", visibility) 
                        .add("measuredDateTime", measuredDateTime.toString()) 
                        .add("isFromCache", isFromCache) 
                        .build();

        return json.toString();
    }

    private static LocalDateTime unixToLocalDT(long unixTimestamp) {
        // Convert Unix timestamp to an Instant
        Instant instant = Instant.ofEpochSecond(unixTimestamp);

        // Convert Instant to LocalDateTime in the system's default time zone
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public List<Weather> getWeathers() {
        return weathers;
    }
    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public double getTemp() {
        return temp;
    }
    public void setTemp(double temp) {
        this.temp = temp;
    }
    public double getFeels_like() {
        return feels_like;
    }
    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }
    public double getTemp_min() {
        return temp_min;
    }
    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }
    public double getTemp_max() {
        return temp_max;
    }
    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }
    public double getWind_spd() {
        return wind_spd;
    }
    public void setWind_spd(double wind_spd) {
        this.wind_spd = wind_spd;
    }
    public double getWind_deg() {
        return wind_deg;
    }
    public void setWind_deg(double wind_deg) {
        this.wind_deg = wind_deg;
    }
    public int getPressure() {
        return pressure;
    }
    public void setPressure(int pressure) {
        this.pressure = pressure;
    }
    public int getHumidity() {
        return humidity;
    }
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
    public int getVisibility() {
        return visibility;
    }
    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
    public LocalDateTime getMeasuredDateTime() {
        return measuredDateTime;
    }
    public void setMeasuredDateTime(LocalDateTime measuredDateTime) {
        this.measuredDateTime = measuredDateTime;
    }
    public boolean isFromCache() {
        return isFromCache;
    }
    public void setFromCache(boolean isFromCache) {
        this.isFromCache = isFromCache;
    }
    public static String getIconUrlPrefix() {
        return ICON_URL_PREFIX;
    }
    public static String getIconUrlSuffix() {
        return ICON_URL_SUFFIX;
    }
}