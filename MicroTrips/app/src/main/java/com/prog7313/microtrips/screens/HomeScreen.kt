package com.prog7313.microtrips.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onOpenAddDest: () -> Unit,
    onOpenView: () -> Unit,
    onExit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Micro Trips Home", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        AssetImage("code.jpg")
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = onOpenAddDest,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Add new destination") }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onOpenView,
            modifier = Modifier.fillMaxWidth()
        ) { Text("View destinations") }

        Spacer(Modifier.height(24.dp))

        OutlinedButton(
            onClick = onExit,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Exit") }
    }
}

@Composable
fun AssetImage(imageName: String) {
    val context = LocalContext.current
    val bitmap = remember(imageName) {
        context.assets.open("images/$imageName").use { input ->
            BitmapFactory.decodeStream(input)
        }
    }

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = imageName,
        modifier = Modifier.fillMaxWidth(0.50f).height(250.dp),
        contentScale = ContentScale.Fit
    )
}