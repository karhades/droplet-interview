package com.karipidis.droplet.domain.usecases

import com.karipidis.droplet.domain.entities.Result
import com.karipidis.droplet.domain.entities.User
import com.karipidis.droplet.domain.repositories.UserRepository

class GetUserUseCaseImpl(private val userRepository: UserRepository) : GetUserUseCase {

    override suspend fun invoke(userId: String): Result<User> {
        return try {
            Result.Success(userRepository.getUser(userId))
        } catch (t: Throwable) {
            Result.Error(t)
        }
    }
}