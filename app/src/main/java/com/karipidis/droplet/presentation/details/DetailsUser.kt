package com.karipidis.droplet.presentation.details

import android.graphics.Bitmap

data class DetailsUser(
    val id: String,
    val avatar: Bitmap?,
    val firstName: String,
    val lastName: String,
    val email: String
)