package com.karipidis.droplet.data.mappers

import com.karipidis.droplet.data.local.LocalUser
import com.karipidis.droplet.domain.entities.User

class UserMapper {

    fun map(localUser: LocalUser): User {
        return User(
            firstName = localUser.firstName,
            lastName = localUser.lastName,
            email = localUser.email,
            avatar = localUser.avatar
        )
    }
}