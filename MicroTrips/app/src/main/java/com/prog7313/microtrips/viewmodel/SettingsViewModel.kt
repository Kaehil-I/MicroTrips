package com.prog7313.microtrips.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.prog7313.microtrips.prefs.AppPrefs
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val appPrefs = AppPrefs(application)

    val isDarkMode = appPrefs.isDarkMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false
    )

    val showBudgetBadges = appPrefs.showBudgetBadges.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = true
    )

    fun setDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            appPrefs.setDarkMode(isDark)
        }
    }

    fun setShowBudgetBadges(show: Boolean) {
        viewModelScope.launch {
            appPrefs.setShowBudgetBadges(show)
        }
    }
}