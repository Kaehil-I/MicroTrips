package com.prog7313.microtrips.util

import android.content.Context
import android.content.Intent
import com.prog7313.microtrips.models.Destination

fun shareDestination(context: Context, destination: Destination) {
    val text = """
        Destination Name: ${destination.destinationName}
        Destination Price: R${"%.2f".format(destination.destinationPrice)}
        Destination Description: ${destination.destinationDescription}
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Share destination"))
}