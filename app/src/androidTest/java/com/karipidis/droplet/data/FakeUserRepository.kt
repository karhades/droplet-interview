package com.karipidis.droplet.data

import com.karipidis.droplet.domain.repositories.UserRepository

class FakeUserRepository : UserRepository {

    var _userId: String? = null

    override val userId: String?
        get() = _userId
}