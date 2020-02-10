package com.karipidis.droplet.data

import com.karipidis.droplet.domain.entities.User
import com.karipidis.droplet.domain.repositories.UserRepository
import kotlinx.coroutines.delay

class FakeUserRepository : UserRepository {

    var _userId: String? = null

    override val userId: String?
        get() = _userId

    var user: User = User("", "", "", "", "")

    var throwGetUserError: Boolean = false
    var throwUpdateUserError: Boolean = false
    var delayUpdateUser: Boolean = false

    override suspend fun getUser(userId: String): User {
        require(!throwGetUserError)
        return user
    }

    override suspend fun updateUser(user: User) {
        if (delayUpdateUser) delay(500)
        require(!throwUpdateUserError)
    }
}