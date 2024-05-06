package com.thusarakap.fbclubinfo.searchbyleague

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import org.json.JSONObject

suspend fun searchClubsByLeague(leagueName: String): String {
    return withContext(Dispatchers.IO) {
        val url = URL("https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l=$leagueName")
        val connection = url.openConnection() as HttpsURLConnection
        try {
            connection.connect()
            if (connection.responseCode == HttpsURLConnection.HTTP_OK) {
                val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonObject = JSONObject(jsonString)

                if (!jsonObject.isNull("teams")) {
                    val teamsArray = jsonObject.getJSONArray("teams")
                    val parsedTeams = mutableListOf<String>()

                    for (i in 0 until teamsArray.length()) {
                        val teamObject = teamsArray.getJSONObject(i)

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

                        parsedTeams.add(parsedTeam)
                    }

                    parsedTeams.joinToString("\n\n")
                } else {
                    "No teams found for league: $leagueName"
                }
            } else {
                "Error: ${connection.responseMessage}"
            }
        } finally {
            connection.disconnect()
        }
    }
}