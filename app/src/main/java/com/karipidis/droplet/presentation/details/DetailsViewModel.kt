package com.karipidis.droplet.presentation.details

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karipidis.droplet.R
import com.karipidis.droplet.domain.entities.InvalidEmailNameException
import com.karipidis.droplet.domain.entities.InvalidFirstNameException
import com.karipidis.droplet.domain.entities.InvalidLastNameException
import com.karipidis.droplet.domain.entities.Result
import com.karipidis.droplet.domain.usecases.GetUserUseCase
import com.karipidis.droplet.domain.usecases.UpdateUserUseCase
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val detailsUserMapper: DetailsUserMapper,
    private val contentResolver: ContentResolver,
    private val updateUserUseCase: UpdateUserUseCase,
    private val userMapper: UserMapper
) : ViewModel() {

    private val _detailsUser = MutableLiveData<DetailsUser>()
    val detailsUser: LiveData<DetailsUser> = _detailsUser

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _message = MutableLiveData<Int>()
    val message: LiveData<Int> = _message.toSingleEvent()

    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> = _bitmap

    private val _invalidFirstName = MutableLiveData<Unit>()
    val invalidFirstName: LiveData<Unit> = _invalidFirstName

    private val _invalidLastName = MutableLiveData<Unit>()
    val invalidLastName: LiveData<Unit> = _invalidLastName

    private val _invalidEmailName = MutableLiveData<Unit>()
    val invalidEmailName: LiveData<Unit> = _invalidEmailName

    fun getUser(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = getUserUseCase(userId)
            _loading.value = false

            when (result) {
                is Result.Success -> _detailsUser.value = detailsUserMapper.map(result.data!!)
                is Result.Error -> _message.value = R.string.error_get_user
            }
        }
    }

    fun handleUri(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        _bitmap.value = bitmap
    }

    fun updateUser(detailsUser: DetailsUser) {
        viewModelScope.launch {
            _loading.value = true
            val user = userMapper.map(detailsUser)
            val result = updateUserUseCase(user)
            _loading.value = false

            when (result) {
                is Result.Success -> _message.value = R.string.update_user_success
                is Result.Error -> handleUpdateUserError(result.throwable)
            }
        }
    }

    private fun handleUpdateUserError(throwable: Throwable?) {
        when (throwable) {
            is InvalidFirstNameException -> _invalidFirstName.value = Unit
            is InvalidLastNameException -> _invalidLastName.value = Unit
            is InvalidEmailNameException -> _invalidEmailName.value = Unit
            else -> _message.value = R.string.error_update_user
        }
    }
}