package com.karipidis.droplet.domain.usecases

import com.karipidis.droplet.domain.entities.Result
import com.karipidis.droplet.domain.entities.User

interface UpdateUserUseCase {

    suspend operator fun invoke(user: User): Result<Unit>
}