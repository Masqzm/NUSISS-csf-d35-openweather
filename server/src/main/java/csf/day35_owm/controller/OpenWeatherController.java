package csf.day35_owm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import csf.day35_owm.models.WeatherInfo;
import csf.day35_owm.service.OpenWeatherService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping("/api")
public class OpenWeatherController {
    @Autowired
    private OpenWeatherService openWeatherSvc;

    @GetMapping("/search")
    public ResponseEntity<String> search(@RequestParam(name="q") String city) {
        WeatherInfo winfo;
        
        try {
            winfo = openWeatherSvc.getWeatherInfo(city);
        } catch(Exception ex) {
            JsonObject json = Json.createObjectBuilder()
                            .add("status", 404)
                            .add("message", ex.getMessage())
                            .build();
            
            return ResponseEntity.status(404).body(json.toString());
        }
        
        //System.out.println(winfo.toJson());

        return ResponseEntity.ok(winfo.toJson());
    }
}