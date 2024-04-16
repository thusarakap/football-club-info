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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        DatabaseIO.initialize(this)

        setContent {
            FbClubInfoTheme {
                // Create a NavController
                val navController = rememberNavController()

                // Define your NavHost
                NavHost(navController = navController, startDestination = MainMenu.MainMenuRoute) {
                    composable(MainMenu.MainMenuRoute) { MainMenu(navController) }
                    composable(MainMenu.SearchByLeagueUIRoute) { SearchByLeagueUI(navController) }
                }
            }
        }
    }
}

