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
import com.karipidis.droplet.domain.entities.Result
import com.karipidis.droplet.domain.usecases.GetUserUseCase
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getUserUseCase: GetUserUseCase,
    private val detailsUserMapper: DetailsUserMapper,
    private val contentResolver: ContentResolver
) : ViewModel() {

    private val _detailsUser = MutableLiveData<DetailsUser>()
    val detailsUser: LiveData<DetailsUser> = _detailsUser

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _message = MutableLiveData<Int>()
    val message: LiveData<Int> = _message.toSingleEvent()

    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> = _bitmap

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
}