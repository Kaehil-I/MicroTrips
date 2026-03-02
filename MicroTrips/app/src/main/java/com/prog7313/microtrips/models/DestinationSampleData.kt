package com.prog7313.microtrips.models

@JvmField
val sampleDestinations = listOf(

        Destination(
            id = 1L,
            name = "Umdloti Tidal Pool",
            province = "KwaZulu-Natal",
            category = "Beach",
            image = "umdloti_tidal_pool.jpg",
            shortDescription = "A chilled coastal swim spot with safe tidal pools and sunrise views.",
            timeNeeded = "2–4 hours",
            bestSeason = "Summer",
            budget = Budget(
                transport = 120,
                food = 120,
                entry = 0,
                misc = 30
            ),
            tips = listOf(
                "Go early for parking",
                "Pack reef shoes",
                "Bring a small cooler bag"
            ),
            location = Location(
                area = "Umdloti, Durban",
                mapsQuery = "Umdloti Tidal Pool"
            ),
            tags = listOf(
                "familyFriendly",
                "sunrise"
            )
        )

        )

