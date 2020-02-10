package com.karipidis.droplet.presentation.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.karipidis.droplet.R
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity() {

    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        observeViewModel()
        val userId = intent.getStringExtra(EXTRA_USER_ID) ?: ""
        viewModel.getUser(userId)
    }

    private fun observeViewModel() {
        val owner = this@DetailsActivity
        viewModel.run {
            detailsUser.observe(owner, Observer { showUser(it) })
            loading.observe(owner, Observer { toggleLoading(it) })
            message.observe(owner, Observer { showMessage(it) })
        }
    }

    private fun showUser(user: DetailsUser) {
        if (user.avatar != null) pick_avatar_image_button.setImageBitmap(user.avatar)
        first_name_edit_text.setText(user.firstName)
        last_name_edit_text.setText(user.lastName)
        email_edit_text.setText(user.email)
    }

    private fun toggleLoading(isEnabled: Boolean) {
        progress_bar.isVisible = isEnabled
    }

    private fun showMessage(@StringRes stringRes: Int) {
        Snackbar.make(root_layout, stringRes, Snackbar.LENGTH_SHORT)
            .show()
    }

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