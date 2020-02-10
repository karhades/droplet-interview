package com.karipidis.droplet.domain.usecases

import com.karipidis.droplet.domain.entities.*
import com.karipidis.droplet.domain.repositories.UserRepository

class UpdateUserUseCaseImpl(private val userRepository: UserRepository) : UpdateUserUseCase {

    override suspend fun invoke(user: User): Result<Unit> {
        return try {
            if (user.firstName.isBlank()) throw InvalidFirstNameException()
            if (user.lastName.isBlank()) throw InvalidLastNameException()
            if (user.email.isBlank()) throw InvalidEmailNameException()
            userRepository.updateUser(user)
            Result.Success(Unit)
        } catch (t: Throwable) {
            Result.Error(t)
        }
    }
}