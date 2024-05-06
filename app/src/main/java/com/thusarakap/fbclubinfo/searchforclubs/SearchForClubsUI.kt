package com.thusarakap.fbclubinfo.searchforclubs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.thusarakap.fbclubinfo.database.Club
import com.thusarakap.fbclubinfo.database.DatabaseIO
import kotlinx.coroutines.launch
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun SearchForClubsUI(navController: NavController) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var searchResults by rememberSaveable { mutableStateOf(listOf<Club>()) }
    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(state = scrollState)
            .fillMaxSize(),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(65.dp))

        Text("Search For Clubs", fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Enter Club Name or League") },
            modifier = Modifier.width(300.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    searchResults = DatabaseIO.searchClubsByNameOrLeague(searchText)
                }
            },
            modifier = Modifier.width(250.dp)
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(20.dp))

        searchResults.forEach { club ->
            Text(
                text = club.name,
                modifier = Modifier.fillMaxWidth()
                    .padding(30.dp)
            )
            val bitmapState = rememberSaveable { mutableStateOf(null as android.graphics.Bitmap?) }
            coroutineScope.launch {
                bitmapState.value = loadImageFromURL(club.strTeamLogo)
            }
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

