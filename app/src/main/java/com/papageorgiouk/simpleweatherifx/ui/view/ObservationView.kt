package com.papageorgiouk.simpleweatherifx.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.papageorgiouk.simpleweatherifx.R

/**
 * Custom view used as the label for weather datapoint observations
 *
 * Accepts title, data, and units of measurement
 */
class ObservationView(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private val txtTitle: TextView
    private val txtData: TextView
    private val txtUnits: TextView

    init {
        View.inflate(context, R.layout.view_observation, this)

        txtTitle = findViewById<TextView>(R.id.observation_title)
        txtData = findViewById<TextView>(R.id.observation_data)
        txtUnits = findViewById<TextView>(R.id.observation_unit)

        context.theme.obtainStyledAttributes(attrs, R.styleable.ObservationView, 0, 0)
            .apply {
                try {
                    txtTitle.text = getString(R.styleable.ObservationView_title)
                    txtUnits.text = getString(R.styleable.ObservationView_units)
                } finally {
                    recycle()
                }
            }
    }

    fun setTitle(title: String) {
        this.txtTitle.text = title
    }

    fun setData(data: String) {
        this.txtData.text = data
    }

    fun setUnit(unit: String) {
        this.txtUnits.text = unit
    }

}