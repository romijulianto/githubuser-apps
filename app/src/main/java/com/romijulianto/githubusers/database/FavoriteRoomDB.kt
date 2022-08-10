package com.romijulianto.githubusers.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteDatabase::class], version = 1)
abstract class FavoriteRoomDB : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDatabaseAccessObject

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRoomDB? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteRoomDB {
            if (INSTANCE == null) {
                synchronized(FavoriteRoomDB::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteRoomDB::class.java, "favourite_database"
                    )
                        .allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE as FavoriteRoomDB
        }
    }
}