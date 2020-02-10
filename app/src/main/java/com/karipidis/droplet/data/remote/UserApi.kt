package com.karipidis.droplet.data.remote

import com.karipidis.droplet.data.remote.responses.GetUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("/getUser")
    suspend fun getUser(@Query("userId") userId: String): GetUserResponse
}