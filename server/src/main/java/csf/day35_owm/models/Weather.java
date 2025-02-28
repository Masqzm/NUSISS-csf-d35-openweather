package csf.day35_owm.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

// Weather condition
public class Weather {
    private String main;
    private String description;
    private String icon;

    public Weather() {}
    public Weather(String main, String description, String icon) {
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public String toJson() {
        JsonObject json = Json.createObjectBuilder()
                        .add("main", main)
                        .add("description", description)
                        .add("icon", icon)
                        .build();

        return json.toString();
    }

    public String getMain() {
        return main;
    }
    public void setMain(String main) {
        this.main = main;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }

    
}
