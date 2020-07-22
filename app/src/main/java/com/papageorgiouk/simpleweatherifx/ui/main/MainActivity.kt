package com.papageorgiouk.simpleweatherifx.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DiffUtil
import com.papageorgiouk.simpleweatherifx.databinding.ActivityMainBinding
import com.papageorgiouk.simpleweatherifx.domain.WeatherDataEntity
import com.papageorgiouk.simpleweatherifx.ui.details.DetailsActivity
import com.papageorgiouk.simpleweatherifx.ui.view.UiState
import com.papageorgiouk.simpleweatherifx.ui.view.querySubmissions
import com.papageorgiouk.simpleweatherifx.ui.view.showError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import org.koin.android.viewmodel.ext.android.viewModel

const val MAX_ITEMS = 5

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    private val adapter = MainWeatherAdapter { view, row -> navigateToDetails(view, row) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainRecycler.adapter = adapter

        viewModel.requestStatus.observe(this) { status ->
            when (status) {
                is UiState.Loading -> displayProgressbar(true)
                is UiState.Success -> displayProgressbar(false)  //  we don't need the data in this case, reactive Room will provide the addition
                is UiState.Error -> {
                    showError(binding.root, status.exception.message ?: "Error")
                    displayProgressbar(false)
                }
            }
        }

        /*
        As data from the db come in, diff the data and dispatch updates to the recycler adapter
         */
        viewModel.historyFlow
            .scan(listOf<WeatherDataEntity?>()) { old, new ->
                DiffUtil.calculateDiff(
                    RowDiffCallback(old, new)
                ).dispatchUpdatesTo(adapter)
                adapter.data = new
                binding.mainRecycler.smoothScrollToPosition(0)
                new
            }.launchIn(lifecycleScope)

        binding.searchView.querySubmissions()
            .filterNotNull()
            .onEach { viewModel.onCityEntered(it) }
            .launchIn(lifecycleScope)

    }

    private fun displayProgressbar(loading: Boolean) {
        when (loading) {
            true -> binding.progressBar.show()
            false -> binding.progressBar.hide()
        }
    }

    /**
     * Open the [DetailsActivity]
     *
     * @param transitionView view which will be used as the shared element in the transition
     * @param dataEntity the historical weather data point
     */
    private fun navigateToDetails(transitionView: View, dataEntity: WeatherDataEntity) {
        DetailsActivity.start(this, transitionView, dataEntity.primaryKey!!)  //  we need the crash
    }

}

