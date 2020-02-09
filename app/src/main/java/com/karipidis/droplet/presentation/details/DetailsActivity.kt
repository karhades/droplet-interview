package com.karipidis.droplet.presentation.details

import android.content.Context
import android.content.Intent
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    companion object {

        @VisibleForTesting
        const val EXTRA_USER_ID = "com.karipidis.droplet.presentation.details.USER_ID"

        fun newIntent(context: Context, userId: String): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userId)
            }
        }
    }
}