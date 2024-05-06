package com.thusarakap.fbclubinfo.searchforclubs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.thusarakap.fbclubinfo.database.Club
import com.thusarakap.fbclubinfo.database.DatabaseIO
import kotlinx.coroutines.launch

// SearchForClubs UI
@Composable
fun SearchForClubsUI(navController: NavController) {
    // Variables for search and results
    var searchText by rememberSaveable { mutableStateOf("") }
    var searchResults by rememberSaveable { mutableStateOf(listOf<Club>()) }
    // Coroutine scope
    val coroutineScope = rememberCoroutineScope()
    // Remember scroll state
    val scrollState = rememberScrollState()

    // Column layout
    Column(
        modifier = Modifier
            .verticalScroll(state = scrollState)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Spacer
        Spacer(modifier = Modifier.height(65.dp))

        // Title
        Text("Search For Clubs", fontWeight = FontWeight.Bold)

        // Spacer
        Spacer(modifier = Modifier.height(12.dp))

        // Search box
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Enter Club Name or League") },
            modifier = Modifier.width(300.dp)
        )

        // Spacer
        Spacer(modifier = Modifier.height(20.dp))

        // Search button
        Button(
            onClick = {
                // Coroutine for the search
                coroutineScope.launch {
                    // Calling the database function to search clubs by name or league
                    searchResults = DatabaseIO.searchClubsByNameOrLeague(searchText)
                }
            },
            modifier = Modifier.width(250.dp) // Set button width
        ) {
            Text("Search")
        }

        // Spacer
        Spacer(modifier = Modifier.height(20.dp))

        // Display search results
        searchResults.forEach { club ->
            // Display club name
            Text(
                text = club.name,
                modifier = Modifier.fillMaxWidth()
                    .padding(30.dp)
            )
            // Load and display club logo from URL
            val bitmapState = rememberSaveable { mutableStateOf(null as android.graphics.Bitmap?) }
            coroutineScope.launch {
                // Coroutine to load club logo
                bitmapState.value = loadImageFromURL(club.strTeamLogo)
            }
            // Display club logo
            bitmapState.value?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Club Logo",
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}
