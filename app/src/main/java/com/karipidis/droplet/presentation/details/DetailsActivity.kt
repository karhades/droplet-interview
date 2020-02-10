package com.karipidis.droplet.presentation.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.karipidis.droplet.R
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity() {

    private val viewModel: DetailsViewModel by viewModel()
    private var userId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        pick_avatar_image_view.setOnClickListener { pickImage() }
        save_button.setOnClickListener { updateUser() }
        observeViewModel()
        userId = intent.getStringExtra(EXTRA_USER_ID) ?: ""
        viewModel.getUser(userId)
    }

    private fun pickImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE)
    }

    private fun updateUser() {
        val defaultBitmap = ContextCompat.getDrawable(this, R.drawable.layer_list_add_photo)
            ?.toBitmap()!!
        val currentBitmap = pick_avatar_image_view.drawable.toBitmap()
        // Avoid sending the default bitmap to server
        val bitmap = if (defaultBitmap.sameAs(currentBitmap)) null else currentBitmap
        val detailsUser = DetailsUser(
            id = userId,
            avatar = bitmap,
            firstName = first_name_edit_text.text.toString(),
            lastName = last_name_edit_text.text.toString(),
            email = email_edit_text.text.toString()
        )
        viewModel.updateUser(detailsUser)
    }

    private fun observeViewModel() {
        val owner = this@DetailsActivity
        viewModel.run {
            detailsUser.observe(owner, Observer { showUser(it) })
            loading.observe(owner, Observer { toggleLoading(it) })
            message.observe(owner, Observer { showMessage(it) })
            bitmap.observe(owner, Observer { loadImage(it) })
            invalidFirstName.observe(owner, Observer { showFirstNameError() })
            invalidLastName.observe(owner, Observer { showLastNameError() })
            invalidEmailName.observe(owner, Observer { showEmailError() })
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
        save_button.isEnabled = !isEnabled
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

    private fun showFirstNameError() {
        first_name_edit_text.error = getString(R.string.invalid_first_name)
    }

    private fun showLastNameError() {
        last_name_edit_text.error = getString(R.string.invalid_last_name)
    }

    private fun showEmailError() {
        email_edit_text.error = getString(R.string.invalid_email)
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