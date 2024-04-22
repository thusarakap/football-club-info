package com.thusarakap.fbclubinfo.webjerseysearch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

@Composable
fun WebJerseySearchUI(navController: NavController) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var matchingTeams by remember { mutableStateOf<List<TeamWithJersey>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        Text("Search for Jerseys Online")

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Enter Club Name or League") }
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                // Perform search when button is clicked
                isLoading = true
                searchJerseys(searchText) { teams ->
                    matchingTeams = teams
                    isLoading = false
                }
            },
            modifier = Modifier.width(250.dp)
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display loading indicator if search is in progress
        if (isLoading) {
            Spacer(modifier = Modifier.height(250.dp))
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        } else {
            // Display matching team names, IDs, and jersey thumbnails
            Column {
                matchingTeams.forEach { team ->
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(team.teamName)
                    Row {
                        team.jerseyImageUrls.forEach { imageUrl ->
                            LoadImageFromUrl(imageUrl = imageUrl)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoadImageFromUrl(imageUrl: String) {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(imageUrl) {
        try {
            val bitmap = withContext(Dispatchers.IO) {
                fetchImageBitmap(imageUrl)
            }
            imageBitmap = bitmap?.asImageBitmap()
        } catch (e: Exception) {
            // Handle errors, e.g., log or show a placeholder image
            e.printStackTrace()
        }
    }

    imageBitmap?.let {
        Image(
            painter = remember { BitmapPainter(it) },
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit
        )
    }
}

private suspend fun fetchImageBitmap(url: String): Bitmap? {
    return try {
        val inputStream = URL(url).openStream()
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        null
    }
}

private fun searchJerseys(query: String, callback: (List<TeamWithJersey>) -> Unit) {
    // Run the coroutine in a background thread
    CoroutineScope(Dispatchers.IO).launch {
        val matchingTeams = mutableListOf<TeamWithJersey>()
        try {
            // Step 1: Retrieve all countries
            val countriesUrl = "https://www.thesportsdb.com/api/v1/json/3/all_countries.php"
            Log.d(TAG, "Fetching countries from: $countriesUrl")
            val countriesResponse = URL(countriesUrl).readText()
            val countries = JSONObject(countriesResponse).getJSONArray("countries")

            for (i in 0 until countries.length()) {
                val country = countries.getJSONObject(i)
                val countryName = country.getString("name_en")

                // Step 2: Search for teams by country and sport
                val teamsUrl = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?s=Soccer&c=$countryName"
                Log.d(TAG, "Fetching teams for country: $countryName from: $teamsUrl")
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

        // Update UI on the main thread
        withContext(Dispatchers.Main) {
            callback(matchingTeams)
        }
    }
}

private fun fetchJerseyImageUrls(teamId: String): List<String> {
    val jerseyUrl = "https://www.thesportsdb.com/api/v1/json/3/lookupequipment.php?id=$teamId"
    Log.d(TAG, "Fetching jersey image URLs from: $jerseyUrl")
    val jerseyResponse = URL(jerseyUrl).readText()
    val jerseysJson = JSONObject(jerseyResponse).optJSONArray("equipment")
    val jerseyUrls = mutableListOf<String>()
    jerseysJson?.let { jsonArray ->
        for (i in 0 until jsonArray.length()) {
            val jersey = jsonArray.getJSONObject(i)
            val imageUrl = jersey.getString("strEquipment")
            jerseyUrls.add(imageUrl)
        }
    }
    return jerseyUrls
}

data class TeamWithJersey(val teamName: String, val jerseyImageUrls: List<String>)

private const val TAG = "WebJerseySearch"
