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

object DatabaseIO {
    private lateinit var leagueDao: LeagueDao
    private lateinit var clubDao: ClubDao

    fun initialize(context: Context) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "fb-club-info-db"
        ).build()
        leagueDao = db.leagueDao()
        clubDao = db.clubDao()
    }

    suspend fun addLeaguesToDB(leagues: List<League>) {
        for (league in leagues) {
            leagueDao.insertLeague(league)
            Log.d("DatabaseIO", "Added league: $league")
        }
    }

    suspend fun addClubsToDB(clubs: List<Club>) {
        for (club in clubs) {
            clubDao.insertClub(club)
            Log.d("DatabaseIO", "Added club: $club")
        }
    }

    suspend fun searchClubsByNameOrLeague(query: String): List<Club> {
        return clubDao.searchByNameOrLeague("%${query.toLowerCase()}%")
    }
}

@Entity(tableName = "leagues")
data class League(
    @PrimaryKey val idLeague: Int,
    @ColumnInfo(name = "League") val strLeague: String,
    @ColumnInfo(name = "Sport") val strSport: String,
    @ColumnInfo(name = "AltName") val strLeagueAlternate: String
)

@Entity(tableName = "clubs")
data class Club(
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

@Dao
interface LeagueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeague(league: League)

    @Query("SELECT * FROM leagues")
    suspend fun getAllLeagues(): List<League>
}

@Dao
interface ClubDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClub(club: Club)

    @Query("SELECT * FROM clubs WHERE LOWER(name) LIKE :query OR LOWER(strLeague) LIKE :query")
    suspend fun searchByNameOrLeague(query: String): List<Club>
}