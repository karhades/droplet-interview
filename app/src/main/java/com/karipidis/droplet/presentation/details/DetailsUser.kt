package com.karipidis.droplet.presentation.details

import android.graphics.Bitmap

data class DetailsUser(
    val avatar: Bitmap?,
    val firstName: String,
    val lastName: String,
    val email: String
)