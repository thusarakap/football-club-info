package com.thusarakap.fbclubinfo.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Function to save initial hardcoded leagues to the database
fun SaveInitialLeagues() {
    // List of leagues with details
    val leagues = listOf(
        League(4328, "English Premier League", "Soccer", "Premier League, EPL"),
        League(4329, "English League Championship", "Soccer", "Championship"),
        League(4330, "Scottish Premier League", "Soccer", "Scottish Premiership, SPFL"),
        League(4331, "German Bundesliga", "Soccer", "Bundesliga, Fußball-Bundesliga"),
        League(4332, "Italian Serie A", "Soccer", "Serie A"),
        League(4334, "French Ligue 1", "Soccer", "Ligue 1 Conforama"),
        League(4335, "Spanish La Liga", "Soccer", "LaLiga Santander, La Liga"),
        League(4336, "Greek Superleague Greece", "Soccer", ""),
        League(4337, "Dutch Eredivisie", "Soccer", "Eredivisie"),
        League(4338, "Belgian First Division A", "Soccer", "Jupiler Pro League"),
        League(4339, "Turkish Super Lig", "Soccer", "Super Lig"),
        League(4340, "Danish Superliga", "Soccer", ""),
        League(4344, "Portuguese Primeira Liga", "Soccer", "Liga NOS"),
        League(4346, "American Major League Soccer", "Soccer", "MLS, Major League Soccer"),
        League(4347, "Swedish Allsvenskan", "Soccer", "Fotbollsallsvenskan"),
        League(4350, "Mexican Primera League", "Soccer", "Liga MX"),
        League(4351, "Brazilian Serie A", "Soccer", ""),
        League(4354, "Ukrainian Premier League", "Soccer", ""),
        League(4355, "Russian Football Premier League", "Soccer", "Чемпионат России по футболу"),
        League(4356, "Australian A-League", "Soccer", "A-League"),
        League(4358, "Norwegian Eliteserien", "Soccer", "Eliteserien"),
        League(4359, "Chinese Super League", "Soccer", "")
    )

    // Launch coroutine in IO context to add leagues to the database
    CoroutineScope(Dispatchers.IO).launch {
        DatabaseIO.addLeaguesToDB(leagues)
    }
}
