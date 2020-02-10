package com.karipidis.droplet.data.remote.responses

import com.google.gson.annotations.SerializedName

data class GetUserResponse(
    @SerializedName("user")
    val remoteUser: RemoteUser
)