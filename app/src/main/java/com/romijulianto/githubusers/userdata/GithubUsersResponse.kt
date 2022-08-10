package com.romijulianto.githubusers.userdata

import com.google.gson.annotations.SerializedName

data class GithubUsersResponse(

    @field:SerializedName("items")
    val items: ArrayList<GithubUsersArray>,
)