package com.karipidis.droplet.data.remote.responses

import com.google.gson.annotations.SerializedName

data class RemoteUser(
    @SerializedName("userId")
    val id: String,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val avatar: String?
)