package com.prog7313.microtrips.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.prog7313.microtrips.models.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addDestScreen(
    onBack: () -> Unit,
    onSave: (Destination) -> Unit
) {
    var destinationName by remember { mutableStateOf("") }
    var destinationDescription by remember { mutableStateOf("") }

    var priceText by remember { mutableStateOf("") }
    val price = priceText.toDoubleOrNull()
    val priceIsValid = price != null && price > 0.0

    val canSave = destinationName.isNotBlank() && destinationDescription.isNotBlank() && priceIsValid

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add new destination") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = destinationName,
                onValueChange = { destinationName = it },
                label = { Text("Destination Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = priceText,
                onValueChange = { input ->
                    priceText = sanitizeMoneyInput(input)
                },
                label = { Text("Price (R)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = priceText.isNotBlank() && !priceIsValid,
                supportingText = {
                    if (priceText.isNotBlank() && !priceIsValid) {
                        Text("Enter a valid amount (e.g., 1499.99)")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

            OutlinedTextField(
                value = destinationDescription,
                onValueChange = { destinationDescription = it },
                label = { Text("Description Description") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

    }
}

private fun sanitizeMoneyInput(input: String): String {
    val filtered = input.filter { it.isDigit() ||  it == '.' }

    val firstDot = filtered.indexOf('.')
    if (firstDot == -1) return filtered

    val before = filtered.substring(0, firstDot)
    val afterRaw = filtered.substring(firstDot + 1).replace(".","")
    val after = afterRaw.take(2)

    return "$before.$after"
}