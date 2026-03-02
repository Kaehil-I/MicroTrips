package com.prog7313.microtrips.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AppPrefs(private val context: Context) {

    private val darkModeKey = booleanPreferencesKey("dark_mode")
    private val showBudgetBadgesKey = booleanPreferencesKey("show_budget_badges")
    private val savedDestinationIdsKey = stringSetPreferencesKey("saved_destination_ids")

    val isDarkMode = context.dataStore.data.map {
        it[darkModeKey] ?: false
    }

    val showBudgetBadges = context.dataStore.data.map {
        it[showBudgetBadgesKey] ?: true
    }

    val savedDestinationIds = context.dataStore.data.map {
        it[savedDestinationIdsKey]?.map { it.toLong() }?.toSet() ?: emptySet()
    }

    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit {
            it[darkModeKey] = isDark
        }
    }

    suspend fun setShowBudgetBadges(show: Boolean) {
        context.dataStore.edit {
            it[showBudgetBadgesKey] = show
        }
    }

    suspend fun saveDestination(id: Long) {
        context.dataStore.edit {
            val currentIds = it[savedDestinationIdsKey] ?: emptySet()
            it[savedDestinationIdsKey] = currentIds + id.toString()
        }
    }

    suspend fun unsaveDestination(id: Long) {
        context.dataStore.edit {
            val currentIds = it[savedDestinationIdsKey] ?: return@edit
            it[savedDestinationIdsKey] = currentIds - id.toString()
        }
    }
}