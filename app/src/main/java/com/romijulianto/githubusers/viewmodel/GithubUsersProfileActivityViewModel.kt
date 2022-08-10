package com.romijulianto.githubusers.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.romijulianto.githubusers.api.ApiConfig
import com.romijulianto.githubusers.database.FavoriteDatabase
import com.romijulianto.githubusers.database.FavoriteRoomDB
import com.romijulianto.githubusers.userdata.GithubUsersJSON
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubUsersProfileActivityViewModel : ViewModel() {

    private val _githubUserProfileJSON = MutableLiveData<GithubUsersJSON>()
    val githubUserProfileJSON: LiveData<GithubUsersJSON> = _githubUserProfileJSON

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isToast = MutableLiveData<Boolean>()
    val isToast: LiveData<Boolean> = _isToast

    private val _toastReason = MutableLiveData<String>()
    val toastReason: LiveData<String> = _toastReason

    fun getGitHubUserData(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(query)
        client.enqueue(object : Callback<GithubUsersJSON> {
            override fun onResponse(
                call: Call<GithubUsersJSON>,
                response: Response<GithubUsersJSON>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _isLoading.value = false
                        _githubUserProfileJSON.value = response.body()
                    }
                } else {
                    _isLoading.value = true
                    _isToast.value = false
                    _toastReason.value = "onFailure: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubUsersJSON>, t: Throwable) {
                _isLoading.value = true
                _isToast.value = false
                _toastReason.value = "onFailure: ${t.message}"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun insertUserFavourite(
        userId: String,
        userAvatar: String,
        userHtml: String,
        context: Context
    ) {
        _isLoading.value = true
        val githubUserDBFavourite = FavoriteRoomDB.getDatabase(context).favoriteDao()
        val inputFavData = FavoriteDatabase(login = userId, avatarUrl = userAvatar, htmlUrl = userHtml)
        if (inputFavData.id.toString().isEmpty()) {
            _isLoading.value = true
        } else {
            githubUserDBFavourite.insert(inputFavData)
        }
        _isLoading.value = false
    }

    fun deleteUserFavourite(userId: String, context: Context) {
        _isLoading.value = true
        val githubUserDBFavourite = FavoriteRoomDB.getDatabase(context).favoriteDao()
        githubUserDBFavourite.delete(userId)
        _isLoading.value = false
    }

    companion object {
        private val TAG = GithubUsersProfileActivityViewModel::class.java.simpleName
    }
}