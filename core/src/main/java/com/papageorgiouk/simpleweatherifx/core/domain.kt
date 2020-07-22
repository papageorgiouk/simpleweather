package com.papageorgiouk.simpleweatherifx.core

import com.squareup.moshi.Json

data class WeatherbitResponse(
    val data: List<WeatherDataModel>,
    val count: Int
)

data class WeatherDataModel(

    /** Latitude (Degrees) */
    val lat: Double,

    /** Longitude (Degrees) */
    val lon: Double,

    /** Sunrise time (HH:MM) */
    val sunrise: String,

    /** Sunset time (HH:MM) */
    val sunset: String,

    /** Local IANA Timezone */
    val timezone: String,

    /** Source station ID */
    val station: String,

    /** Last observation time (YYYY-MM-DD HH:MM) */
    val ob_time: String,

    /** Current cycle hour (YYYY-MM-DD:HH) */
    val datetime: String,

    /** Last observation time (Unix timestamp) */
    val ts: Long,

    /** City name */
    @Json(name = "city_name")
    val cityName: String,

    /** Country abbreviation */
    @Json(name = "country_code")
    val countryCode: String,

    /** State abbreviation/code */
    @Json(name = "state_code")
    val stateCode: String,

    /** Pressure (mb) */
    val pres: Double,

    /** Sea level pressure (mb) */
    val slp: Double,

    /** Wind speed (Default m/s) */
    @Json(name = "wind_spd")
    val windSpeed: Double,

    /** Wind direction (degrees) */
    @Json(name = "wind_dir")
    val windDir: Int,

    /** Abbreviated wind direction */
    @Json(name = "wind_cdir")
    val windCDir: String,

    /** Verbal wind direction */
    @Json(name = "wind_cdir_full")
    val windCDirFull: String,

    /** Temperature (default Celsius) */
    val temp: Double,

    /** Apparent/"Feels Like" temperature (default Celcius) */
    @Json(name = "app_temp")
    val appTemp: Double,

    /** Relative humidity (%) */
    val rh: Double,

    /** Dew point (default Celsius) */
    val dewpt: Double,

    /** Cloud coverage (%) */
    val clouds: Int,

    /** Part of the day (d = day / n = night) */
    val pod: String,

    /** Weather object */
    val weather: Weather,

    /** Visibility (default KM) */
    val vis: Double,

    /** Liquid equivalent precipitation rate (default mm/hr) */
    val precip: Double,

    /** Snowfall (default mm/hr) */
    val snow: Int,

    /** UV Index (0-11+) */
    val uv: Double,

    /** Air Quality Index [US - EPA standard 0 - +500] */
    val aqi: Int,

    /** Diffuse horizontal solar irradiance (W/m^2) [Clear Sky] */
    val dhi: Double,

    /** Direct normal solar irradiance (W/m^2) [Clear Sky] */
    val dni: Double,

    /** Global horizontal solar irradiance (W/m^2) [Clear Sky] */
    val ghi: Double,

    /** Estimated Solar Radiation (W/m^2) */
    @Json(name = "solar_rad")
    val solarRad: Double,

    /** Solar elevation angle (degrees) */
    @Json(name = "elev_angle")
    val elevAngle: Double,

    /** Solar hour angle (degrees) */
    @Json(name = "h_angle")
    val hAngle: Double
)

data class Weather(
    val icon: String,
    val code: String,
    val description: String
)