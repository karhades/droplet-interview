package com.karipidis.droplet.domain.usecases

import com.karipidis.droplet.domain.repositories.UserRepository

class LogoutUseCaseImpl(private val userRepository: UserRepository) : LogoutUseCase {

    override suspend fun invoke() {
        userRepository.logout()
    }
}