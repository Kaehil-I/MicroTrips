package com.prog7313.microtrips.models

import kotlinx.serialization.Serializable

@Serializable
data class Destination (
    val id: String = "",
    val destinationName: String,
    val destinationPrice: Double,
    val destinationDescription: String
)