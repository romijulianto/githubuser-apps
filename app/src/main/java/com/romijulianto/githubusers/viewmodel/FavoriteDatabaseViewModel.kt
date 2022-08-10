package com.romijulianto.githubusers.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.romijulianto.githubusers.database.FavoriteDatabase
import com.romijulianto.githubusers.database.FavoriteRoomDB

class FavoriteDatabaseViewModel : ViewModel() {

    private lateinit var githubUserFavouriteList: LiveData<List<FavoriteDatabase>>
    private lateinit var githubUserFavouriteResult: LiveData<List<FavoriteDatabase>>

    fun getGitHubUserFavoriteData(context: Context): LiveData<List<FavoriteDatabase>> {
        val database = FavoriteRoomDB.getDatabase(context.applicationContext).favoriteDao()

        githubUserFavouriteList = database.getAllFavorites().map { data ->
            data.reversed().map { FavoriteDatabase(it.id, it.login, it.htmlUrl, it.avatarUrl) }
        }

        githubUserFavouriteResult = githubUserFavouriteList
        return githubUserFavouriteResult
    }
}