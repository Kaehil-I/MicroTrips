package com.prog7313.microtrips

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.prog7313.microtrips.navigation.AppNavGraph
import com.prog7313.microtrips.ui.theme.MicroTripsTheme
import com.prog7313.microtrips.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsVm: SettingsViewModel = viewModel()
            val isDarkMode by settingsVm.isDarkMode.collectAsStateWithLifecycle()

            MicroTripsTheme(darkTheme = isDarkMode) {
                AppNavGraph(onExit = { finish() })
            }
        }
    }
}