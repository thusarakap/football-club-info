package com.thusarakap.fbclubinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.thusarakap.fbclubinfo.database.SaveInitialLeagues

object MainMenu {
    const val MainMenuRoute = "MainMenu"
    const val SearchByLeagueUIRoute = "SearchByLeagueUI"
    const val SearchForClubsUIRoute = "SearchForClubsUI"
    const val WebJerseySearchUIRoute = "WebJerseySearchUI"
}

@Composable
fun MainMenu(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {SaveInitialLeagues()},
            modifier = Modifier.width(250.dp)) {
            Text("Add Leagues to DB")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(MainMenu.SearchByLeagueUIRoute) },
            modifier = Modifier.width(250.dp)) {
            Text("Search for Clubs By League")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(MainMenu.SearchForClubsUIRoute) },
            modifier = Modifier.width(250.dp)) {
            Text("Search for Clubs")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(MainMenu.WebJerseySearchUIRoute) },
            modifier = Modifier.width(250.dp)) {
            Text("Search for Jerseys Online")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    val navController = rememberNavController()
    MainMenu(navController)
}