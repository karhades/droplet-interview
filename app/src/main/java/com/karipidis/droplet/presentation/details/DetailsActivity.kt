package com.karipidis.droplet.presentation.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.karipidis.droplet.R
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity() {

    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        pick_avatar_image_view.setOnClickListener { pickImage() }
        observeViewModel()
        val userId = intent.getStringExtra(EXTRA_USER_ID) ?: ""
        viewModel.getUser(userId)
    }

    private fun pickImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE)
    }

    private fun observeViewModel() {
        val owner = this@DetailsActivity
        viewModel.run {
            detailsUser.observe(owner, Observer { showUser(it) })
            loading.observe(owner, Observer { toggleLoading(it) })
            message.observe(owner, Observer { showMessage(it) })
            bitmap.observe(owner, Observer { loadImage(it) })
        }
    }

    private fun showUser(user: DetailsUser) {
        if (user.avatar != null) loadImage(user.avatar)
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

    private fun loadImage(bitmap: Bitmap) {
        Glide.with(this)
            .load(bitmap)
            .circleCrop()
            .into(pick_avatar_image_view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED || data == null) return
        if (requestCode == REQUEST_IMAGE) viewModel.handleUri(data.data ?: return)
    }

    companion object {

        private const val REQUEST_IMAGE = 1

        @VisibleForTesting
        const val EXTRA_USER_ID = "com.karipidis.droplet.presentation.details.USER_ID"

        fun newIntent(context: Context, userId: String): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_USER_ID, userId)
            }
        }
    }
}