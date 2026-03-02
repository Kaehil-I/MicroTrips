package com.prog7313.microtrips.repository

import android.content.Context
import com.prog7313.microtrips.models.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import java.io.File
import java.util.UUID
import kotlinx.serialization.json.Json

class DestinationJsonStore(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = true}
    private val assetPath = "data/gems.json"
    private val fileName = "destination.json"

    private fun internalFile(): File = File(context.filesDir, fileName)

    private fun ensureSeeded() {
        val f = internalFile()

        if (f.exists()) {
            return
        }
        val seed = context.assets.open(assetPath).bufferedReader().use { it.readText() }
        f.writeText(seed)
    }

    suspend fun load(): List<Destination> = withContext(Dispatchers.IO) {
        ensureSeeded()

        val originalText = internalFile().readText()
        val loaded: List<Destination> = json.decodeFromString(originalText)

        val normalized = loaded.map { g ->
            if (g.id == 0L) g.copy(id = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE) else g
        }

        if (normalized != loaded) {
            internalFile().writeText(json.encodeToString(normalized))
        }
        normalized
    }

    suspend fun save(destinations: List<Destination>) = withContext(Dispatchers.IO) {
        ensureSeeded()

        val normalized = destinations.map { g ->
            if(g.id == 0L) g.copy(id = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE) else g
        }

        internalFile().writeText(json.encodeToString(normalized))
    }
}