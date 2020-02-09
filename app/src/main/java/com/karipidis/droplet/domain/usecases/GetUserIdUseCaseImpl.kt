package com.karipidis.droplet.domain.usecases

import com.karipidis.droplet.domain.repositories.UserRepository

class GetUserIdUseCaseImpl(private val userRepository: UserRepository) : GetUserIdUseCase {

    override fun invoke(): String? {
        return userRepository.userId
    }
}