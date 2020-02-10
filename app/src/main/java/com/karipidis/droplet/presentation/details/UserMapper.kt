package com.karipidis.droplet.presentation.details

import com.karipidis.droplet.domain.entities.User

class UserMapper(private val imageEncoder: ImageEncoder) {

    fun map(detailsUser: DetailsUser): User {
        return User(
            id = detailsUser.id,
            firstName = detailsUser.firstName,
            lastName = detailsUser.lastName,
            email = detailsUser.email,
            avatar = imageEncoder.encode(detailsUser.avatar)
        )
    }
}