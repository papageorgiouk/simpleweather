package com.papageorgiouk.simpleweatherifx.ui.view

import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import coil.api.load
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Loads drawable by filename
 */
fun ImageView.loadFromResourceName(name: String) {
    val id = context.resources.getIdentifier(name, "drawable", context.packageName)
    load(id)
}

fun AppCompatActivity.showError(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

/**
 * Extension function that returns [SearchView] submissions in a reactive way
 */
@ExperimentalCoroutinesApi
fun SearchView.querySubmissions(): Flow<String?> {
    return callbackFlow {
        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                offer(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //  don't need this
                return false
            }
        }

        this@querySubmissions.setOnQueryTextListener(listener)
        awaitClose { setOnQueryTextListener(null) }
    }
}

sealed class UiState {
    object Loading : UiState()
    object Success : UiState()
    data class Error(val exception: Exception): UiState()
}