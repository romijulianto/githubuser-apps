package com.romijulianto.githubusers.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.romijulianto.githubusers.R
import com.romijulianto.githubusers.api.ApiConfig
import com.romijulianto.githubusers.userdata.GithubUsersArray
import com.romijulianto.githubusers.userdata.GithubUsersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivityViewModel : ViewModel() {

    private val _githubUserArray = MutableLiveData<ArrayList<GithubUsersArray>>()
    val githubUserArray: LiveData<ArrayList<GithubUsersArray>> = _githubUserArray

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isToast = MutableLiveData<Boolean>()
    val isToast: LiveData<Boolean> = _isToast

    private val _toastReason = MutableLiveData<String>()
    val toastReason: LiveData<String> = _toastReason

    fun findGitHubUserID(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserQuery(query)
        client.enqueue(object : Callback<GithubUsersResponse> {
            override fun onResponse(
                call: Call<GithubUsersResponse>,
                response: Response<GithubUsersResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body()?.items?.isEmpty() == true) {
                        _isToast.value = false
                        _toastReason.value = R.string.search_result_empty.toString()
                    } else {
                        _isToast.value = true
                        _githubUserArray.value = response.body()?.items
                    }
                } else {
                    _isToast.value = false
                    _toastReason.value = R.string.on_failure.toString() + " " + response.message()
                    Log.e(TAG, R.string.on_failure.toString() + " " + response.message())
                }
            }

            override fun onFailure(call: Call<GithubUsersResponse>, t: Throwable) {
                _isLoading.value = false
                _isToast.value = false
                _toastReason.value = R.string.on_failure.toString() + " " + t.message.toString()
                Log.e(TAG, R.string.on_failure.toString() + " " + t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}