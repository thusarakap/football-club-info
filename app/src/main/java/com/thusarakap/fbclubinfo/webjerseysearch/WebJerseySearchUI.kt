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

@Composable
fun WebJerseySearchUI(navController: NavController) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var matchingTeams by remember { mutableStateOf<List<TeamWithJersey>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(state = scrollState)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        Text("Search for Jerseys Online", fontWeight = FontWeight.Bold)

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

