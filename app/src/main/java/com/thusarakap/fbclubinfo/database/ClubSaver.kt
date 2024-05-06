package com.thusarakap.fbclubinfo.database

suspend fun saveClubsToDatabase(clubDetails: String): String {
    if (clubDetails.isBlank()) {
        return "No clubs are available to save."
    }

    val clubs = clubDetails.split("\n\n").mapNotNull { clubDetail ->
        val details = clubDetail.split("\n").map { it.split(": ").last() }
        if (details.size < 15) {
            null
        } else {
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

    return if (clubs.isEmpty()) {
        "No valid clubs are available to save."
    } else {
        DatabaseIO.addClubsToDB(clubs)
        "Clubs saved successfully."
    }
}