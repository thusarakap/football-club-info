package com.thusarakap.fbclubinfo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.thusarakap.fbclubinfo.database.SaveInitialLeagues

// MainMenu object containing routes
object MainMenu {
    const val MainMenuRoute = "MainMenu"
    const val SearchByLeagueUIRoute = "SearchByLeagueUI"
    const val SearchForClubsUIRoute = "SearchForClubsUI"
    const val WebJerseySearchUIRoute = "WebJerseySearchUI"
}

// Main menu UI
@Composable
fun MainMenu(navController: NavController) {
    // Column layout
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Name
        Text("Football Club Info", fontWeight = FontWeight.Bold, fontSize = 32.sp)

        // Spacer
        Spacer(modifier = Modifier.height(120.dp))

        // Add Leagues to DB button
        Button(
            onClick = {SaveInitialLeagues()},
            modifier = Modifier.width(250.dp)) {
            Text("Add Leagues to DB")
        }

        // Spacer
        Spacer(modifier = Modifier.height(16.dp))

        // Search for Clubs By League button
        Button(
            onClick = { navController.navigate(MainMenu.SearchByLeagueUIRoute) },
            modifier = Modifier.width(250.dp)) {
            Text("Search for Clubs By League")
        }

        // Spacer
        Spacer(modifier = Modifier.height(16.dp))

        // Search for Clubs button
        Button(
            onClick = { navController.navigate(MainMenu.SearchForClubsUIRoute) },
            modifier = Modifier.width(250.dp)) {
            Text("Search for Clubs")
        }

        // Spacer
        Spacer(modifier = Modifier.height(16.dp))

        // Search for Jerseys Online button
        Button(
            onClick = { navController.navigate(MainMenu.WebJerseySearchUIRoute) },
            modifier = Modifier.width(250.dp)) {
            Text("Search for Jerseys Online")
        }
    }
}

// Main menu preview
@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    val navController = rememberNavController()
    MainMenu(navController)
}
