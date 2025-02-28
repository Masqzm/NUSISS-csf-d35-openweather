package csf.day35_owm.repo;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OpenWeatherRepo {
    @Autowired @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    // GET <city>
    public String getWeatherInfo(String city) {
        return template.opsForValue().get(city);
    }

    // SET <city> <json> EX <min>
    public void cacheWeatherInfo(String city, String json, int min) {
        template.opsForValue().set(city, json, min, TimeUnit.MINUTES);
    }
}
