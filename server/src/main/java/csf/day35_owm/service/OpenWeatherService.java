package csf.day35_owm.service;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import csf.day35_owm.models.WeatherInfo;
import csf.day35_owm.models.exception.ResourceNotFoundException;
import csf.day35_owm.repo.OpenWeatherRepo;
import jakarta.json.Json;
import jakarta.json.JsonReader;

@Service
public class OpenWeatherService {
    @Autowired
    private OpenWeatherRepo openWeatherRepo;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    //https://api.openweathermap.org/data/2.5/weather?q=singapore&units=metric&appid=b11efa596d223bdd417af6ba2d3141a6
    public static final String GET_URL = "https://api.openweathermap.org/data/2.5/weather";
    public static final int CACHE_EXPIRY_MIN = 15;

    public WeatherInfo getWeatherInfo(String city) throws Exception {
        String json = null;

        // Process city string
        city = city.toLowerCase().replaceAll(" ", "+");   

        // Lookup redis db cache
        json = openWeatherRepo.getWeatherInfo(city);

        // Make API REST call if can't find in db
        if(json == null) {
            json = fetchWeatherInfoJSON(city);

            // Cache result if avail
            openWeatherRepo.cacheWeatherInfo(city, json, CACHE_EXPIRY_MIN);
            
            return WeatherInfo.jsonToWeatherInfo(json);
        }
        
        WeatherInfo wInfo = WeatherInfo.jsonToWeatherInfo(json);
        wInfo.setFromCache(true);

        return wInfo;
    }

    // REST call
    public String fetchWeatherInfoJSON(String city) throws Exception {
        String url = UriComponentsBuilder.fromUriString(GET_URL)
                    .queryParam("q", city)
                    .queryParam("units", "metric")
                    .queryParam("appid", apiKey)
                    .toUriString();

        RequestEntity<Void> req = RequestEntity
                    .get(url)
                    .accept(MediaType.APPLICATION_JSON)     
                    .build();
                    
        // Create REST template
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp;

        try {
            // Make call
            resp = template.exchange(req, String.class);

            // 404 no results
            if(resp.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
                JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
                // Grab message from resp & include in exception to be thrown
                throw new ResourceNotFoundException(reader.readObject().getString("message"));
            }

            // Extract payload
            String payload = resp.getBody();

            return payload;
        } catch(Exception ex) {
            throw ex;
        }

        //return null;
    }
}
