export interface Weather {
    main: string
    description: string
    icon: string
}

export interface WeatherInfo {
    city: string
    weathers: Weather[]

    temp: number
    feels_like: number
    temp_min: number
    temp_max: number
    pressure: number
    humidity: number
    
    visibility: number
    
    wind_spd: number
    wind_deg: number

    measuredDateTime: string

    isFromCache: boolean
}

// Preloaded cities data
export const CITIES: string[] = [
    'Singapore',
    'Kuala Lumpur',
    'Tokyo',
    'Bangkok',
    'Hong Kong',
    'Beijing'
] 