package com.romijulianto.githubusers.api

import com.romijulianto.githubusers.BuildConfig
import com.romijulianto.githubusers.userdata.GithubUsersArray
import com.romijulianto.githubusers.userdata.GithubUsersJSON
import com.romijulianto.githubusers.userdata.GithubUsersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    @Headers("Authorization: token $apiKEY")
    fun getUserQuery(
        @Query("q") username: String
    ): Call<GithubUsersResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $apiKEY")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<GithubUsersJSON>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $apiKEY")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<GithubUsersArray>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $apiKEY")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<ArrayList<GithubUsersArray>>

    companion object {
        const val apiKEY: String = BuildConfig.API_KEY
    }
}