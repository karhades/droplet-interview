package com.karipidis.droplet.data.remote

import com.karipidis.droplet.data.remote.responses.GetUserResponse
import com.karipidis.droplet.data.remote.responses.RemoteUser
import com.karipidis.droplet.data.remote.responses.UpdateUserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserApi {

    @GET("/getUser")
    suspend fun getUser(@Query("userId") userId: String): GetUserResponse

    @PUT("/updateUser")
    suspend fun updateUser(@Body remoteUser: RemoteUser): UpdateUserResponse
}