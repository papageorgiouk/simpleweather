package com.papageorgiouk.simpleweatherifx.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.papageorgiouk.simpleweatherifx.R
import com.papageorgiouk.simpleweatherifx.databinding.ItemCityWeatherBinding
import com.papageorgiouk.simpleweatherifx.domain.WeatherDataEntity
import com.papageorgiouk.simpleweatherifx.ui.view.loadFromResourceName

typealias OnItemClick = (view: View, row: WeatherDataEntity) -> Unit

/**
 * The recycler adapter that will hold the city cards
 *
 * @param clickListener a typealiased higher order function with two parameters
 *      1. view -> the view that will be used as the shared element for the activity transition
 *      2. row -> the data contained in the clicked row
 */
class MainWeatherAdapter(val clickListener: OnItemClick): RecyclerView.Adapter<MainWeatherAdapter.WeatherRowVH>() {

    var data: List<WeatherDataEntity?> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherRowVH {
        return LayoutInflater.from(parent.context)
            .let { ItemCityWeatherBinding.inflate(it, parent, false) }
            .let { WeatherRowVH(it, clickListener) }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: WeatherRowVH, position: Int) {
        val item = data[position]
        item?.let { holder.bind(it) }

        //  shared element transition name
        ViewCompat.setTransitionName(holder.itemBinding.root, item.toString())
    }

    class WeatherRowVH(
        val itemBinding: ItemCityWeatherBinding,
        val listener: OnItemClick
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(datapoint: WeatherDataEntity) {
            with(itemBinding.txtCityCountry) {
                text = context.getString(R.string.city_country, datapoint.cityName, datapoint.countryCode)
            }
            itemBinding.txtDescr.text = datapoint.weather.description

            //  temperatures
            val context = itemBinding.root.context
            itemBinding.txtTemp.text = context.getString(R.string.temperature_celsius, datapoint.temp)
            itemBinding.txtFeelsLike.text = context.getString(R.string.feels_like, datapoint.appTemp)

            //  load drawable by string
            itemBinding.imgIcon.loadFromResourceName(datapoint.weather.icon)

            //  on click call the listener
            itemBinding.root.setOnClickListener { listener(itemBinding.root, datapoint) }
        }

    }

}

class RowDiffCallback(val old: List<WeatherDataEntity?>, val new: List<WeatherDataEntity?>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition]?.primaryKey == new[newItemPosition]?.primaryKey
    }

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        //  not needed
        return true
    }

}