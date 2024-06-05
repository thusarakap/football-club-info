package com.thusarakap.fbclubinfo.searchbyleague

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import org.json.JSONObject

// Function to search for clubs by league and retrieve details
suspend fun searchClubsByLeague(leagueName: String): String {
    return withContext(Dispatchers.IO) {
        // API endpoint for searching teams by league name
        val url = URL("https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=$leagueName")
        // Open a connection to the URL
        val connection = url.openConnection() as HttpsURLConnection
        try {
            // Connect to the server
            connection.connect()
            // Check if the response is OK (200)
            if (connection.responseCode == HttpsURLConnection.HTTP_OK) {
                // Get response
                val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
                // Create JSONObject from retrieved JSON string
                val jsonObject = JSONObject(jsonString)

                // Check if the "teams" array exists
                if (!jsonObject.isNull("teams")) {
                    // Get the "teams" array
                    val teamsArray = jsonObject.getJSONArray("teams")
                    // Initialize a list to store team details
                    val parsedTeams = mutableListOf<String>()

                    // Iterate through team objects in the "teams" array
                    for (i in 0 until teamsArray.length()) {
                        // Get the team object at index i
                        val teamObject = teamsArray.getJSONObject(i)

                        // Get details of the team from the JSON object
                        val idTeam = teamObject.getString("idTeam")
                        val name = teamObject.getString("strTeam")
                        val strTeamShort = teamObject.getString("strTeamShort")
                        val strAlternate = teamObject.getString("strAlternate")
                        val intFormedYear = teamObject.getString("intFormedYear")
                        val strLeague = teamObject.getString("strLeague")
                        val idLeague = teamObject.getString("idLeague")
                        val strStadium = teamObject.getString("strStadium")
                        val strKeywords = teamObject.getString("strKeywords")
                        val strStadiumThumb = teamObject.getString("strStadiumThumb")
                        val strStadiumLocation = teamObject.getString("strStadiumLocation")
                        val intStadiumCapacity = teamObject.getString("intStadiumCapacity")
                        val strWebsite = teamObject.getString("strWebsite")
                        val strTeamJersey = teamObject.getString("strTeamJersey")
                        val strTeamLogo = teamObject.getString("strTeamLogo")

                        // Create formatted string containing team details
                        val parsedTeam = """
                            idTeam: $idTeam
                            Name: $name
                            strTeamShort: $strTeamShort
                            strAlternate: $strAlternate
                            intFormedYear: $intFormedYear
                            strLeague: $strLeague
                            idLeague: $idLeague
                            strStadium: $strStadium
                            strKeywords: $strKeywords
                            strStadiumThumb: $strStadiumThumb
                            strStadiumLocation: $strStadiumLocation
                            intStadiumCapacity: $intStadiumCapacity
                            strWebsite: $strWebsite
                            strTeamJersey: $strTeamJersey
                            strTeamLogo: $strTeamLogo
                        """.trimIndent()

                        // Add formatted team details to the list
                        parsedTeams.add(parsedTeam)
                    }

                    // Join the list of team details into a single string with newlines to separate
                    parsedTeams.joinToString("\n\n")
                } else {
                    // Catch exception and return message if no teams were found for the search term
                    "No teams found for league: $leagueName"
                }
            } else {
                // Return an error message if the response code is not OK (200)
                "Error: ${connection.responseMessage}"
            }
        } finally {
            // Disconnect connection after method execution
            connection.disconnect()
        }
    }
}
