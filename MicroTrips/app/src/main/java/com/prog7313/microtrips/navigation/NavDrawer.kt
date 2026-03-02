package com.prog7313.microtrips.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NavDrawer(
    onViewDestinations: () -> Unit,
    onSaved: () -> Unit,
    onSettings: () -> Unit
) {
    ModalDrawerSheet {
        Text(
            "MicroTrips",
            modifier = Modifier.padding(16.dp)
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Destinations") },
            selected = false,
            onClick = onViewDestinations
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = "Saved") },
            label = { Text("Saved") },
            selected = false,
            onClick = onSaved
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = false,
            onClick = onSettings
        )
    }
}