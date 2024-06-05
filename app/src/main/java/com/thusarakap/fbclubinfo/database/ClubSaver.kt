package com.thusarakap.fbclubinfo.database

// Function to save club details to the database
suspend fun saveClubsToDatabase(clubDetails: String): String {
    // Check if club details are missing
    if (clubDetails.isBlank()) {
        // Return a message indicating no clubs to save
        return "No clubs are available to save."
    }

    // Split the club details string into individual details and map to Club objects
    val clubs = clubDetails.split("\n\n").mapNotNull { clubDetail ->
        // Split each club detail into its properties and filter invalid ones
        val details = clubDetail.split("\n").map { it.split(": ").last() }
        if (details.size < 15) {
            // Return null if club details don't have enough properties
            null
        } else {
            // Create a Club object from the club detail properties
            Club(
                idTeam = details[0],
                name = details[1],
                strTeamShort = details[2],
                strAlternate = details[3],
                intFormedYear = details[4],
                strLeague = details[5],
                idLeague = details[6],
                strStadium = details[7],
                strKeywords = details[8],
                strStadiumThumb = details[9],
                strStadiumLocation = details[10],
                intStadiumCapacity = details[11],
                strWebsite = details[12],
                strTeamJersey = details[13],
                strTeamLogo = details[14]
            )
        }
    }

    // Check if no clubs are available to save
    return if (clubs.isEmpty()) {
        // Error message
        "No clubs are available to save."
    } else {
        // Add clubs to the database
        DatabaseIO.addClubsToDB(clubs)
        // Success message
        "Clubs saved successfully."
    }
}
