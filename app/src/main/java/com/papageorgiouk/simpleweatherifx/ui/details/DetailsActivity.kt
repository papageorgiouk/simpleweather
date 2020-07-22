package com.papageorgiouk.simpleweatherifx.ui.details

import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.observe
import com.papageorgiouk.simpleweatherifx.R
import com.papageorgiouk.simpleweatherifx.databinding.ActivityDetailsBinding
import com.papageorgiouk.simpleweatherifx.domain.WeatherDataEntity
import com.papageorgiouk.simpleweatherifx.ui.view.loadFromResourceName
import com.papageorgiouk.simpleweatherifx.ui.view.showError
import org.koin.android.viewmodel.ext.android.viewModel


class DetailsActivity : AppCompatActivity() {

    private val viewModel: DetailsViewModel by viewModel()

    private val transitionName by lazy { intent.extras?.getString(EXTRA_TRANSITION_NAME) }

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportPostponeEnterTransition()

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (val extraPrimaryKey = intent.getIntExtra(EXTRA_WEATHER_DATA, 0)) {
            0 -> showError(binding.root, "Invalid item")
            else -> viewModel.entityByKey(extraPrimaryKey).observe(this) { setWeather(it) }
        }

        //  card-expand transition
        binding.cardContainer.transitionName = transitionName
        window.sharedElementEnterTransition = TransitionInflater.from(this).inflateTransition(R.transition.card_transition)
        supportStartPostponedEnterTransition()

    }

    private fun setWeather(weatherDataEntity: WeatherDataEntity) {
        binding.txtCityCountry.text = getString(R.string.city_country, weatherDataEntity.cityName, weatherDataEntity.countryCode)
        binding.imgIcon.loadFromResourceName(weatherDataEntity.weather.icon)

        binding.obTemp.apply {
            setTitle(getString(R.string.temperature))
            setData(weatherDataEntity.temp.toString())
            setUnit(getString(R.string.degrees_celsius))
        }

        binding.obAppTemp.apply {
            setTitle(getString(R.string.feels_like))
            setData(weatherDataEntity.appTemp.toString())
            setUnit(getString(R.string.degrees_celsius))
        }

        binding.obHumidity.apply {
            setTitle(getString(R.string.humidity))
            setData(weatherDataEntity.rh.toString())
            setUnit(getString(R.string.percent))
        }

        binding.obUv.apply {
            setTitle(getString(R.string.uv_index))
            setData(weatherDataEntity.uv.toString())
        }

        binding.obWindSpeed.apply {
            setTitle(getString(R.string.wind_speed))
            setData(weatherDataEntity.windSpeed.toString())
            setUnit(getString(R.string.metres_per_second))
        }

        binding.obPrecip.apply {
            setTitle(getString(R.string.precipitation))
            setData(weatherDataEntity.precip.toString())
            setUnit(getString(R.string.mm_per_hour))
        }

        binding.obSunrise.apply {
            setTitle(getString(R.string.sunrise))
            setData(weatherDataEntity.sunrise)
        }

        binding.obSunset.apply {
            setTitle(getString(R.string.sunset))
            setData(weatherDataEntity.sunset)
        }

        binding.obAirQuality.apply {
            setTitle(getString(R.string.air_quality_index))
            setData(weatherDataEntity.aqi.toString())
        }

        binding.obDatetime.apply {
            setTitle(getString(R.string.datetime))
            setData(weatherDataEntity.datetime)
        }
    }

    companion object {

        private const val EXTRA_WEATHER_DATA = "entity_key"
        private const val EXTRA_TRANSITION_NAME = "transition"

        fun start(activity: AppCompatActivity, transitionView: View, primaryKey: Int) {
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra(EXTRA_WEATHER_DATA, primaryKey)
            intent.putExtra(EXTRA_TRANSITION_NAME, ViewCompat.getTransitionName(transitionView))

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                transitionView,
                ViewCompat.getTransitionName(transitionView)!!
            )
            activity.startActivity(intent, options.toBundle())
        }

    }

}