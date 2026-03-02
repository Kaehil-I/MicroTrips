package com.prog7313.microtrips.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.prog7313.microtrips.models.Destination
import com.prog7313.microtrips.prefs.AppPrefs
import com.prog7313.microtrips.repository.DestinationJsonStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DestinationViewModel(application: Application) : AndroidViewModel(application) {
    private val destinationStore = DestinationJsonStore(application)
    private val appPrefs = AppPrefs(application)

    private val _destinations = MutableStateFlow<List<Destination>>(emptyList())
    val destinations = _destinations.asStateFlow()

    val savedDestinationIds = appPrefs.savedDestinationIds.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptySet()
    )

    val savedDestinations = combine(destinations, savedDestinationIds) { dests, ids ->
        dests.filter { it.id in ids }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            _destinations.value = destinationStore.load()
        }
    }

    fun addDestination(destination: Destination) {
        viewModelScope.launch {
            val updatedDestinations = _destinations.value + destination
            destinationStore.save(updatedDestinations)
            _destinations.value = updatedDestinations
        }
    }

    fun getDestination(id: Long): Destination? {
        return _destinations.value.find { it.id == id }
    }

    fun saveDestination(id: Long) {
        viewModelScope.launch {
            appPrefs.saveDestination(id)
        }
    }

    fun unsaveDestination(id: Long) {
        viewModelScope.launch {
            appPrefs.unsaveDestination(id)
        }
    }
}