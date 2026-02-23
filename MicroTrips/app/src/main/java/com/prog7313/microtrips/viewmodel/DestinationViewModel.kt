package com.prog7313.microtrips.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.prog7313.microtrips.models.Destination
import com.prog7313.microtrips.repository.DestinationJsonStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DestinationViewModel(app: Application) : AndroidViewModel(app) {
    private val store = DestinationJsonStore(app.applicationContext)
    private val _destinations = MutableStateFlow<List<Destination>>(emptyList())
    val destinations: StateFlow<List<Destination>> = _destinations.asStateFlow()

    init {
        viewModelScope.launch {
            _destinations.value = store.load()
        }
    }

    fun addDestination(destination: Destination) {
        val updated = _destinations.value + destination
        _destinations.value = updated

        viewModelScope.launch {
            store.save(updated)
        }
    }
}