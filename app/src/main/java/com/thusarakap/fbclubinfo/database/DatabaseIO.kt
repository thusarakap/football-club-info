package com.thusarakap.fbclubinfo.database

import android.content.Context
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room

// Database I/O operations object
object DatabaseIO {
    // DAO instances to access league and club tables
    private lateinit var leagueDao: LeagueDao
    private lateinit var clubDao: ClubDao

    // Function to initialize database
    fun initialize(context: Context) {
        // Create Room database instance
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "fb-club-info-db"
        ).build()
        // Initialize DAO instances
        leagueDao = db.leagueDao()
        clubDao = db.clubDao()
    }

    // Function to add list of leagues to the database
    suspend fun addLeaguesToDB(leagues: List<League>) {
        for (league in leagues) {
            // Insert each league into the leagues table
            leagueDao.insertLeague(league)
            // Log the added league
            Log.d("DatabaseIO", "Added league: $league")
        }
    }

    // Function to add list of clubs to the database
    suspend fun addClubsToDB(clubs: List<Club>) {
        for (club in clubs) {
            // Insert each club into the clubs table
            clubDao.insertClub(club)
            // Log the added club
            Log.d("DatabaseIO", "Added club: $club")
        }
    }

    // Function to search clubs by name or league
    suspend fun searchClubsByNameOrLeague(query: String): List<Club> {
        // Run a case-insensitive search for clubs
        return clubDao.searchByNameOrLeague("%${query.toLowerCase()}%")
    }
}

// Entity class representing a league in the database
@Entity(tableName = "leagues")
data class League(
    // Primary key for the league
    @PrimaryKey val idLeague: Int,
    @ColumnInfo(name = "League") val strLeague: String,
    @ColumnInfo(name = "Sport") val strSport: String,
    @ColumnInfo(name = "AltName") val strLeagueAlternate: String
)

// Entity class representing a club in the database
@Entity(tableName = "clubs")
data class Club(
    // Primary key for the club
    @PrimaryKey val idTeam: String,
    val name: String,
    val strTeamShort: String,
    val strAlternate: String,
    val intFormedYear: String,
    val strLeague: String,
    val idLeague: String,
    val strStadium: String,
    val strKeywords: String,
    val strStadiumThumb: String,
    val strStadiumLocation: String,
    val intStadiumCapacity: String,
    val strWebsite: String,
    val strTeamJersey: String,
    val strTeamLogo: String
)

// DAO interface for accessing league database operations
@Dao
interface LeagueDao {
    // Function to insert a league into the leagues table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeague(league: League)

    // Function to retrieve leagues list from the leagues table
    @Query("SELECT * FROM leagues")
    suspend fun getAllLeagues(): List<League>
}

// DAO interface for accessing club database operations
@Dao
interface ClubDao {
    // Function to insert a club into the clubs table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClub(club: Club)

    // Function to search clubs by name or league
    @Query("SELECT * FROM clubs WHERE LOWER(name) LIKE :query OR LOWER(strLeague) LIKE :query")
    suspend fun searchByNameOrLeague(query: String): List<Club>
}
