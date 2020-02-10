package com.karipidis.droplet.domain

import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.karipidis.droplet.domain.entities.InvalidEmailNameException
import com.karipidis.droplet.domain.entities.InvalidFirstNameException
import com.karipidis.droplet.domain.entities.InvalidLastNameException
import com.karipidis.droplet.domain.entities.User
import com.karipidis.droplet.domain.repositories.UserRepository
import com.karipidis.droplet.domain.usecases.UpdateUserUseCaseImpl
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

@SmallTest
class UpdateUserUseCaseImplTest {

    private lateinit var updateUserUseCaseImpl: UpdateUserUseCaseImpl

    @MockK
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        updateUserUseCaseImpl = UpdateUserUseCaseImpl(userRepository)
    }

    @Test
    fun `throws exception when first name is invalid`() = runBlocking {
        val user = User(
            id = "userId",
            avatar = "avatar",
            firstName = "",
            lastName = "lastName",
            email = "email"
        )

        val result = updateUserUseCaseImpl(user)

        assertThat(result.throwable).isInstanceOf(InvalidFirstNameException::class.java)
    }

    @Test
    fun `throws exception when last name is invalid`() = runBlocking {
        val user = User(
            id = "userId",
            avatar = "avatar",
            firstName = "firstName",
            lastName = "",
            email = "email"
        )

        val result = updateUserUseCaseImpl(user)

        assertThat(result.throwable).isInstanceOf(InvalidLastNameException::class.java)
    }

    @Test
    fun `throws exception when email is invalid`() = runBlocking {
        val user = User(
            id = "userId",
            avatar = "avatar",
            firstName = "firstName",
            lastName = "lastName",
            email = ""
        )

        val result = updateUserUseCaseImpl(user)

        assertThat(result.throwable).isInstanceOf(InvalidEmailNameException::class.java)
    }
}