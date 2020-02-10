package com.karipidis.droplet.domain.repositories

import com.karipidis.droplet.domain.entities.User

interface UserRepository {

    val userId: String?

    suspend fun getUser(userId: String): User
}