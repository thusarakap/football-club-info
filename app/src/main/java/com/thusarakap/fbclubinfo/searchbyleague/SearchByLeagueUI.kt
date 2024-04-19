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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.thusarakap.fbclubinfo.database.Club
import com.thusarakap.fbclubinfo.database.DatabaseIO
import kotlinx.coroutines.launch

@Composable
fun SearchByLeagueUI(navController: NavController) {
    var leagueName by rememberSaveable { mutableStateOf("") }
    var clubDetails by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(state = scrollState)
            .fillMaxSize(),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Search by League")

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = leagueName,
            onValueChange = { leagueName = it },
            label = { Text("Enter League Name") },
            modifier = Modifier.width(250.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    clubDetails = searchClubsByLeague(leagueName)
                }
            },
            modifier = Modifier.width(250.dp)
        ) {
            Text("Retrieve Clubs")
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    val clubs = clubDetails.split("\n\n").map { clubDetail ->
                        val details = clubDetail.split("\n").map { it.split(": ").last() }
                        Club(
                            idTeam = details[0],
                            name = details[1],
                            strTeamShort = details[2],
                            strAlternate = details[3],
                            intFormedYear = details[4],
                            strLeague = details[5],
                            idLeague = details[6],
                            strStadium = details[7],
                            strKeywords = details[8],
                            strStadiumThumb = details[9],
                            strStadiumLocation = details[10],
                            intStadiumCapacity = details[11],
                            strWebsite = details[12],
                            strTeamJersey = details[13],
                            strTeamLogo = details[14]
                        )
                    }
                    DatabaseIO.addClubsToDB(clubs)
                }
            },
            modifier = Modifier.width(250.dp)
        ) {
            Text("Save clubs to Database")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = clubDetails,
            modifier = Modifier.fillMaxWidth()
                .padding(30.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SearchByLeagueUIPreview() {
    val navController = rememberNavController()
    SearchByLeagueUI(navController)
}