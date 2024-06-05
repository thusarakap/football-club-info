package com.thusarakap.fbclubinfo.database

import androidx.room.Database
import androidx.room.RoomDatabase

// Database class
@Database(entities = [League::class, Club::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    // Abstract function to provide access to LeagueDao
    abstract fun leagueDao(): LeagueDao
    // Abstract function to provide access to ClubDao
    abstract fun clubDao(): ClubDao
}
