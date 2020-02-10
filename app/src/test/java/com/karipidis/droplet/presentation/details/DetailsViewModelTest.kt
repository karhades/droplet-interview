package com.karipidis.droplet.presentation.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.karipidis.droplet.LiveDataTest
import com.karipidis.droplet.R
import com.karipidis.droplet.domain.entities.Result
import com.karipidis.droplet.domain.entities.User
import com.karipidis.droplet.domain.usecases.GetUserUseCase
import com.karipidis.droplet.presentation.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@SmallTest
class DetailsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: DetailsViewModel

    @MockK
    private lateinit var getUserUseCase: GetUserUseCase

    @MockK
    private lateinit var detailsUserMapper: DetailsUserMapper

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = DetailsViewModel(getUserUseCase, detailsUserMapper)
    }

    @Test
    fun `getUser emits message when result is error`() {
        coEvery { getUserUseCase(USER_ID) } returns Result.Error(Throwable())

        viewModel.getUser(USER_ID)

        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(R.string.error_get_user)
    }

    @Test
    fun `getUser emits detailsUser when result is success`() {
        val user = User(
            avatar = "avatar",
            firstName = "firstName",
            lastName = "lastName",
            email = "email"
        )
        val result = Result.Success(user)
        val detailsUser = DetailsUser(
            avatar = null,
            firstName = "firstName",
            lastName = "lastName",
            email = "email"
        )
        coEvery { getUserUseCase(USER_ID) } returns result
        every { detailsUserMapper.map(user) } returns detailsUser

        viewModel.getUser(USER_ID)

        assertThat(LiveDataTest.getValue(viewModel.detailsUser)).isEqualTo(detailsUser)
    }

    companion object {

        private const val USER_ID = "user_id"
    }
}