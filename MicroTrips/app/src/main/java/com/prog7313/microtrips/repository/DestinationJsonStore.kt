package com.prog7313.microtrips.repository

import android.content.Context
import com.prog7313.microtrips.models.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class DestinationJsonStore(private val context: Context) {
    private val json = Json { ignoreUnkownKeys = true; prettyPrint = true}
    private val assetPath = "data/destinations.json"
    private val fileName = "destination.json"

    private fun internalFile(): File = File(context.filesDir, fileName)

    private fun ensureSeeded() {
        val f = internalFile()

        if (f.exists()) return
            val seed =context.assets.open(assetPath).bufferedReader().use { it.readText() }
            f.writeText(seed)
    }

    suspend fun load(): List<Destination> = withContext(Dispatchers.IO) {
        ensureSeeded()

        val originalText = internalFile().readText()
        val loaded: List<Destination> = json.decodeFromString(originalText)

        val normalized = loaded.map { g ->
            if (g.id.isBlank()) g.copy(id = UUID.randomUUID().toString()) else g
        }

        if (normalized != loaded) {
            internalFile().writeText(json.encodeToString(normalized))
        }
        normalized
    }

    suspend fun save(destinations: List<Destination>) = withContext(Dispatchers.IO) {
        ensureSeeded()

        val normalized = destinations.map { g ->
            if(g.id.isBlank()) g.copy(id = UUID.randomUUID().toString()) else g
        }

        internalFile().writeText(json.encodeToString(normalized))
    }
}