package com.thusarakap.fbclubinfo.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [League::class, Club::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun leagueDao(): LeagueDao
    abstract fun clubDao(): ClubDao
}
