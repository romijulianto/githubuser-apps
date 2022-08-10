package com.romijulianto.githubusers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.romijulianto.githubusers.R
import com.romijulianto.githubusers.api.ApiConfig
import com.romijulianto.githubusers.userdata.GithubUsersArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowFragmentViewModel : ViewModel() {
    private val _githubUserFollowArray = MutableLiveData<ArrayList<GithubUsersArray>>()
    val githubUserFollowArray: LiveData<ArrayList<GithubUsersArray>> = _githubUserFollowArray

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isToast = MutableLiveData<Boolean>()
    val isToast: LiveData<Boolean> = _isToast

    private val _toastReason = MutableLiveData<String>()
    val toastReason: LiveData<String> = _toastReason

    fun getGitHubUserFollowersData(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(query)
        client.enqueue(object : Callback<ArrayList<GithubUsersArray>> {
            override fun onResponse(
                call: Call<ArrayList<GithubUsersArray>>,
                response: Response<ArrayList<GithubUsersArray>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body()?.isEmpty() == true) {
                        _isToast.value = false
                        _toastReason.value = R.string.followers_empty_list.toString()
                    } else {
                        _isToast.value = true
                    }
                    _githubUserFollowArray.value = response.body()
                } else {
                    _isToast.value = false
                    _toastReason.value = "onFailure: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<GithubUsersArray>>, t: Throwable) {
                _isLoading.value = false
                _isToast.value = false
                _toastReason.value = "onFailure: ${t.message.toString()}"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getGitHubUserFollowingData(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(query)
        client.enqueue(object : Callback<ArrayList<GithubUsersArray>> {
            override fun onResponse(
                call: Call<ArrayList<GithubUsersArray>>,
                response: Response<ArrayList<GithubUsersArray>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body()?.isEmpty() == true) {
                        _isToast.value = false
                        _toastReason.value = R.string.following_empty_list.toString()
                    } else {
                        _isToast.value = true
                    }
                    _githubUserFollowArray.value = response.body()
                } else {
                    _isToast.value = false
                    _toastReason.value = "onFailure: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<GithubUsersArray>>, t: Throwable) {
                _isLoading.value = false
                _isToast.value = false
                _toastReason.value = "onFailure: ${t.message.toString()}"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "FollowViewModel"
    }
}