import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Weather, WeatherInfo } from "./models";
import { firstValueFrom, map } from "rxjs";

@Injectable()
export class WeatherService {
    // RestTemplate
    private http = inject(HttpClient)

    result$ !: Promise<WeatherInfo>

    //UNITS: string = 'metric'
    //URL: string = 'https://api.openweathermap.org/data/2.5/weather'

    searchAsPromise(q: string): Promise<WeatherInfo> {
        const params = new HttpParams().set('q', q)
                        //.set('units', this.UNITS)
                        //.set('appid', this.API_KEY)

        this.result$ = firstValueFrom(
            this.http.get<any>('/api/search', { params }).pipe(
                    map(result => {
                        const weatherInfo: WeatherInfo = {
                            // Map each JSON field to WeatherInfo obj's field
                            ...result,

                            // Convert JSON array (from JSON strings) to array of Weather objects
                            // Ie. in result.weathers JSON arr they are JSON strings
                            // so we need to convert them to Weather objects
                            // weathers: result.weathers.map((jsonW: string) => {
                            //             const w: Weather = JSON.parse(jsonW)
                            //             return w
                            //         }) 
                            // Shorthand for above
                            weathers: result.weathers.map((jsonW: string) => JSON.parse(jsonW)) 
                        }
                        return weatherInfo
                    }
                )
            ) 
            //this.http.get<WeatherInfo>(this.URL, { params })
        )

        return this.result$
    }
}