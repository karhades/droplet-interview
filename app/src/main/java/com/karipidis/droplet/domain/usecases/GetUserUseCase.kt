package com.karipidis.droplet.domain.usecases

import com.karipidis.droplet.domain.entities.Result
import com.karipidis.droplet.domain.entities.User

interface GetUserUseCase {

    suspend operator fun invoke(userId: String): Result<User>
}