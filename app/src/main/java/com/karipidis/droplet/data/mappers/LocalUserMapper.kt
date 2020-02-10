package com.karipidis.droplet.data.mappers

import com.karipidis.droplet.data.local.LocalUser
import com.karipidis.droplet.data.remote.responses.RemoteUser

class LocalUserMapper {

    fun map(remoteUser: RemoteUser): LocalUser {
        return LocalUser(
            id = remoteUser.id,
            firstName = remoteUser.firstName ?: "",
            lastName = remoteUser.lastName ?: "",
            email = remoteUser.email ?: "",
            avatar = remoteUser.avatar ?: ""
        )
    }
}