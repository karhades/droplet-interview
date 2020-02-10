package com.karipidis.droplet.data.mappers

import com.karipidis.droplet.data.remote.responses.RemoteUser
import com.karipidis.droplet.domain.entities.User

class RemoteUserMapper {

    fun map(user: User): RemoteUser {
        return RemoteUser(
            id = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            avatar = user.avatar
        )
    }
}