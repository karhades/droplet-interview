package com.karipidis.droplet.data.remote.responses

import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(
    @SerializedName("user")
    val remoteUser: RemoteUser
)