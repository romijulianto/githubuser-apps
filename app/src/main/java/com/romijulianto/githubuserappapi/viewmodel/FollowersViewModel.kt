package com.romijulianto.githubuserappapi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.romijulianto.githubuserappapi.Utils
import com.romijulianto.githubuserappapi.model.SimpleUser
import com.romijulianto.githubuserappapi.repository.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _followers = MutableLiveData<ArrayList<SimpleUser>?>(null)
    val followers: LiveData<ArrayList<SimpleUser>?> = _followers

    fun getUserFollowers(username: String) {
        _isLoading.value = true

        ApiConfig.getApiService().getUserFollowers(token = "Bearer ${Utils.TOKEN}", username)
            .apply {
                enqueue(object : Callback<ArrayList<SimpleUser>> {
                    override fun onResponse(
                        call: Call<ArrayList<SimpleUser>>,
                        response: Response<ArrayList<SimpleUser>>
                    ) {
                        if (response.isSuccessful) _followers.value = response.body()
                        else Log.e(TAG, response.message())
                        _isLoading.value = false
                    }

                    override fun onFailure(call: Call<ArrayList<SimpleUser>>, t: Throwable) {
                        Log.e(TAG, t.message.toString())

                        _followers.value = arrayListOf()
                        _isLoading.value = false
                    }

                })
            }
    }

    companion object {
        private val TAG = FollowersViewModel::class.java.simpleName
    }

}