package com.karipidis.droplet.presentation.welcome

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseUiException
import com.firebase.ui.auth.IdpResponse
import com.google.common.truth.Truth.assertThat
import com.karipidis.droplet.LiveDataTest
import com.karipidis.droplet.R
import com.karipidis.droplet.domain.usecases.GetUserIdUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@SmallTest
class WelcomeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WelcomeViewModel

    @MockK
    private lateinit var getUserIdUseCase: GetUserIdUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = false)
        viewModel = WelcomeViewModel(getUserIdUseCase)
    }

    @Test
    fun `handleEntrance starts login when user id is null`() {
        every { getUserIdUseCase() } returns null

        viewModel.handleEntrance()

        assertThat(LiveDataTest.getValue(viewModel.startLogin)).isEqualTo(Unit)
    }

    @Test
    fun `handleEntrance starts details when user id is not null`() {
        every { getUserIdUseCase() } returns USER_ID

        viewModel.handleEntrance()

        assertThat(LiveDataTest.getValue(viewModel.startDetails)).isEqualTo(USER_ID)
    }

    @Test
    fun `handleLoginResponse starts details when result ok`() {
        every { getUserIdUseCase() } returns USER_ID

        viewModel.handleLoginResponse(Activity.RESULT_OK, null)

        assertThat(LiveDataTest.getValue(viewModel.startDetails)).isEqualTo(USER_ID)
    }

    @Test
    fun `handleLoginResponse shows message when no network`() {
        val idpResponse = IdpResponse.from(FirebaseUiException(ErrorCodes.NO_NETWORK))
        viewModel.handleLoginResponse(Activity.RESULT_CANCELED, idpResponse)

        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(R.string.no_internet)
    }

    @Test
    fun `handleLoginResponse shows message when idpResponse is null`() {
        viewModel.handleLoginResponse(Activity.RESULT_CANCELED, null)

        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(R.string.login_canceled)
    }

    @Test
    fun `handleLoginResponse shows message when unknown error`() {
        val idpResponse = IdpResponse.from(FirebaseUiException(ErrorCodes.UNKNOWN_ERROR))
        viewModel.handleLoginResponse(Activity.RESULT_CANCELED, idpResponse)

        assertThat(LiveDataTest.getValue(viewModel.message)).isEqualTo(R.string.login_failed)
    }

    companion object {

        private const val USER_ID = "user_id"
    }
}