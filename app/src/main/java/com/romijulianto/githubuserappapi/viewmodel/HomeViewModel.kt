package com.romijulianto.githubuserappapi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.romijulianto.githubuserappapi.Utils.Companion.TOKEN
import com.romijulianto.githubuserappapi.model.ResponseSearch
import com.romijulianto.githubuserappapi.model.SimpleUser
import com.romijulianto.githubuserappapi.repository.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel: ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData(false)
    val isError: LiveData<Boolean> = _isError

    private val _simpleUsers = MutableLiveData<ArrayList<SimpleUser>>()
    val simpleUsers: LiveData<ArrayList<SimpleUser>> = _simpleUsers

    init {
        findUser("\"\"")
    }

    fun findUser(query: String) {
        _isLoading.value = true

        ApiConfig.getApiService().searchUsername(token = "Bearer $TOKEN", query).apply {
            enqueue(object : Callback<ResponseSearch> {
                override fun onResponse(
                    call: Call<ResponseSearch>,
                    response: Response<ResponseSearch>
                ) {
                    if (response.isSuccessful) _simpleUsers.value = response.body()?.items
                    else Log.e(TAG, response.message())

                    _isLoading.value = false
                    _isError.value = false
                }

                override fun onFailure(call: Call<ResponseSearch>, t: Throwable) {
                    Log.e(TAG, t.message.toString())

                    _simpleUsers.value = arrayListOf()
                    _isError.value = true
                    _isLoading.value = false
                }

            })
        }
    }

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
    }

}