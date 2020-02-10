package com.karipidis.droplet.presentation.details

import com.karipidis.droplet.domain.entities.User

class DetailsUserMapper(private val imageEncoder: ImageEncoder) {

    fun map(user: User): DetailsUser {
        return DetailsUser(
            avatar = imageEncoder.decode(user.avatar),
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email
        )
    }
}