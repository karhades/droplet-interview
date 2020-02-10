package com.karipidis.droplet.presentation.welcome

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.karipidis.droplet.R
import com.karipidis.droplet.domain.usecases.GetUserIdUseCase
import com.karipidis.droplet.presentation.details.toSingleEvent

class WelcomeViewModel(private val getUserIdUseCase: GetUserIdUseCase) : ViewModel() {

    private val _startLogin = MutableLiveData<Unit>()
    val startLogin: LiveData<Unit> = _startLogin.toSingleEvent()

    private val _startDetails = MutableLiveData<String>()
    val startDetails: LiveData<String> = _startDetails.toSingleEvent()

    private val _message = MutableLiveData<Int>()
    val message: LiveData<Int> = _message.toSingleEvent()

    fun handleEntrance() {
        val userId = getUserIdUseCase()
        if (userId != null) {
            _startDetails.value = userId
        } else {
            _startLogin.value = Unit
        }
    }

    fun handleLoginResponse(resultCode: Int, idpResponse: IdpResponse?) {
        when {
            resultCode == Activity.RESULT_OK -> {
                _startDetails.value = getUserIdUseCase()
            }
            idpResponse == null -> {
                _message.value = R.string.login_canceled
            }
            idpResponse.error?.errorCode == ErrorCodes.NO_NETWORK -> {
                _message.value = R.string.no_internet
            }
            else -> {
                _message.value = R.string.login_failed
            }
        }
    }
}