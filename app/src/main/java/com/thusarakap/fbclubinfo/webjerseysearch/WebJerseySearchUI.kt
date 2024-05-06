package com.thusarakap.fbclubinfo.webjerseysearch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// API jersey search UI
@Composable
fun WebJerseySearchUI(navController: NavController) {
    // Variables for search and teams matching search
    var searchText by rememberSaveable { mutableStateOf("") }
    var matchingTeams by rememberSaveable { mutableStateOf<List<TeamWithJersey>>(emptyList()) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Column layout
    Column(
        modifier = Modifier
            .verticalScroll(state = scrollState)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Add space at the top
        Spacer(modifier = Modifier.height(60.dp))

        Text("Search for Jerseys Online", fontWeight = FontWeight.Bold)

        // Spacer
        Spacer(modifier = Modifier.height(15.dp))

        // Search box
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it }, // Update search text on value change
            label = { Text("Enter Club Name or League") }
        )

        // Spacer
        Spacer(modifier = Modifier.height(15.dp))

        // Search button
        Button(
            onClick = {
                // Set loading indicator to true before initiating search
                isLoading = true
                // Initiate search
                searchJerseys(searchText) { teams ->
                    matchingTeams = teams
                    // Set loading indicator to false after search completes
                    isLoading = false
                }
            },
            // Set button width
            modifier = Modifier.width(250.dp)
        ) {
            Text("Search")
        }

        // Spacer
        Spacer(modifier = Modifier.height(16.dp))

        // Display loading indicator if searching
        if (isLoading) {
            // Spacer for loading circle
            Spacer(modifier = Modifier.height(250.dp))
            // Display loading circle
            CircularProgressIndicator(modifier = Modifier.size(50.dp)) // Display circular progress indicator
        } else {
            // Display matching team names, IDs, and jersey thumbnails
            Column {
                // Iterate over matching teams and display jersey
                matchingTeams.forEach { team ->
                    Spacer(modifier = Modifier.width(20.dp))
                    // Display team name
                    Text(team.teamName)
                    Row {
                        // Display jersey thumbnails
                        team.jerseyImageUrls.forEach { imageUrl ->
                            LoadImageFromUrl(imageUrl = imageUrl)
                        }
                    }
                }
            }
        }
    }
}