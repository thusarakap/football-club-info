package com.thusarakap.fbclubinfo.webjerseysearch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

// Function to search for jerseys and return the results
fun searchJerseys(query: String, callback: (List<TeamWithJersey>) -> Unit) {
    // Run the coroutine in a background thread
    CoroutineScope(Dispatchers.IO).launch {
        // Initialize a list to store matching teams and jerseys
        val matchingTeams = mutableListOf<TeamWithJersey>()
        try {
            // Step 1: Retrieve all countries
            val countriesUrl = "https://www.thesportsdb.com/api/v1/json/3/all_countries.php"
            Log.d(TAG, "Fetching countries from: $countriesUrl")
            val countriesResponse = URL(countriesUrl).readText()
            val countries = JSONObject(countriesResponse).getJSONArray("countries")

            // Iterate through each country
            for (i in 0 until countries.length()) {
                val country = countries.getJSONObject(i)
                val countryName = country.getString("name_en")

                // Step 2: Search for teams by country and sport
                val teamsUrl = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?s=Soccer&c=$countryName"
                Log.d(TAG, "Fetching teams for country: $countryName from: $teamsUrl")
                val teamsResponse = URL(teamsUrl).readText()

                // Parse the response
                val teamsJson = JSONObject(teamsResponse)
                val teamsArray = teamsJson.optJSONArray("teams")

                // Get jersey URLs from teams, if not null
                if (teamsArray != null) {
                    // Iterate through teams
                    for (j in 0 until teamsArray.length()) {
                        val team = teamsArray.getJSONObject(j)
                        val teamName = team.getString("strTeam")

                        // Check if the team name contains the search String
                        if (teamName.contains(query, ignoreCase = true)) {
                            val teamId = team.getString("idTeam")
                            val jerseyImageUrls = fetchJerseyImageUrls(teamId)
                            matchingTeams.add(TeamWithJersey(teamName, jerseyImageUrls))
                        }
                    }
                } else {
                    Log.e(TAG, "Teams response is null for country: $countryName")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error occurred during background task", e)
        }

        // Update UI with the matching teams
        withContext(Dispatchers.Main) {
            callback(matchingTeams)
        }
    }
}

// Function to fetch jersey image URLs for team IDs
private fun fetchJerseyImageUrls(teamId: String): List<String> {
    val jerseyUrl = "https://www.thesportsdb.com/api/v1/json/3/lookupequipment.php?id=$teamId"
    Log.d(TAG, "Fetching jersey image URLs from: $jerseyUrl")
    val jerseyResponse = URL(jerseyUrl).readText()
    val jerseysJson = JSONObject(jerseyResponse).optJSONArray("equipment")
    val jerseyUrls = mutableListOf<String>()
    jerseysJson?.let { jsonArray ->
        // Iterate through the JSON array and extract image URLs
        for (i in 0 until jsonArray.length()) {
            val jersey = jsonArray.getJSONObject(i)
            val imageUrl = jersey.getString("strEquipment")
            jerseyUrls.add(imageUrl)
        }
    }
    return jerseyUrls
}

// Data class to hold team names and jersey image URLs
data class TeamWithJersey(val teamName: String, val jerseyImageUrls: List<String>)

// Constant for logging
private const val TAG = "WebJerseySearch"

// Composable function to load an image from a URL and display it
@Composable
fun LoadImageFromUrl(imageUrl: String) {
    // State variable to hold the loaded image bitmap
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    // Launched effect to load the image asynchronously
    LaunchedEffect(imageUrl) {
        try {
            // Fetch the image bitmap from the URL in a background thread
            val bitmap = withContext(Dispatchers.IO) {
                fetchImageBitmap(imageUrl)
            }
            // Update the image bitmap state when it's loaded
            imageBitmap = bitmap?.asImageBitmap()
        } catch (e: Exception) {
            // Error Handler
            e.printStackTrace()
        }
    }

    // Display the image if bitmap is not null
    imageBitmap?.let {
        Image(
            painter = remember { BitmapPainter(it) },
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )
    }
}

// Function to fetch image bitmap from URL
private suspend fun fetchImageBitmap(url: String): Bitmap? {
    return try {
        // Open input from URL and decode into a bitmap
        val inputStream = URL(url).openStream()
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        null
    }
}
