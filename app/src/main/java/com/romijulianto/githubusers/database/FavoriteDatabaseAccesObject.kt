package com.romijulianto.githubusers.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDatabaseAccessObject {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(fav: FavoriteDatabase)

    @Update
    fun update(fav: FavoriteDatabase)

    @Query("DELETE from FavoriteDatabase where login =:login")
    fun delete(login: String)

    @Query("SELECT * from FavoriteDatabase ORDER BY id ASC")
    fun getAllFavorites(): LiveData<List<FavoriteDatabase>>

    @Query("SELECT EXISTS (SELECT * from FavoriteDatabase where login =:login)")
    fun checkUserFavorites(login: String): Boolean
}