package com.thusarakap.fbclubinfo.webjerseysearch

import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun WebJerseySearchUI(navController: NavController) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var matchingTeams by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Search for Jerseys Online")

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Enter Club Name or League") }
        )

        Button(
            onClick = {
                // Perform search when button is clicked
                SearchJerseysTask(searchText) { teams ->
                    matchingTeams = teams
                }.execute()
            },
            modifier = Modifier.width(250.dp)
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display matching team names
        Column {
            matchingTeams.forEach { team ->
                Text("${team.first} (ID: ${team.second})")
            }
        }
    }
}


//@Composable
//fun rememberImagePainter(url: String): BitmapPainter {
//    return remember(url) {
//        val imageUrl = URL(url)
//        val connection = imageUrl.openConnection() as HttpURLConnection
//        connection.doInput = true
//        connection.connect()
//        val inputStream = connection.inputStream
//        val bitmap = BitmapFactory.decodeStream(inputStream)
//        inputStream.close()
//        BitmapPainter(bitmap)
//    }
//}

class SearchJerseysTask(
    private val query: String,
    private val callback: (List<Pair<String, String>>) -> Unit
) : AsyncTask<Void, Void, List<Pair<String, String>>>() {

    override fun doInBackground(vararg params: Void?): List<Pair<String, String>> {
        val matchingTeams = mutableListOf<Pair<String, String>>()

        try {
            // Step 1: Retrieve all countries
            val countriesUrl = "https://www.thesportsdb.com/api/v1/json/3/all_countries.php"
            val countriesResponse = URL(countriesUrl).readText()
            val countries = JSONObject(countriesResponse).getJSONArray("countries")

            for (i in 0 until countries.length()) {
                val country = countries.getJSONObject(i)
                val countryName = country.getString("name_en")

                // Step 2: Search for teams by country and sport
                val teamsUrl = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?s=Soccer&c=$countryName"
                val teamsResponse = URL(teamsUrl).readText()

                val teamsJson = JSONObject(teamsResponse)
                val teamsArray = teamsJson.optJSONArray("teams")

                if (teamsArray != null) {
                    for (j in 0 until teamsArray.length()) {
                        val team = teamsArray.getJSONObject(j)
                        val teamName = team.getString("strTeam")

                        // Check if the team name contains the query string
                        if (teamName.contains(query, ignoreCase = true)) {
                            val teamId = team.getString("idTeam")
                            matchingTeams.add(Pair(teamName, teamId))
                        }
                    }
                } else {
                    Log.e(TAG, "Teams response is null for country: $countryName")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error occurred during background task", e)
        }

        return matchingTeams
    }

    companion object {
        private const val TAG = "SearchJerseysTask"
    }

    override fun onPostExecute(result: List<Pair<String, String>>) {
        super.onPostExecute(result)
        callback(result)
    }
}

