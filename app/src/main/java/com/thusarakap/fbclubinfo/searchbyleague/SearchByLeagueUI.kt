package com.thusarakap.fbclubinfo.searchbyleague

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.thusarakap.fbclubinfo.database.saveClubsToDatabase
import kotlinx.coroutines.launch

// Search By League UI
@Composable
fun SearchByLeagueUI(navController: NavController) {
    // Variables for league name and club details
    var leagueName by rememberSaveable { mutableStateOf("") }
    var clubDetails by rememberSaveable { mutableStateOf("") }
    // Coroutine scope
    val coroutineScope = rememberCoroutineScope()

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
        Spacer(modifier = Modifier.height(60.dp))

        // Title
        Text("Search by League", fontWeight = FontWeight.Bold)

        // Spacer
        Spacer(modifier = Modifier.height(15.dp))

        // Search box
        OutlinedTextField(
            value = leagueName,
            onValueChange = { leagueName = it }, // Update league name on value change
            label = { Text("Enter League Name") },
            modifier = Modifier.width(250.dp) // Set text field width
        )

        // Spacer
        Spacer(modifier = Modifier.height(20.dp))

        // Search button
        Button(
            onClick = {
                // Launch a coroutine to search
                coroutineScope.launch {
                    clubDetails = searchClubsByLeague(leagueName)
                }
            },
            // Set button width
            modifier = Modifier.width(250.dp)
        ) {
            Text("Retrieve Clubs")
        }

        // Save button
        Button(
            onClick = {
                // Launch a coroutine to save
                coroutineScope.launch {
                    saveClubsToDatabase(clubDetails)
                }
            },
            modifier = Modifier.width(250.dp)
        ) {
            Text("Save clubs to Database")
        }

        // Spacer
        Spacer(modifier = Modifier.height(20.dp))

        // Display club details
        Text(
            text = clubDetails,
            modifier = Modifier.fillMaxWidth()
                .padding(30.dp)
        )
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun SearchByLeagueUIPreview() {
    val navController = rememberNavController()
    SearchByLeagueUI(navController)
}
