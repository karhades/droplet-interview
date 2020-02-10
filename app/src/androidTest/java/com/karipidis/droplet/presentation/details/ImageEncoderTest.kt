package com.karipidis.droplet.presentation.details

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.filters.MediumTest
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.io.BufferedReader

@MediumTest
class ImageEncoderTest {

    private lateinit var imageEncoderImpl: ImageEncoderImpl

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        imageEncoderImpl = ImageEncoderImpl()
    }

    @Test
    fun encodeBitmapReturnsCorrectString() {
        val bitmap = getBitmapFromAssets()
        val base64String = getBase64StringFromAssets()

        val result = imageEncoderImpl.encode(bitmap)

        assertThat(result).isEqualTo(base64String)
    }

    @Test
    fun decodeReturnCorrectBitmap() {
        val bitmap = getBitmapFromAssets()
        val base64String = getBase64StringFromAssets()

        val result = imageEncoderImpl.decode(base64String)

        assertThat(bitmap.sameAs(result)).isTrue()
    }

    private fun getBitmapFromAssets(): Bitmap {
        val assetManager = context.assets
        val inputStream = assetManager.open("avatar.png")
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun getBase64StringFromAssets(): String {
        val assetManager = context.assets
        val inputStream = assetManager.open("base64.txt")
        return inputStream.bufferedReader()
            .use(BufferedReader::readText)
            .replace("\r", "")
    }
}