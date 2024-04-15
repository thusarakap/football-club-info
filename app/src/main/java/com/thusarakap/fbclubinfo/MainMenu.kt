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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object MainMenu {
    const val Screen1Route = "Screen1"
    const val Screen2Route = "Screen2"
    const val Screen3Route = "Screen3"
}

@Composable
fun MainMenu(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate(MainMenu.Screen1Route) },
            modifier = Modifier.width(250.dp)) {
            Text("Add Leagues to DB")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(MainMenu.Screen2Route) },
            modifier = Modifier.width(250.dp)) {
            Text("Search for Clubs By League")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate(MainMenu.Screen3Route) },
            modifier = Modifier.width(250.dp)) {
            Text("Search for Clubs")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainMenuPreview() {
    val navController = rememberNavController()
    MainMenu(navController)
}