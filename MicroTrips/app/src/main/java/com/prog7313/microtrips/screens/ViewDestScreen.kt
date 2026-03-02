package com.prog7313.microtrips.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prog7313.microtrips.viewmodel.DestinationViewModel
import com.prog7313.microtrips.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewDestScreen(
    destinationVm: DestinationViewModel,
    onBack: () -> Unit,
    onDestinationClick: (Long) -> Unit,
    settingsVm: SettingsViewModel
) {
    val destinationState = destinationVm.destinations.collectAsStateWithLifecycle()
    val destinations = destinationState.value
    val showBudgetBadges by settingsVm.showBudgetBadges.collectAsStateWithLifecycle()
    val savedDestinationIds by destinationVm.savedDestinationIds.collectAsStateWithLifecycle()

    var searchQuery by remember { mutableStateOf("") }
    var selectedProvince by remember { mutableStateOf<String?>(null) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    val maxBudget = remember(destinations) { destinations.maxOfOrNull { it.budget.total }?.toFloat() ?: 5000f }
    var maxPrice by remember(maxBudget) { mutableStateOf(maxBudget) }

    val provinces = remember(destinations) { destinations.map { it.province }.distinct().sorted() }
    val categories = remember(destinations) { destinations.map { it.category }.distinct().sorted() }

    val filteredDests = remember(searchQuery, maxPrice, selectedProvince, selectedCategory, destinations, showBudgetBadges) {
        destinations.filter { dest ->
            val matchesSearch = searchQuery.isBlank() ||
                    dest.name.contains(searchQuery, ignoreCase = true) ||
                    dest.location.area.contains(searchQuery, ignoreCase = true)
            val matchesPrice = if (showBudgetBadges) dest.budget.total <= maxPrice else true
            val matchesProvince = selectedProvince == null || dest.province == selectedProvince
            val matchesCategory = selectedCategory == null || dest.category == selectedCategory
            matchesSearch && matchesPrice && matchesProvince && matchesCategory
        }
    }

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
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search by name or area") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FilterDropdown(
                    options = provinces,
                    selectedOption = selectedProvince,
                    onOptionSelected = { selectedProvince = it },
                    label = "Province",
                    modifier = Modifier.weight(1f)
                )
                FilterDropdown(
                    options = categories,
                    selectedOption = selectedCategory,
                    onOptionSelected = { selectedCategory = it },
                    label = "Category",
                    modifier = Modifier.weight(1f)
                )
            }

            if (showBudgetBadges) {
                Spacer(Modifier.height(8.dp))
                Text("Filter by max price: R${"%.2f".format(maxPrice)}")
                Slider(
                    value = maxPrice,
                    onValueChange = { maxPrice = it },
                    valueRange = 0f..maxBudget,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = filteredDests,
                    key = { it.id }
                ) { g ->
                    val isSaved = savedDestinationIds.contains(g.id)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDestinationClick(g.id) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(g.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                                IconButton(onClick = { if (isSaved) destinationVm.unsaveDestination(g.id) else destinationVm.saveDestination(g.id) }) {
                                    Icon(
                                        if (isSaved) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Save"
                                    )
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(g.location.area, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (showBudgetBadges) {
                                    InfoCard(
                                        title = "Price",
                                        value = "R${"%.2f".format(g.budget.total.toDouble())}",
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                InfoCard(
                                    title = "Time",
                                    value = g.timeNeeded,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(Modifier.height(4.dp))
                            Text(
                                g.shortDescription,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(8.dp))
                            Text("Tap to see details", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterDropdown(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedOption ?: "All",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            DropdownMenuItem(
                text = { Text("All") },
                onClick = {
                    onOptionSelected(null)
                    isExpanded = false
                }
            )
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}