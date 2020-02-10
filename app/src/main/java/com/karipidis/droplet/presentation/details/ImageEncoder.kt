package com.karipidis.droplet.presentation.details

import android.graphics.Bitmap

/**
 * Encodes [Bitmap]s images into Base64 strings and vise versa.
 */
interface ImageEncoder {

    fun decode(base64: String): Bitmap?

    fun encode(bitmap: Bitmap): String
}
