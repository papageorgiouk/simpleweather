package com.papageorgiouk.simpleweatherifx.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.papageorgiouk.simpleweatherifx.core.Weather
import com.papageorgiouk.simpleweatherifx.core.WeatherDataModel

/**
 * Duplicates the model from the 'core' module and adds Room-required field and annotations
 */
@Entity(tableName = "history")
data class WeatherDataEntity(

    @PrimaryKey(autoGenerate = true)
    var primaryKey: Int? = null,

    var lat: Double,
    var lon: Double,
    var sunrise: String,
    var sunset: String,
    var timezone: String,
    var station: String,
    var ob_time: String,
    var datetime: String,
    var ts: Long,
    var cityName: String,
    var countryCode: String,
    var stateCode: String,
    var pres: Double,
    var slp: Double,
    var windSpeed: Double,
    var windDir: Int,
    var windCDir: String,
    var windCDirFull: String,
    var temp: Double,
    var appTemp: Double,
    var rh: Double,
    var dewpt: Double,
    var clouds: Int,
    var pod: String,

    @Embedded
    var weather: Weather,

    var vis: Double,
    var precip: Double,
    var snow: Int,
    var uv: Double,
    var aqi: Int,
    var dhi: Double,
    var dni: Double,
    var ghi: Double,
    var solarRad: Double,
    var elevAngle: Double,
    var hAngle: Double
) {

    /**
     * Constructs from a model
     */
    constructor(model: WeatherDataModel) : this(
        null,
        model.lat,
        model.lon,
        model.sunrise,
        model.sunset,
        model.timezone,
        model.station,
        model.ob_time,
        model.datetime,
        model.ts,
        model.cityName,
        model.countryCode,
        model.stateCode,
        model.pres,
        model.slp,
        model.windSpeed,
        model.windDir,
        model.windCDir,
        model.windCDirFull,
        model.temp,
        model.appTemp,
        model.rh,
        model.dewpt,
        model.clouds,
        model.pod,
        model.weather,
        model.vis,
        model.precip,
        model.snow,
        model.uv,
        model.aqi,
        model.dhi,
        model.dni,
        model.ghi,
        model.solarRad,
        model.elevAngle,
        model.hAngle
    )

}