package com.prog7313.microtrips.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prog7313.microtrips.util.shareDestination
import com.prog7313.microtrips.viewmodel.DestinationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewDestScreen(
    destinationVm : DestinationViewModel,
    onBack: () -> Unit
    ) {
    val context = LocalContext.current

    val destinationState = destinationVm.destinations.collectAsStateWithLifecycle()
    val destinations = destinationState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Destinations") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(
                items = destinations,
                key = { it.id }
            ) { g ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                        .clickable { shareDestination(context, g) }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(g.destinationName, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "R${"%.2f".format(g.destinationPrice)}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(g.destinationDescription, style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(4.dp))
                        Text("Tap to share", style = MaterialTheme.typography.labelSmall)
                    }

                }

            }
        }
    }
}