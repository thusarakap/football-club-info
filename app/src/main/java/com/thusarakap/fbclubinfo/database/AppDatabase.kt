package com.thusarakap.fbclubinfo.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [League::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun leagueDao(): LeagueDao
}