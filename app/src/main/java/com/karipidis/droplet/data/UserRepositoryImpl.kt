package com.karipidis.droplet.data

import com.google.firebase.auth.FirebaseAuth
import com.karipidis.droplet.data.local.UserDao
import com.karipidis.droplet.data.mappers.LocalUserMapper
import com.karipidis.droplet.data.mappers.RemoteUserMapper
import com.karipidis.droplet.data.mappers.UserMapper
import com.karipidis.droplet.data.remote.UserApi
import com.karipidis.droplet.domain.entities.User
import com.karipidis.droplet.domain.repositories.UserRepository

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val userApi: UserApi,
    private val userMapper: UserMapper,
    private val userDao: UserDao,
    private val localUserMapper: LocalUserMapper,
    private val remoteUserMapper: RemoteUserMapper
) : UserRepository {

    override val userId: String?
        get() = firebaseAuth.currentUser?.uid

    override suspend fun getUser(userId: String): User {
        val cachedUser = userDao.getUser(userId)
        return if (cachedUser == null) {
            // Fetch user
            val response = userApi.getUser(userId)
            val remoteUser = response.remoteUser
            // Save user to database
            val localUser = localUserMapper.map(remoteUser)
            userDao.addUser(localUser)
            // Return fetched user
            userMapper.map(localUser)
        } else {
            userMapper.map(cachedUser)
        }
    }

    override suspend fun updateUser(user: User) {
        val remoteUser = remoteUserMapper.map(user)
        userApi.updateUser(remoteUser)
        val localUser = localUserMapper.map(remoteUser)
        userDao.addUser(localUser)
    }
}