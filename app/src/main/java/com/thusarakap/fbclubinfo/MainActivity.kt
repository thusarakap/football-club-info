package com.thusarakap.fbclubinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thusarakap.fbclubinfo.database.DatabaseIO
import com.thusarakap.fbclubinfo.ui.theme.FbClubInfoTheme
import com.thusarakap.fbclubinfo.searchbyleague.SearchByLeagueUI
import com.thusarakap.fbclubinfo.searchforclubs.SearchForClubsUI
import com.thusarakap.fbclubinfo.webjerseysearch.WebJerseySearchUI

// MainActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // Initialize the database
        DatabaseIO.initialize(this)

        setContent {
            FbClubInfoTheme {
                // NavController
                val navController = rememberNavController()

                // Define your NavHost with composable destinations
                NavHost(navController = navController, startDestination = MainMenu.MainMenuRoute) {
                    // Main menu screen
                    composable(MainMenu.MainMenuRoute) { MainMenu(navController) }
                    // Search clubs by league screen
                    composable(MainMenu.SearchByLeagueUIRoute) { SearchByLeagueUI(navController) }
                    // Search clubs screen
                    composable(MainMenu.SearchForClubsUIRoute) { SearchForClubsUI(navController) }
                    // Search jerseys online screen
                    composable(MainMenu.WebJerseySearchUIRoute) { WebJerseySearchUI(navController) }
                }
            }
        }
    }
}
