package com.karipidis.droplet.data

import com.karipidis.droplet.domain.entities.User
import com.karipidis.droplet.domain.repositories.UserRepository

class FakeUserRepository : UserRepository {

    var _userId: String? = null

    override val userId: String?
        get() = _userId

    var user: User = User("", "", "", "")

    var throwGetUserError: Boolean = false

    override suspend fun getUser(userId: String): User {
        require(!throwGetUserError)
        return user
    }
}