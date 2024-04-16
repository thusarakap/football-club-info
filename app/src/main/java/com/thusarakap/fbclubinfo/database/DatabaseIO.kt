package com.thusarakap.fbclubinfo.database

import android.app.Application
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

    fun initialize(context: Context) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "fb-club-info-db"
        ).build()
        leagueDao = db.leagueDao()
    }

    suspend fun addLeaguesToDB(leagues: List<League>) {
        for (league in leagues) {
            leagueDao.insertLeague(league)
            Log.d("DatabaseIO", "Added league: $league")
        }
    }
}

@Entity(tableName = "leagues")
data class League(
    @PrimaryKey val idLeague: Int,
    @ColumnInfo(name = "League") val strLeague: String,
    @ColumnInfo(name = "Sport") val strSport: String,
    @ColumnInfo(name = "AltName") val strLeagueAlternate: String
)

@Dao
interface LeagueDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeague(league: League)

    @Query("SELECT * FROM leagues")
    suspend fun getAllLeagues(): List<League>
}